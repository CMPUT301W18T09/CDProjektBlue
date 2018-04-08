package cmput301w18t09.orbid;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.RatingBar;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * This test covers the "Wow" factor use cases (UC 11.01.01, UC 11.02.01). In particular, we show
 * a task requester making a review on a task provider only after setting the task they were
 * involved in to status: complete. After the review is add, we view the list of reviews for the
 * task provider.
 *
 * @author Zach Redfern
 */
public class ReviewTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public ReviewTest() {
        super(cmput301w18t09.orbid.LoginActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    // Credit: David Laycock
    // Method to return the server ID of a User based on the username
    public ArrayList<String> findUserID(String username) {
        DataManager.getUsers getUser = new DataManager.getUsers(getActivity());
        ArrayList<String> queryList = new ArrayList<>();
        ArrayList<User> usersList = new ArrayList<>();

        queryList.add("username");
        queryList.add(username);
        getUser.execute(queryList);

        try {
            usersList = getUser.get();
        } catch (Exception e) {}

        if (!usersList.isEmpty()) {
            String stringQuery = usersList.get(0).getID();
            queryList.clear();
            queryList.add(stringQuery);
        }

        return queryList;
    }

    // Returns the server ID of a Task based on the task title
    public ArrayList<String> findTaskID(String title) {
        DataManager.getTasks getTask = new DataManager.getTasks(getActivity());
        ArrayList<String> queryList = new ArrayList<>();
        ArrayList<Task> taskList = new ArrayList<>();

        // Fetch the task from the server with a given title
        queryList.clear();
        queryList.add("or");
        queryList.add("title");
        queryList.add(title);

        try {
            getTask.execute(queryList);
            taskList = getTask.get();
        }
        catch (Exception e) {
            Log.e("Task ID Error", "Error getting Task ID from server");
            e.printStackTrace();;
            assertTrue(Boolean.FALSE);
        }

        // If we got a task back, return it's ID to the caller, else report the error
        if (!taskList.isEmpty()) {
            queryList.clear();
            queryList.add(taskList.get(0).getID());
        }

        return queryList;
    }

    /**
     * Tests the ability to add a review and then see the added review after.
     */
    public void testAddReview() {

        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        ArrayList<String> queryList = new ArrayList<>();

        // Delete the test reviewer if their account already exists
        DataManager.deleteUsers delReviewer = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("testReviewer");
        if (!queryList.get(0).toString().equals("username")) {
            Log.e("found", queryList.get(0).toString());
            delReviewer.execute(queryList);
        }

        solo.sleep(1500);

        // Delete the test reviewee if their account already exists
        DataManager.deleteUsers delReviewee = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("testReviewee");
        if (!queryList.get(0).toString().equals("username")) {
            Log.e("found", queryList.get(0).toString());
            delReviewee.execute(queryList);
        }

        solo.sleep(1500);

        // Delete test task 1 if it already exists
        DataManager.deleteTasks delTask1 = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("Cat Declawing");
        if (!queryList.get(0).toString().equals("or")) {
            delTask1.execute(queryList);
        }

        solo.sleep(1500);


        // Create an account for the test reviewer
        // NOTE: Creating accounts is covered in another use case
        DataManager.addUsers addReviewer = new DataManager.addUsers(context);
        User reviewer = new User("testReviewer", "test", "zred@hotmail.com", "7809396963", "Zach", "Redfern");
        addReviewer.execute(reviewer);

        // Create an account for the test reviewee
        // NOTE: Creating accounts is covered in another use case
        DataManager.addUsers addReviewee = new DataManager.addUsers(context);
        User reviewee = new User("testReviewee", "test","bpanda@hotmail.com", "5875551234", "Bobbi", "Pandachuck");
        addReviewee.execute(reviewee);


        // Add the task that is being handled between the reviewer and reviewee
        Task task = new Task("testReviewer", "Getting scratched hurts!", "Cat Declawing", 80.00, Task.TaskStatus.ASSIGNED);
        task.addBid(new Bid("testReviewee", 35.0, "I have arms of steel and can handle cats."));
        task.acceptBid(0);
        DataManager.addTasks addTasks = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task);

        // Login as the test reviewer
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "testReviewer");
        solo.enterText((EditText) solo.getView(R.id.login_etPassword), "test");
        solo.sleep(2000);
        solo.clickOnButton("Sign In");

        // Start the list task activity
        solo.waitForActivity(RecentListingsActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("My Listings");

        // Go to assignments page
        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        solo.waitForText("New Listings", 1, 3000);
        solo.clickOnImageButton(1);

        solo.sleep(1000);
        solo.waitForText("Bidded Listings", 1, 3000);
        solo.clickOnImageButton(1);

        solo.sleep(1000);
        solo.waitForText("Assigned Listings", 1, 3000);
        solo.clickOnText("Cat Declawing");

        // Say the task was fulfilled
        solo.assertCurrentActivity("Wrong Activity", TaskDetailsActivity.class);
        solo.clickOnText("Fulfilled");
        solo.sleep(1000);

        // Go to the completed requests page
        solo.waitForText("Assigned Listings", 1, 3000);
        solo.clickOnImageButton(1);

        solo.waitForText("Completed Listings", 1, 3000);
        solo.waitForText("Cat Declawing", 1, 3000);

        solo.clickOnText("Cat Declawing");
        solo.clickOnText("Add Review");


        // Add a review
        RatingBar ratingBar = (RatingBar) solo.getView(R.id.ratingBar2);
        ratingBar.setRating(5.0f);
        solo.enterText((EditText) solo.getView(R.id.review), "A skilled veterinarian who got the job done quick.");
        solo.sleep(3000);

        solo.clickOnButton("Add");
        solo.sleep(1000);

        // See the review for that individual
        solo.waitForText("testReviewee",1, 3000);
        solo.clickOnText("testReviewee");

        solo.sleep(1500);

        RatingBar ratingBar1 = (RatingBar) solo.getView(R.id.ratingBar);
        solo.clickOnView(ratingBar1);

        solo.sleep(3000);


        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);

        solo.sleep(1000);

        // Delete test user
        DataManager.deleteUsers deleteUsers = new DataManager.deleteUsers(solo.getCurrentActivity().getBaseContext());
        ID.clear();
        ID.add(reviewer.getID());
        ID.add(reviewee.getID());
        deleteUsers.execute(ID);

    }
}