package cmput301w18t09.orbid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListTaskActivity extends NavigationActivity {

    private ArrayList<Task> taskList = new ArrayList<>();
    private ListView listView;
    private TaskListAdapter taskListAdapter;
    private int currentPage=0;
    private CardView cardView;
    private int isMyBids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutID = getIntent().getIntExtra("tasks_layout_id", 0);
        isMyBids = getIntent().getIntExtra("isMyBids", 0);
        changeLayout();
        User testUser = new User("NanTheMAN", "Nan@hotmail.com","1800NAN", "NAN", "THEMAN");
        Task task = new Task(testUser, "", "", 0, Task.TaskStatus.REQUESTED);
        taskList.add(task);
        // Selection for either a list of Tasks you Bid on,
        // Or a list of tasks you requested
        if(isMyBids==1) {

        } else {
            Button addButton = (Button) findViewById(R.id.AddTaskButton);
        }
        loadTasks();
        swipeInit();
    }

    /**
     * Opens the Add/Edit task activity when the button is pressed
     * @param view
     */
    public void addTask(View view) {
        Intent addTask = new Intent(ListTaskActivity.this, AddEditTaskActivity.class);
        addTask.putExtra("addedit_layout_id", R.layout.activity_add_edit_task);
        addTask.putExtra("isAdd", 1);
        startActivity(addTask);
    }


    private void swipeInit() {
        cardView.setOnTouchListener(new OnSwipeTouchListener(ListTaskActivity.this) {
            public void onSwipeRight() {
                if(currentPage>0) {
                    currentPage--;
                    changeLayout();
                }
            }
            public void onSwipeLeft() {
                if(currentPage<3) {
                    currentPage++;
                    changeLayout();
                }
            }
        });
    }

    /**
     * Handles setting up which
     */
    private void changeLayout() {
        int view=R.id.RequestedTasks;
        // Prepare to inflate layout
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        // Select which layout to inflate
        switch(currentPage){
            case 0:
                view = R.id.RequestedTasks;
                getSupportActionBar().setTitle("My requested Tasks");
                inflater.inflate(R.layout.activity_list_requested_tasks, frameLayout);

                break;
            case 1:
                view = R.id.RequestedTasksFinished;
                inflater.inflate(R.layout.layout_list_requested_tasks2, frameLayout);
                getSupportActionBar().setTitle("My Finished Tasks");
                break;
        }
        // Setup the card view to show tasks
        cardView = (CardView) findViewById(view);
        TaskListAdapter taskAdapter = new TaskListAdapter(this, taskList);
    }

    // Used on some type of swipe functionality
    // Will use data man to search server for results
    private void filterList(String key, String value)
    {

    }

    private void loadTasks() {

    }

    private void openUserProfileDialog()
    {

    }
}
