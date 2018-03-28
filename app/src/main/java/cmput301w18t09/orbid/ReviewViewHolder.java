package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView reviewType;
    public TextView reviewDescription;
    public TextView reviewUsername;
    public RatingBar reviewRating;
    private ItemClickListener clickListener;
    private int type;

    public ReviewViewHolder(View view, final Context context, int type) {
        super(view);
        reviewType = view.findViewById(R.id.review_type);
        reviewDescription = view.findViewById(R.id.description);
        reviewUsername = view.findViewById(R.id.review_username);
        reviewRating = view.findViewById(R.id.ratingBar);
        view.setOnClickListener(this);
        this.type = type;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View view) {

    }
}
