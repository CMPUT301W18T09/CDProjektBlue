package cmput301w18t09.orbid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AddEditTaskActivity extends NavigationActivity {

    private Button btnSave;
    private EditText etDescription;
    private EditText etTitle;
    private EditText etLocation;
    private EditText etPrice;
    private ArrayList<Bid> bidList = new ArrayList<Bid>();
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        User testUser = new User("test", "test","test", "test", "test");
        Bid testBid = new Bid(testUser, 3.14, "test");
        Bid testBid2 = new Bid(testUser, 3.14, "test");
        Bid testBid3 = new Bid(testUser, 3.14, "test");
        bidList.add(testBid);
        bidList.add(testBid2);
        bidList.add(testBid3);
        ListView listView = (ListView) findViewById(R.id.BidList);
        BidListAdapter bidAdapter = new BidListAdapter(this, bidList);
        listView.setAdapter(bidAdapter);
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

    private void save()
    {

    }

    private void repostBid(View view) {

    }

    private void fulfillBid(View view) {

    }


}
