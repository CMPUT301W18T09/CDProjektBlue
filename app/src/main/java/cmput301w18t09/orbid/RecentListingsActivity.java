package cmput301w18t09.orbid;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class RecentListingsActivity extends NavigationActivity {

    private ArrayList<Task> taskList;
    private ListView listView;
    private TaskListAdapter taskListAdapter;
    private ToggleButton tbtnToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Must call in this order, NOT super first
        setContentView(R.layout.activity_recent_listings);
        super.onCreate(savedInstanceState);
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

}
