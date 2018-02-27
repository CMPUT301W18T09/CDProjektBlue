package cmput301w18t09.orbid;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

        setContentView(R.layout.activity_add_edit_task);
        super.onCreate(savedInstanceState);
        User testUser = new User("test", "test","test", "test", "test");
        Bid testBid = new Bid(testUser, 3.14, "test");
        bidList.add(testBid);
        ListView listView = (ListView) findViewById(R.id.BidList);
        BidAdapter bidAdapter = new BidAdapter();
        listView.setAdapter(bidAdapter);
    }

    private void save()
    {

    }

    private void repostBid(View view) {

    }

    private void fulfillBid(View view) {

    }

    class BidAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return bidList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.bid_listview_layout, null);
            TextView bidPrice = (TextView)view.findViewById(R.id.BidPrice);
            TextView bidComment = (TextView)view.findViewById(R.id.BidComment);
            bidPrice.setText("Price:" + Double.toString(bidList.get(i).getPrice()));
            bidComment.setText(bidList.get(i).getDescription());

            return view;
        }
    }

}
