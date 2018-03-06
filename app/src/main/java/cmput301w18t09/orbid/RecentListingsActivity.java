package cmput301w18t09.orbid;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ToggleButton;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class RecentListingsActivity extends NavigationActivity {

    private ArrayList<Task> taskList;
    private ListView listView;
    private TaskListAdapter taskListAdapter;
    private ToggleButton tbtnToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        int layoutID = getIntent().getIntExtra("recent_listings_layout_id", 0);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(layoutID, frameLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);

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
