package cmput301w18t09.orbid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ToggleButton;


import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RecentListingsActivity extends NavigationActivity implements ItemClickListener {

    private ArrayList<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskListAdapter taskListAdapter;
    private ToggleButton tbtnToggle;
    private DrawerLayout mDrawerLayout;


    /**
     * Sets the switch for list view and map view in the toolbar, creates onClick
     * for the switch and initialises the recyclerView for the task.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Must call in this order, NOT super first
        super.onCreate(savedInstanceState);
        int layoutID = getIntent().getIntExtra("recent_listings_layout_id", 0);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(layoutID, frameLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Switch change_view_switch = new Switch(getBaseContext());
        change_view_switch.setTextOn("List");
        change_view_switch.setTextOff("Map");
        change_view_switch.setChecked(true);
        change_view_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        toolbar.addView(change_view_switch);

        DataManager.getTasks getTasks = new DataManager.getTasks();
        getTasks.execute(new ArrayList<String>());
        try {
            taskList = getTasks.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TaskListAdapter taskListAdapter = new TaskListAdapter(this, taskList, 0);
        taskListAdapter.setClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setHasFixedSize(true);

    }


    /**
     * Function for when an options item is selected.
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
     * @param string
     * @return resultList
     */
    public ArrayList<Task> search(String string)
    {
        ArrayList<Task> resultList = new ArrayList<Task>();
        return resultList;
    }

    /**
     * Opens the MapActivity on the switch from List View to Map View
     */
    public void openMapActivity()
    {
        MapActivity mapActivity = new MapActivity();
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("type", "recent_listings");
        mapActivity.setArguments(bundle);
        fm.beginTransaction().replace(R.id.navigation_content_frame, mapActivity).commit();
    }

    /**
     * Opens the place bid activity when the user chooses to place
     * a bid on a task.
     */
    public void openPlaceBidActivity()
    {

    }

    /**
     * When the user clicks on a username of the requester
     * It opens a dialog with that user's information
     */
    public void openUserProfileDialog()
    {

    }

    /**
     * When a task is clicked the Task Details are opened in a new activity.
     * @param view
     * @param position
     * @param type
     */
    @Override
    public void onClick(View view, int position, int type) {
        Intent intent = new Intent(this, PlaceBidActivity.class);
        intent.putExtra("task_details_layout_id", R.layout.activity_task_details);
        intent.putExtra("_id", taskList.get(position).getID());
        this.startActivity(intent);
    }


}
