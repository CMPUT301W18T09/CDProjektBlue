package cmput301w18t09.orbid;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * An activity class used to list the requestor/bidders tasks;
 * It will list tasks for requestors, sorted by Requested, Bidded,
 * Assigned and Completed. It will also list the tasks a bidder bidded on
 * Organized by: My Open Bids, My Assigned Bids, and My Completed Bids
 *
 * @author Chady Haidar, Zach Redfern
 */
public class ListTaskActivity extends NavigationActivity implements ItemClickListener{

    private ArrayList<Task> taskList = new ArrayList<>();
    private int currentPage=0;
    private RecyclerView recyclerView;
    private int isMyBids;
    private int maxPages;
    private Task.TaskStatus taskStatus;
    private int shouldWait = 1;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(LoginActivity.getCurrentUsername() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
        }

        // Inflate the layout of the list task activity
        isMyBids = getIntent().getIntExtra("isMyBids", 0);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(R.layout.activity_list_requested_tasks, frameLayout);
        try {
            shouldWait = getIntent().getIntExtra("shouldWait", 0);;
        } catch (Exception e) {}

        // Selection for either a list of Tasks you Bid on,
        // Or a list of tasks you requested

        if(isMyBids==1) {
            maxPages=2;
        } else {
            maxPages=3;
        }
    }



    /**
     * Opens the Add/Edit task activity when the button is pressed
     */
    public void addTask() {
        Intent addTask = new Intent(ListTaskActivity.this, AddEditTaskActivity.class);
        addTask.putExtra("addedit_layout_id", R.layout.activity_add_edit_task);
        addTask.putExtra("isAdd", 1);
        startActivity(addTask);
    }

    /**
     * Sets the refresh button visibility to false (index 0) and the add button to visible (index 1)
     *
     * @param menu The menu and it's items
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        if(isMyBids == 0) {
            // Hide Refresh button in action bar
            menu.getItem(0).setVisible(false);
            // Show Add button in action bar
            menu.getItem(1).setVisible(true);
        } else {
            // Show refresh button in action bar
            menu.getItem(0).setVisible(true);
            // hide Add button in action bar
            menu.getItem(1).setVisible(false);
        }
        return true;
    }

    /**
     * Sets what to do when an options button is pressed.
     *
     * @param item the item that was pressed
     * @return super call to onOptionsItemSelected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.MenuItem_AddButton) {
            Log.i("BUTTON", "add clicked");
            addTask();
        }
        if (id == R.id.MenuItem_RefreshButton) {
            filterList();
            initRecyclerView();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Changes the page forward when the forward button is tapped
     *
     * @param view The current activity view
     */
    public void pageForward(View view) {
        if(currentPage<maxPages) {
            currentPage++;
            changeLayout();
        }
    }

    /**
     * Changes the page backward when the back button tapped
     *
     * @param view The current activity view
     */
    public void pageBack(View view) {
        if(currentPage>0) {
            currentPage--;
            changeLayout();
        }
    }

    /**
     * Changes the layout depending on the current page that the user wishes to see
     */
    private void changeLayout() {

        // Select which layout to inflate
        if(isMyBids==0) {
            switch (currentPage) {
                case 0:
                    getSupportActionBar().setTitle("New Listings");
                    taskStatus = Task.TaskStatus.REQUESTED;
                    break;
                case 1:
                    getSupportActionBar().setTitle("Bidded Listings");
                    taskStatus = Task.TaskStatus.BIDDED;
                    break;
                case 2:
                    getSupportActionBar().setTitle("Assigned Listings");
                    taskStatus = Task.TaskStatus.ASSIGNED;
                    break;
                case 3:
                    getSupportActionBar().setTitle("Completed Listings");
                    taskStatus = Task.TaskStatus.COMPLETED;
                    break;
            }
        } else {
            switch(currentPage) {
                case 0:
                    getSupportActionBar().setTitle("Active Bids");
                    taskStatus = Task.TaskStatus.BIDDED;
                    break;
                case 1:
                    getSupportActionBar().setTitle("My Assignements");
                    taskStatus = Task.TaskStatus.ASSIGNED;
                    break;
                case 2:
                    getSupportActionBar().setTitle("Completed Assignments");
                    taskStatus = Task.TaskStatus.COMPLETED;
                    break;
            }
        }
        // Re-initiate recycler view
        initRecyclerView();
    }

    /**
     * Initializes the recycler view with the task list
     */
    private void initRecyclerView() {
        // Setup the card view to show tasks

        filterList();
        Log.i("LENGTH", Integer.toString(taskList.size()));
        Log.i("PAGE", Integer.toString(currentPage));
        recyclerView = (RecyclerView) findViewById(R.id.RequestedTasks);
        final TaskListAdapter taskAdapter = new TaskListAdapter(this, taskList, 1);
        taskAdapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setHasFixedSize(true);
        taskAdapter.notifyDataSetChanged();

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                taskAdapter.setTaskList(taskList);
                taskAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * Gets the task list for the users requested tasks,
     * Or gets the task list of the Users bidded tasks
     */
    private void filterList() {
        if(isMyBids == 0) {
            ArrayList<String> query = new ArrayList<>();
            query.add("and");
            query.add("requester");
            query.add(thisUser);
            query.add("status");
            query.add(taskStatus.toString());
            DataManager.getTasks getTasks = new DataManager.getTasks(this);
            getTasks.execute(query);
            try {

                // If no network is available, use the backup tasks, else fetch from the server
                if (!DataManager.isNetworkAvailable(this )) {

                    // Create a copy of the backupTasks
                    taskList = new ArrayList<>(DataManager.backupTasks);

                    // Remove tasks that don't have the status of the current filter
                    Task.TaskStatus status;
                    ListIterator<Task> it = taskList.listIterator();
                    while (it.hasNext()) {
                        status = it.next().getStatus();
                        if (status != taskStatus) {
                            it.remove();
                        }
                    }

                }
                else {
                    taskList = getTasks.get();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DataManager.getTasks getTasks = new DataManager.getTasks(this);
            ArrayList<String> arguments = new ArrayList<>();
            arguments.add("and");
            arguments.add("provider");
            arguments.add(thisUser);
            arguments.add("status");
            arguments.add(taskStatus.toString());
            try {
                taskList = getTasks.execute(arguments).get();
            }catch (Exception e){
                Log.e("Error", "Could not return results from Asynctask");
            }
        }
    }

    /**
     * Reloads the tasks when the activity is resumed
     * with a short delay for the DataManager to load
     */
    @Override
    public void onResume(){
        super.onResume();
        if(shouldWait == 1) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {

            }
        } else {
            shouldWait = 1;
        }
        changeLayout();
    }

    /**
     * Handles the scenarios when a task is tapped, and opens the proper activity
     *
     * @param view The current activity view
     * @param position The index of the task tapped in the list
     * @param type
     */
    @Override
    public void onClick(View view, int position, int type) {

        //selection if the task tapped is in My Bids or My Requests
        if(isMyBids == 0) {
            // Opens a task to be editted
            if (currentPage == 0) {
                // is on my requested tasks so should open as edit
                Intent intent = new Intent(this, AddEditTaskActivity.class);
                intent.putExtra("addedit_layout_id", R.layout.activity_add_edit_task);
                intent.putExtra("_id", taskList.get(position).getID());
                intent.putExtra("isAdd", 0);

                // Pass the task to be viewed in case we are offline
                String backupTask = (new Gson().toJson(taskList.get(position)));
                intent.putExtra("backupTask", backupTask);

                this.startActivity(intent);
            }

            // Opens a task that is bidded
            if (currentPage == 1) {
                // is on my bidded tasks so should open as edit
                Intent intent = new Intent(this, AddEditTaskActivity.class);


                intent.putExtra("addedit_layout_id", R.layout.activity_new_add_edit_task);
                intent.putExtra("_id", taskList.get(position).getID());
                intent.putExtra("isAdd", 3);

                // Pass the task to be viewed in case we are offline
                String backupTask = (new Gson().toJson(taskList.get(position)));
                intent.putExtra("backupTask", backupTask);

                this.startActivity(intent);
            }

            // opens my Assigned or my Completed task
            if (currentPage == 2 || currentPage == 3) {
                // Is my assigned so should open Task Details activity
                Intent intent = new Intent(this, TaskDetailsActivity.class);
                intent.putExtra("_id", taskList.get(position).getID());
                if (currentPage == 2) {
                    intent.putExtra("isAssigned", 1);
                } else {
                    intent.putExtra("isAssigned", 2);
                    intent.putExtra("cameFromCompletedRequest", true);
                }

                // Pass the task to be viewed in case we are offline
                String backupTask = (new Gson().toJson(taskList.get(position)));
                intent.putExtra("backupTask", backupTask);

                this.startActivity(intent);
            }
        } else {
            // Opens a Task the user bidded on
            Intent intent = new Intent(this, TaskDetailsActivity.class);
            intent.putExtra("_id", taskList.get(position).getID());
            intent.putExtra("isBid", 1);

            // Pass the task to be viewed in case we are offline
            String backupTask = (new Gson().toJson(taskList.get(position)));
            intent.putExtra("backupTask", backupTask);

            if (currentPage == 2) {
                intent.putExtra("cameFromCompletedBid", true);
            }

            this.startActivity(intent);
        }
    }

    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }
}
