package cmput301w18t09.orbid;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskDetailsActivity extends NavigationActivity{

    private DrawerLayout mDrawerLayout;
    private ArrayList<Task> taskList;

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

        taskList = new ArrayList<Task>();
        User user = new User("NAN", "nan@gmail.com", "1", "NAN", "THE MAN");
        Task task = new Task(user, "SOME TASK", "TESTING TASK", 10, Task.TaskStatus.BIDDED);
        taskList.add(task);

        // Todo get task from data manager

        task_title.setText(task.getTitle());
        task_description.setText(task.getDescription());
        // Todo set lowest bid

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

}
