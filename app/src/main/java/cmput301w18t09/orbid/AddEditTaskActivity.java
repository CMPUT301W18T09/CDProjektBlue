package cmput301w18t09.orbid;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
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

    private void save()
    {

    }

    private void repostBid(View view) {

    }

    private void fulfillBid(View view) {

    }


}
