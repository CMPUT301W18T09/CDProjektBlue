package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class TaskDetailsActivity extends NavigationActivity{

    private DrawerLayout mDrawerLayout;
    private ArrayList<Task> taskList = new ArrayList<>();
    private String id;
    public Task task;
    public Bid lowest_bid;

    /**
     * Inflates the layout for task details. Sets the details of the task
     * being viewed. Initialises the stackView for the images of the task.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout the XML that goes to the corresponding child that is being inflated
        // Then setup the generic parts.
        int layoutID = getIntent().getIntExtra("layout_id", 0);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(layoutID, frameLayout);
        //setContentView(R.layout.activity_place_bid);
        // Use the id of the task to get it from the Data Manager
        id = getIntent().getStringExtra("_id");
        ArrayList<String> query = new ArrayList<>();
        query.add("and");
        query.add("_id");
        query.add(id);
        DataManager.getTasks getTasks = new DataManager.getTasks(this);
        getTasks.execute(query);
        try {
            taskList = getTasks.get();
            task = taskList.get(0);
            Log.i("MSG", "got the task");
        } catch (InterruptedException e) {
            Log.i("MSG", "interrupted execution");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.i("MSG", "execution");
            e.printStackTrace();
        }
        // Find the text views in the layout
        TextView task_title = findViewById(R.id.details_task_title);
        TextView task_description = findViewById(R.id.details_task_description);
        TextView text_lowest_bid = findViewById(R.id.details_lowest_bid);
        // find lowest bid
        lowest_bid = null;
        if (task == null) {
            Log.i("MSG", "task is null here");
        }
        for (Bid bid : task.getBidList()) {
            if (lowest_bid != null) {
                if (bid.getPrice() < lowest_bid.getPrice()) {
                    lowest_bid = bid;
                }
            } else {
                lowest_bid = bid;
            }
        }
        if(lowest_bid != null) {
            text_lowest_bid.setText("Lowest Bid:$" + Double.toString(lowest_bid.getPrice()));
        }


        task_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo set on click to show user details
            }
        });

        task_title.setText(task.getTitle());
        task_description.setText(task.getDescription());
        Log.i("MSG", task_title.getText().toString());


        //assert lowest_bid != null;

//             Setting up the stack view for the images when you add a Task
//            StackView stackView = findViewById(R.id.stackView);
//            stackView.setInAnimation(this, android.R.animator.fade_in);
//            stackView.setOutAnimation(this, android.R.animator.fade_out);
//
//            ImageViewAdapter imageViewAdapter = new ImageViewAdapter(this, task.getPhotoList());
//            stackView.setAdapter(imageViewAdapter);
    }

    /**
     * Function for when an options menu item is selected.
     * @param item
     * @return boolean if the item was selected
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
}
