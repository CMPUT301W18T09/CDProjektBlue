package cmput301w18t09.orbid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ToggleButton;


import java.util.ArrayList;

public class RecentListingsActivity extends NavigationActivity {

    private ArrayList<Task> taskList;
    private ListView listView;
    private TaskListAdapter taskListAdapter;
    private ToggleButton tbtnToggle;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Must call in this order, NOT super first
        super.onCreate(savedInstanceState);
        int layoutID = getIntent().getIntExtra("recent_listings_layout_id", 0);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(layoutID, frameLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Switch change_view_switch = new Switch(getBaseContext());
        change_view_switch.setTextOn("List");
        change_view_switch.setTextOff("Map");
        change_view_switch.setChecked(true);
//        change_view_switch.setOnCheckedChangeListener(new OnCheckedChangedListener(
//
//        );
        toolbar.addView(change_view_switch);


    }

    @Override
    public void onCheckChanged(CompoundButton buttonView, boolean isChecked) {

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
