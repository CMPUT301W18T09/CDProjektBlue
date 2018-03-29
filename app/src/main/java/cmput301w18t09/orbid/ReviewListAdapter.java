package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private Context context;
    private ArrayList<Review> reviewList;
    private ItemClickListener clickListener;
    private int type;


    public ReviewListAdapter(final Context context, ArrayList<Review> reviewList, int type) {
        this.context = context;
        this.reviewList = reviewList;
        this.type = type;
    }


    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.layout_review_card, parent, false);
        return new ReviewViewHolder(view, this.context, type);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.setClickListener(clickListener);
        Review review = reviewList.get(position);
        holder.reviewType.setText(review.getType().toString());
        holder.reviewDescription.setText(review.getDescription());
        holder.reviewUsername.setText(review.getSubmittingUser());
        holder.reviewRating.setRating(review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public void setReviewList(ArrayList<Review> reviewList) {this.reviewList = reviewList;}
}
