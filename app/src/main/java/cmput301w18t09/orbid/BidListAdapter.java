package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Binds the bid view holder with the recycler view bid list
 *
 * @author Aidan Kosik
 * @see Bid
 */
public class BidListAdapter extends RecyclerView.Adapter<BidViewHolder> {

    private Context context;
    private ItemClickListener clickListener;
    private ArrayList<Bid> bidList;

    public BidListAdapter(final Context context, ArrayList<Bid> bidList) {
        this.context = context;
        this.bidList = bidList;
    }

    @Override
    public BidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.layout_bid_card, parent, false);
        return new BidViewHolder(view, this.context);
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(BidViewHolder holder, int position) {
        Bid bid = bidList.get(position);
        holder.setClickListener(clickListener);
        holder.bidDescription.setText(bid.getDescription());
        holder.bidPrice.setText("$" + Double.toString(bid.getPrice()));
        holder.bidUsername.setText(bid.getProvider());
    }

    @Override
    public int getItemCount() {
        return bidList.size();
    }
}
