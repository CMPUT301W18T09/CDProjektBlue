package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by aidankosik on 2018-03-03.
 */

public class BidViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView bid_price;
    public TextView bid_description;
    private ItemClickListener clickListener;

    public BidViewHolder(View view, final Context context) {
        super(view);
        bid_price = view.findViewById(R.id.bid_price);
        bid_description = view.findViewById(R.id.bid_description);
        view.setOnClickListener(this);
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
    @Override
    public void onClick(View view) {
        if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
    }

}
