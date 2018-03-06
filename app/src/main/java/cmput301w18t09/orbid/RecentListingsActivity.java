package cmput301w18t09.orbid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ToggleButton;


import java.util.ArrayList;

public class RecentListingsActivity extends NavigationActivity {

    private ArrayList<Task> taskList;
    private RecyclerView recyclerView;
    private TaskListAdapter taskListAdapter;
    private ToggleButton tbtnToggle;
    private DrawerLayout mDrawerLayout;


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
                    MapActivity mapActivity = new MapActivity();
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.navigation_content_frame, mapActivity).commit();
                }
                else {
                    Intent intent = new Intent(getBaseContext(), RecentListingsActivity.class);
                    intent.putExtra("recent_listings_layout_id", R.layout.activity_recent_listings);
                    getBaseContext().startActivity(intent);
                }
            }
        });
        toolbar.addView(change_view_switch);


        // FOR TESTING PURPOSES
        taskList = new ArrayList<>();
        User user = new User("NAN", "nan@gmail.com", "1", "NAN", "THE MAN");
        Task task = new Task(user, "SOME TASK", "TESTING TASK", 10, Task.TaskStatus.BIDDED);
        taskList.add(task);

        TaskListAdapter taskListAdapter = new TaskListAdapter(this, taskList);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setHasFixedSize(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Will need to parse string for individual keywords
    public ArrayList<Task> search(String string)
    {
        ArrayList<Task> resultList = new ArrayList<Task>();
        return resultList;
    }

    public void openMapActivity()
    {

    }

    public void openPlaceBidActivity()
    {

    }

    public void openUserProfileDialog()
    {

    }

    public void onTaskClick(int position) {
        Intent intent = new Intent(this, TaskDetailsActivity.class);
        intent.putExtra("task_details_layout_id", R.layout.activity_task_details);
        intent.putExtra("position", position);
        startActivity(intent);
    }



}
