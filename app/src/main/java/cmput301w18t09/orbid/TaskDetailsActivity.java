package cmput301w18t09.orbid;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.StackView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class TaskDetailsActivity extends NavigationActivity{

    private DrawerLayout mDrawerLayout;
    private ArrayList<Task> taskList = new ArrayList<>();
    private String id;
    private Task task;

    /**
     * Inflates the layout for task details. Sets the details of the task
     * being viewed. Initialises the stackView for the images of the task.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutID = getIntent().getIntExtra("task_details_layout_id", 0);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(layoutID, frameLayout);

        // Find the text views in the layout
        TextView task_title = findViewById(R.id.task_title);
        TextView task_description = findViewById(R.id.task_description);
        TextView text_lowest_bid = findViewById(R.id.lowest_bid);
//        TextView task_lowest_bid = findViewById(R.id.lowest_bid);
        task_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo set on click to show user details
            }
        });


        // Use the id of the task to get it from the Data Manager
        id = getIntent().getStringExtra("_id");
        ArrayList<String> query = new ArrayList<>();
        query.add("_id");
        query.add(id);
        DataManager.getTasks getTasks = new DataManager.getTasks();
        getTasks.execute(query);
        try {
            taskList = getTasks.get();
            task = taskList.get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Set the text for the text views
        task_title.setText(task.getTitle());
        task_description.setText(task.getDescription());
        task_title.setText(task.getTitle());
        task_description.setText(task.getDescription());

        // find lowest bid
        Bid lowest_bid = null;
        for (Bid bid : task.getBidList()) {
            if (lowest_bid == null) {
                lowest_bid = bid;
            } else {
                if (bid.getPrice() < lowest_bid.getPrice()) {
                    lowest_bid = bid;
                }
            }
        }
        assert lowest_bid != null;
        text_lowest_bid.setText(Double.toString(lowest_bid.getPrice()));


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

    /**
     * On click function for the bid button. Adds the bid if the inputted information
     * is valid. Otherwise, it makes a toast telling the user to properly fill out
     * the fields.
     * @param view
     */
    public void placeBid(View view) {
        EditText bid_amount = findViewById(R.id.my_bid_amount);
        EditText bid_desc = findViewById(R.id.my_bid_description);

        if (!bid_amount.getText().toString().isEmpty() && !bid_desc.getText().toString().isEmpty()) {
            User user = new User("McChicken Man", "", "", "Nan", "Man");
            Bid bid = new Bid(user, Double.parseDouble(bid_amount.getText().toString()), bid_desc.getText().toString());
            task.addBid(bid);
            task.setStatus(Task.TaskStatus.BIDDED);
            DataManager.updateTasks updateTasks = new DataManager.updateTasks();
            updateTasks.execute(taskList);

        } else {
            Toast.makeText(this, "You need to fill out both bid fields properly", Toast.LENGTH_SHORT).show();
        }

    }



}
