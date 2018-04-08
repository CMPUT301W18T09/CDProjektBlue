package cmput301w18t09.orbid;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by zachredfern on 2018-04-07.
 */

public class ReviewAddTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public ReviewAddTest() {
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

    public void testAddReview() {

        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        ArrayList<String> queryList = new ArrayList<>();

        // Delete the test requester if their account already exists
        DataManager.deleteUsers delReviewer = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("testReviewer");
        if (!queryList.get(0).toString().equals("username")) {
            Log.e("found", queryList.get(0).toString());
            delReviewer.execute(queryList);
        }

        solo.sleep(1000);

        // Delete the first test provider if their account already exists
        DataManager.deleteUsers delReviewee = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("testReviewee");
        if (!queryList.get(0).toString().equals("username")) {
            Log.e("found", queryList.get(0).toString());
            delReviewee.execute(queryList);
        }

        solo.sleep(1000);



        // Create an account for the test requester
        // NOTE: Creating accounts is covered in another use case
        DataManager.addUsers addReviewer = new DataManager.addUsers(context);
        User reviewer = new User("testReviewer", "test", "zred@hotmail.com", "7809396963", "Zach", "Redfern");
        addReviewer.execute(reviewer);

        // Create an account for the first test provider
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

        solo.waitForText("Bidded Listings", 1, 3000);
        solo.clickOnImageButton(1);

        solo.waitForText("Assigned Listings");
        solo.clickOnText("Cat Declawing");

        // Say the task was fulfilled
        solo.assertCurrentActivity("Wrong Activity", TaskDetailsActivity.class);
        solo.clickOnText("Fulfilled");

        // Go to the completed requests page
        solo.waitForText("Assigned Listings");
        solo.clickOnImageButton(1);

        solo.waitForText("Completed Listings");
        solo.waitForText("Cat Declawing");

        solo.clickOnText("Cat Declawing");

        solo.clickOnText("Add Review");





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