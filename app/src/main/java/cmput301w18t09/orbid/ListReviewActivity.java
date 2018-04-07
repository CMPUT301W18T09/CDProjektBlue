package cmput301w18t09.orbid;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Allows a user to see the list of reviews and ratings for any other user that has reviews.
 *
 * @author David Laycock
 * @see Review
 */
public class ListReviewActivity extends NavigationActivity implements ItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Review> reviewList = new ArrayList<>();
        TextView tvUsername;
        String username;
        ReviewListAdapter reviewListAdapter;
        RecyclerView recyclerView;

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(R.layout.activity_view_reviews, frameLayout);

        tvUsername = findViewById(R.id.review_username);
        username = getIntent().getStringExtra("username");
        tvUsername.setText(username.toUpperCase());

        // Get the user we are adding a review to
        DataManager.getUsers getUsers = new DataManager.getUsers(this);
        ArrayList<String> queryParameters = new ArrayList<>();
        ArrayList<User> returnUsers;

        queryParameters.add("username");
        queryParameters.add(username);
        getUsers.execute(queryParameters);
        try {
            returnUsers = getUsers.get();
        }
        catch (Exception e) {
            Log.e("Error", "Failed to get ArrayList intended as return from getUsers");
            e.printStackTrace();
            Toast.makeText(this, "Error getting the user to be reviewed", Toast.LENGTH_LONG).show();
            return;
        }

        // Populate the recycler view 
        User user = returnUsers.get(0);
        reviewList = user.getReviewList();
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
