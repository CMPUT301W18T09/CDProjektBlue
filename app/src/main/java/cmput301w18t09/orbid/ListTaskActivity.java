package cmput301w18t09.orbid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ListTaskActivity extends NavigationActivity {

    private ArrayList<Task> taskList;
    private ListView listView;
    private TaskListAdapter taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_list_task);
        super.onCreate(savedInstanceState);
    }

    // Used on some type of swipe functionality
    // Will use data man to search server for results
    private void filterList(String key, String value)
    {

    }

    private void openUserProfileDialog()
    {

    }
}
