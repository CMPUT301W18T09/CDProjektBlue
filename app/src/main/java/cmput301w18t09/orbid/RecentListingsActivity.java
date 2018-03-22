package cmput301w18t09.orbid;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ALL")
/**
 * The main screen of the application shown post-login. Displays a list of all tasks on the
 * server that have been requested or bidded on. Also features a search bar to filter results.
 *
 * @author Aidan Kosik
 */
public class RecentListingsActivity extends NavigationActivity implements ItemClickListener {

    private ArrayList<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskListAdapter taskListAdapter;
    private Switch tbtnToggle;
    private DrawerLayout mDrawerLayout;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        tbtnToggle = new Switch(getBaseContext());
        tbtnToggle.setTextOn("List");
        tbtnToggle.setTextOff("Map");
        tbtnToggle.setChecked(true);
        tbtnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    openMapActivity();
                }
                else {
                    Intent intent = new Intent(getBaseContext(), RecentListingsActivity.class);
                    intent.putExtra("recent_listings_layout_id", R.layout.activity_recent_listings);
                    getBaseContext().startActivity(intent);
                }
            }
        });
        toolbar.addView(tbtnToggle);

        // Fill taskList with all tasks
        getListings();
        taskListAdapter = new TaskListAdapter(this, taskList, 0);
        taskListAdapter.setClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setHasFixedSize(true);
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
     * Function for when an options item is selected.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        Intent intent = new Intent(this, PlaceBidActivity.class);
        intent.putExtra("layout_id", R.layout.activity_place_bid);
        intent.putExtra("_id", taskList.get(position).getID());
        this.startActivity(intent);
    }

    /**
     * When a task is clicked the Task Details are opened in a new activity.
     *
     * @param view
     * @param position
     * @param type
     */
    @Override
    public void onClick(View view, int position, int type) {
        openPlaceBidActivity(position);
    }

    /**
     * Gets the recycler list view in the activity
     *
     * @return The recycler list view in the activity
     */
    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }
}
