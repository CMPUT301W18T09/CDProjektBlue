package cmput301w18t09.orbid;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.StackView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class TaskDetailsActivity extends NavigationActivity{

    private DrawerLayout mDrawerLayout;
    private ArrayList<Task> taskList = new ArrayList<>();

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

        TextView task_title = findViewById(R.id.task_title);
        TextView task_description = findViewById(R.id.task_description);
//        TextView task_lowest_bid = findViewById(R.id.lowest_bid);
        task_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo set on click to show user details
            }
        });


        // Todo get task from data manager
        String id = getIntent().getStringExtra("id");
        ArrayList<String> query = new ArrayList<>();
        query.add("_id");
        query.add(id);
        DataManager.getTasks getTasks = new DataManager.getTasks(this);
        getTasks.execute(query);
        try {
            taskList = getTasks.get();
            Task task = taskList.get(0);
            task_title.setText(task.getTitle());
            task_description.setText(task.getDescription());
            // Todo set lowest bid

            task_title.setText(task.getTitle());
            task_description.setText(task.getDescription());

            // Setting up the stack view for the images when you add a Task
//            StackView stackView = findViewById(R.id.stackView);
//            stackView.setInAnimation(this, android.R.animator.fade_in);
//            stackView.setOutAnimation(this, android.R.animator.fade_out);
//
//            ImageViewAdapter imageViewAdapter = new ImageViewAdapter(this, task.getPhotoList(), R.layout.layout_stack_view_item);
//            stackView.setAdapter(imageViewAdapter);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

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
