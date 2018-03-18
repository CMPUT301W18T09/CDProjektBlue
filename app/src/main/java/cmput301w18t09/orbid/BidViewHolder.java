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

    public TextView bid_price;
    public TextView bid_description;
    public TextView bid_username;
    private ItemClickListener clickListener;

    public BidViewHolder(View view, final Context context) {
        super(view);
        bid_price = view.findViewById(R.id.bid_price);
        bid_description = view.findViewById(R.id.bid_description);
        bid_username = view.findViewById(R.id.bid_username);
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
