package cmput301w18t09.orbid;

import android.content.Context;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Allows a user to provide a review to some other user only after they have completed a task
 * interaction with one another.
 *
 * @author Zach Redfern
 * @see Review
 */
public class AddReviewActivity extends NavigationActivity {

    private Review.reviewType type;
    private String reviewedBy;
    private String reviewee;

    /**
     *
     * @param savedInstanceState Any information that needs to be passed to this activity instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Inflate the layout ID that was received
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(R.layout.activity_add_review, frameLayout);

        if (getIntent().getStringExtra("reviewType").equals("Provider")) {
            type = Review.reviewType.PROVIDER_REVIEW;
        }
        else if (getIntent().getStringExtra("reviewType").equals("Requester")) {
            type = Review.reviewType.REQUESTOR_REVIEW;
        }

        reviewedBy = LoginActivity.getCurrentUsername();
        reviewee = getIntent().getStringExtra("reviewee");

        TextView tvReviewee = findViewById(R.id.reviewee_name);
        tvReviewee.setText(reviewee);

        RatingBar ratingBar = findViewById(R.id.ratingBar2);
        ratingBar.setRating((float) 3.0);

        // Assign on click listener to the sign in button
        Button btnAddReview = findViewById(R.id.submit_button);
        btnAddReview.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                addReview();
            }
        });
    }

    /**
     * Takes the information input from the user interface and converts it to a review object to be
     * added to a user, only after validating the input.
     */
    private void addReview() {

        if (!DataManager.isNetworkAvailable(this)) {
            Toast.makeText(this, "Can't add reviews offline", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText etReview = findViewById(R.id.review);
        RatingBar ratingBar = findViewById(R.id.ratingBar2);

        // Check description length
        if (etReview.getText().toString().length() == 0) {
            Toast.makeText(this, "Must provide a description", Toast.LENGTH_LONG).show();
            return;
        }
        else if (etReview.getText().toString().length() > 300) {
            Toast.makeText(this, "Review must be 300 characters or less", Toast.LENGTH_LONG).show();
            return;
        }

        // Ensure we are still online
        Review review = new Review(ratingBar.getRating(), etReview.getText().toString(), type, reviewedBy);
        if (!DataManager.isNetworkAvailable(this, reviewedBy)) {
            Toast.makeText(this, "Can't add review when offline", Toast.LENGTH_LONG).show();
            return;
        }

        // Get the user we are adding a review to
        DataManager.getUsers getUsers = new DataManager.getUsers(this);
        ArrayList<String> queryParameters = new ArrayList<>();
        ArrayList<User> returnUsers;

        queryParameters.add("username");
        queryParameters.add(reviewee);
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

        User user = returnUsers.get(0);
        user.addReview(review);

        DataManager.updateUsers updateUsers = new DataManager.updateUsers(this);
        ArrayList<User> params = new ArrayList<>();
        params.add(user);

        updateUsers.execute(params);
        updateTask();

        Toast.makeText(this, "Review added", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Changes a the task that was completed between two users to reflect that a review of a
     * specific type was added. E.g., only one review for a provider may be added per each completed
     * task.
     */
    private void updateTask() {

        // Get the task being reviewed
        DataManager.getTasks getTasks = new DataManager.getTasks(this);
        ArrayList<String> query = new ArrayList<>();
        ArrayList<Task> taskList = new ArrayList<>();

        query.add("and");
        query.add("_id");
        query.add(getIntent().getStringExtra("taskID"));
        getTasks.execute(query);
        try {
            taskList = getTasks.get();
        } catch (Exception e) {
            // TODO: HANLDE
        }

        // Make sure only one review type can exist per task
        Task task = taskList.get(0);
        if (type == Review.reviewType.PROVIDER_REVIEW) {
            task.setIsReviewedByRequester(true);
        }
        else if (type == Review.reviewType.REQUESTOR_REVIEW) {
            task.setIsReviewedByProvider(true);
        }

        // Update the task that was being reviewed
        DataManager.updateTasks updateTasks = new DataManager.updateTasks(this);
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task);
        updateTasks.execute(tasks);
    }
}
