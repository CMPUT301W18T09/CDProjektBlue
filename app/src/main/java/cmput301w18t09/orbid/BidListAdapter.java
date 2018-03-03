package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class BidListAdapter extends RecyclerView.Adapter<BidViewHolder> {

    private Context context;
    private ArrayList<Bid> bidList;

    public BidListAdapter(final Context context, ArrayList<Bid> bidList) {
        this.context = context;
        this.bidList = bidList;
    }

    @Override
    public BidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.layout_recent_listings_view, parent, false);
        return new BidViewHolder(view, this.context);
    }


    @Override
    public void onBindViewHolder(BidViewHolder holder, int position) {
        Bid bid = bidList.get(position);

        // Todo fill out bid information here

    }


    @Override
    public int getItemCount() {
        return bidList.size();
    }

}
