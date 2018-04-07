package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Inflates the card for a bid in the recycler view
 *
 * Created by aidankosik on 2018-03-03.
 *
 * @author  Aidan Kosik
 * @see Bid
 */

public class BidViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener clickListener;
    public TextView bidPrice;
    public TextView bidDescription;
    public TextView bidUsername;

    public BidViewHolder(View view, final Context context) {
        super(view);
        bidPrice = view.findViewById(R.id.bid_price);
        bidDescription = view.findViewById(R.id.bid_description);
        bidUsername = view.findViewById(R.id.bid_username);
        view.setOnClickListener(this);
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null) clickListener.onClick(view, getAdapterPosition(), 0);
    }

}
