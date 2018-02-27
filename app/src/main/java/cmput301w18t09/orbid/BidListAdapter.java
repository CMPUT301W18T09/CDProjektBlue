package cmput301w18t09.orbid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class BidListAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Bid> bidList;

    public BidListAdapter(Context context, ArrayList<Bid> bidList)
    {
        super(context, 0, bidList);
        this.context = context;
        this.bidList = bidList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View bidItem = convertView;

        // An short example of inflating a bid item in the list

        if(bidItem == null) {
            bidItem = LayoutInflater.from(context).inflate(R.layout.bid_listview_layout, parent, false);
         }

        Bid bid = bidList.get(position);
        TextView bidPrice = (TextView) bidItem.findViewById(R.id.BidPrice);
        TextView bidComment = (TextView) bidItem.findViewById(R.id.BidComment);

        bidPrice.setText(Double.toString(bid.getPrice()));
        bidComment.setText(bid.getDescription());
        return bidItem;
    }
}
