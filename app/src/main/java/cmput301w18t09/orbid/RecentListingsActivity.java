package cmput301w18t09.orbid;


import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ALL")
/**
 * The main screen of the application shown post-login. Displays a list of all tasks on the
 * server that have been requested or bidded on. Also features a search bar to filter results.
 *
 * @author Aidan Kosik, Zach Redfern
 */
public class RecentListingsActivity extends NavigationActivity implements ItemClickListener {

    private ArrayList<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskListAdapter taskListAdapter;
    private Switch tbtnToggle;
    private DrawerLayout mDrawerLayout;
    private SearchView searchView;
    private boolean permissionsGranted = false;

    /**
     * Sets the switch for list view and map view in the toolbar, creates onClick
     * for the switch and initialises the recyclerView for the task.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutID = getIntent().getIntExtra("recent_listings_layout_id", 0);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(layoutID, frameLayout);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M || permissionsGranted) {
            permissionsGranted = checkLocationPermission();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        tbtnToggle = new Switch(getBaseContext());
        tbtnToggle.setTextOn("List");
        tbtnToggle.setTextOff("Map");
        tbtnToggle.setChecked(true);
        tbtnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    if (!permissionsGranted) {
                        permissionsGranted = checkLocationPermission();
                        tbtnToggle.setChecked(true);
                    } else {
                        openMapActivity();
                    }
                }
                else {
                    Intent intent = new Intent(getBaseContext(), RecentListingsActivity.class);
                    intent.putExtra("recent_listings_layout_id", R.layout.activity_recent_listings);
                    getBaseContext().startActivity(intent);
                }
            }
        });
        toolbar.addView(tbtnToggle);


        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                refineListings(1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // If search string is empty, refresh the listings
                refineListings(0);
                return false;
            }
        });

        // Fill taskList with all tasks if the network is available, otherwise report
        // to the user that that functionality is not available offline
        if (DataManager.isNetworkAvailable(this )) {
            getListings();
        }
        else {
            Toast.makeText(this, "Recent listings cannot be fetched while offline", Toast.LENGTH_SHORT).show();
        }


        taskListAdapter = new TaskListAdapter(this, taskList, 0);
        taskListAdapter.setClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_reviews);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setHasFixedSize(true);
    }


    /**
     * Sets the refresh (index 0) button to visible and the add (index 1) button to invisible
     *
     * @param menu The menu and it's items
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(false);
        return true;
    }

    /**
     * Function for when an options item is selected.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.MenuItem_RefreshButton) {
            getListings();
            taskListAdapter.setTaskList(taskList);
            taskListAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets all of the tasks that are requested or bidded and stores
     * them in the taskList ArrayList.
     */
    private void getListings() {
        DataManager.getTasks getTasks = new DataManager.getTasks(this);
        ArrayList<String> query = new ArrayList<String>();
        query.add("or");
        query.add("status");
        query.add(Task.TaskStatus.REQUESTED.toString());
        query.add("status");
        query.add(Task.TaskStatus.BIDDED.toString());
        getTasks.execute(query);
        try {
            taskList = getTasks.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refines the listings by keywords provided in the search bar. Only those tasks whose
     * descriptions contain all keywords are shown to the user.
     */
    private void refineListings(Integer type) {

        // Don't attempt a search if we are offline
        if (!DataManager.isNetworkAvailable(this )) {
            Toast.makeText(this, "Cannot perform search while offline", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set up the data manager and split up the search query into tokens
        StringTokenizer tokenizer = new StringTokenizer(searchView.getQuery().toString());
        DataManager.getTasks getTasks = new DataManager.getTasks(this);
        ArrayList<String> searchParams = new ArrayList<>();

        // Build the elastic search query to
        String token = "";
        searchParams.add("and");
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            Log.e("token", token);
            searchParams.add("description");
            searchParams.add(token);
        }

        // Execute the search
        try {
            getTasks.execute(searchParams);
            taskList = getTasks.get();
            Collections.reverse(taskList);

            // If there were no results, tell the user
            if (taskList.size() == 0 && type == 1) {
                Toast.makeText(this, "Search returned no results", Toast.LENGTH_SHORT).show();
                // TODO: Handle no tasks?
            }

            // Remove results with status assigned or bidded
            Task.TaskStatus status;
            ListIterator<Task> it = taskList.listIterator();
            while(it.hasNext()) {
                status = it.next().getStatus();
                if (status == Task.TaskStatus.ASSIGNED) {
                    it.remove();
                }
                else if (status == Task.TaskStatus.COMPLETED) {
                    it.remove();
                }
            }
            taskListAdapter.setTaskList(taskList);

        } catch (Exception e) {
            Log.e("Refine Tasks Error", "There was an error refining the tasks list");
        }

        taskListAdapter.notifyDataSetChanged();
    }


    /**
     * Parses a string of keywords and returns a result list of
     * the matching tasks.
     *
     * @param string
     * @return resultList
     */
    public ArrayList<Task> search(String string) {
        ArrayList<Task> resultList = new ArrayList<Task>();
        return resultList;
    }

    /**
     * Opens the MapActivity on the switch from List View to Map View
     */
    public void openMapActivity() {
        MapActivity mapActivity = new MapActivity();
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("came_from", "recent_listings");
        mapActivity.setArguments(bundle);
        fm.beginTransaction().replace(R.id.navigation_content_frame, mapActivity).commit();
    }

    /**
     * Opens the place bid activity when the user chooses to place
     * a bid on a task.
     */
    public void openPlaceBidActivity(int position) {
        Intent intent = new Intent(this, TaskDetailsActivity.class);
        intent.putExtra("layout_id", R.layout.activity_new_task_details);
        intent.putExtra("_id", taskList.get(position).getID());
        this.startActivity(intent);
    }

    /**
     * When a task is clicked the Task Details are opened in a new activity only
     * if a network connection is available. (Cannot bid offline.)
     *
     * @param view
     * @param position
     * @param type
     */
    @Override
    public void onClick(View view, int position, int type) {
        if (DataManager.isNetworkAvailable(this )) {
            openPlaceBidActivity(position);
        }
        else {
            Toast.makeText(this, "Cannot open task details for bidding while offline", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Gets the recycler list view in the activity
     *
     * @return The recycler list view in the activity
     */
    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }

    /**
     * Checks if the user has given the app permissions to access location services.
     * If not, it prompts the user to allow them.
     */
    private boolean checkLocationPermission() {
        Context context = getBaseContext();
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        } else if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
}
