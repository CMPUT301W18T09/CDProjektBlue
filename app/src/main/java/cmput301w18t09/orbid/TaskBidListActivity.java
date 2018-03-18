package cmput301w18t09.orbid;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Activity that shows a list of bids made on the current users tasks. Allows bids to be
 * accepted/ assigned
 *
 * @author Chady Haidar
 */
public class TaskBidListActivity extends TaskDetailsActivity {

    private ArrayList<Bid> bidList;
    private ListView listView;
    private BidListAdapter bidListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
