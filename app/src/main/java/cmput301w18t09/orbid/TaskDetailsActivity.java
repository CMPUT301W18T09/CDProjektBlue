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
import android.widget.RelativeLayout;
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
    public View view;

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
        view = inflater.inflate(layoutID, frameLayout);
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

        // Find the text views in the layout
        included_view = (View) view.findViewById(R.id.RelativeLayout);
        if (included_view == null) {
            Log.e("UGH", "view not found");
        } else {
            Log.e("UGH", "found view");
        }
        task_title = included_view.findViewById(R.id.task_title);
        if (task_title == null) {
            Log.e("UGH", "title not found");
        } else {
            Log.e("UGH", "found title: " + task.getTitle());
        }
        task_description = included_view.findViewById(R.id.task_description);
        if (task_description == null) {
            Log.e("UGH", "desc not found");
        } else {
            Log.e("UGH", "found desc: " + task.getDescription());
        }
        text_lowest_bid = included_view.findViewById(R.id.lowest_bid);
        if (text_lowest_bid == null) {
            Log.e("UGH", "lowest not found");
        } else {
            Log.e("UGH", "found lowest");
        }
        task_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo set on click to show user details
            }
        });

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
