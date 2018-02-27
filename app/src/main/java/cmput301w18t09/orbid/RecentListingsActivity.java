package cmput301w18t09.orbid;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ToggleButton;
import android.widget.Toolbar;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        Switch change_view_switch = new Switch(getBaseContext());
        change_view_switch.setTextOn("List View");
        change_view_switch.setTextOff("Map View");
        change_view_switch.setChecked(true);
        toolbar.addView(change_view_switch);

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
