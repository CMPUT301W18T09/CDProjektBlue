package cmput301w18t09.orbid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

// AKA myRequests
public class TaskBidListActivity extends TaskDetailsActivity {

    private ArrayList<Bid> bidList;
    private ListView listView;
    private BidListAdapter bidListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void openAcceptBidDialog()
    {

    }
}
