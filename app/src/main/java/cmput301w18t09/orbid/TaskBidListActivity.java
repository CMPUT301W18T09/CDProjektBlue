package cmput301w18t09.orbid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class TaskBidListActivity extends TaskDetailsActivity {

    private ArrayList<Bid> bidList;
    private ListView listView;
    private BidListAdapter bidListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* an example of setting up list view
        Bid testBid = new Bid(testUser, 3.14, "test");
        bidList.add(testBid);
        ListView listView = (ListView) findViewById(R.id.BidList);
        BidListAdapter bidAdapter = new BidListAdapter(this, bidList);
        listView.setAdapter(bidAdapter);*/
    }

    private void openAcceptBidDialog()
    {

    }
}
