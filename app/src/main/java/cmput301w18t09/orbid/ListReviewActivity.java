package cmput301w18t09.orbid;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by david on 28/03/18.
 */

public class ListReviewActivity extends NavigationActivity implements ItemClickListener {

    private ArrayList<Review> reviewList = new ArrayList<>();
    private TextView userName;
    private ReviewListAdapter reviewListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(R.layout.activity_view_reviews, frameLayout);

        userName = findViewById(R.id.review_username);
        userName.setText(getIntent().getStringExtra("username").toUpperCase());

        Review testr = new Review(4.0f, "description", Review.reviewType.PROVIDER_REVIEW, "subuser");
        reviewList.add(testr);
        reviewList.add(testr);
        reviewListAdapter = new ReviewListAdapter(this, reviewList, 0);
        reviewListAdapter.setClickListener(this);
        recyclerView = findViewById(R.id.recyclerView_reviews);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reviewListAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(View view, int position, int type) {

    }
}
