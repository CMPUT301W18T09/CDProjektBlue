package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by aidankosik on 2018-03-03.
 */

public class BidViewHolder extends RecyclerView.ViewHolder {

    public TextView bid_price;
    public TextView bid_description;

    public BidViewHolder(View view, final Context context) {
        super(view);
        bid_price = view.findViewById(R.id.task_description);
        bid_description = view.findViewById(R.id.task_title);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Timber", "NICE CLICK BRO");
            }
        });
    }
}
