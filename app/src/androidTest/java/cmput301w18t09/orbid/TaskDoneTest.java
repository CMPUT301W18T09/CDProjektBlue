package cmput301w18t09.orbid;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by Ceegan on 2018-03-17.
 *
 * This class tests the use case category Task Done. (UC 07.01.01, UC 07.02.01)
 */
public class TaskDoneTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public TaskDoneTest(){
        super(LoginActivity.class);
    }

    public void setUp(){
        solo = new Solo(getInstrumentation(), getActivity());
    }

    /**
     * This test shows that a task that has been assigned is set to status completed
     * once the task has been fulfilled. (UC 07.01.01)
     */
    public void testTaskCompleted(){
        // Create a test user
        User testRequester = new User("testCeeg", "test", "test@provider.com", "1234567890", "test", "requester");
        DataManager.addUsers addUsers = new DataManager.addUsers(solo.getCurrentActivity().getBaseContext());
        addUsers.execute(testRequester);

        // Create a test task
        Task task = new Task("testCeeg", "My dishwasher broke", "Fix My Dishwasher", 30.00, Task.TaskStatus.ASSIGNED);
        task.addBid(new Bid("Zach36", 10.0, "I can fix the leak"));
        task.acceptBid(0);
        DataManager.addTasks addTasks = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task);

        // Login
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText(0, "testCeeg");
        solo.enterText(1, "test");
        solo.clickOnText("Sign In");

        solo.waitForActivity(RecentListingsActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("My Listings");

        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        solo.waitForText("New Listings", 1, 3000);
        solo.clickOnImageButton(1);

        solo.waitForText("Bidded Listings", 1, 3000);
        solo.clickOnImageButton(1);

        solo.waitForText("Assigned Listings");
        solo.clickOnText("Fix My Dishwasher");

        solo.assertCurrentActivity("Wrong Activity", TaskDetailsActivity.class);
        solo.clickOnText("Fulfilled");

        solo.waitForText("Assigned Listings");
        solo.clickOnImageButton(1);

        solo.waitForText("Completed Listings");
        solo.waitForText("Fix My Dishwasher");

        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);

        solo.sleep(1000);

        // Delete test user
        DataManager.deleteUsers deleteUsers = new DataManager.deleteUsers(solo.getCurrentActivity().getBaseContext());
        ID.clear();
        ID.add(testRequester.getID());
        deleteUsers.execute(ID);
    }

    /**
     * This test reposts a task that was not completed. changing its status back to requested
     * from assigned. (UC 07.02.01)
     */
    public void testTaskReposted(){
        // Create a test user
        User testRequester = new User("testCeeg", "test", "test@provider.com", "1234567890", "test", "requester");
        DataManager.addUsers addUsers = new DataManager.addUsers(solo.getCurrentActivity().getBaseContext());
        addUsers.execute(testRequester);

        // Create a test task
        Task task = new Task("testCeeg", "Getting scratched hurts!", "Cat Declawing", 80.00, Task.TaskStatus.ASSIGNED);
        task.addBid(new Bid("Zach36", 11.0, "test bid"));
        task.acceptBid(0);
        DataManager.addTasks addTasks = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task);

        // Login
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText(0, "testCeeg");
        solo.enterText(1, "test");
        solo.clickOnText("Sign In");

        solo.waitForActivity(RecentListingsActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("My Listings");

        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        solo.waitForText("New Listings", 1, 3000);
        solo.clickOnImageButton(1);

        solo.waitForText("Bidded Listings", 1, 3000);
        solo.clickOnImageButton(1);

        solo.waitForText("Assigned Listings");
        solo.clickOnText("Cat Declawing");

        solo.assertCurrentActivity("Wrong Activity", TaskDetailsActivity.class);
        solo.clickOnText("Repost");

        solo.waitForText("Assigned Listings");
        solo.clickOnImageButton(2);

        solo.waitForText("Bidded Listings");
        solo.clickOnImageButton(2);

        solo.waitForText("New Listings");
        solo.waitForText("Cat Declawing");

        // Delete test task
        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);

        solo.sleep(1000);

        // Delete test user
        DataManager.deleteUsers deleteUsers = new DataManager.deleteUsers(solo.getCurrentActivity().getBaseContext());
        ID.clear();
        ID.add(testRequester.getID());
        deleteUsers.execute(ID);
    }
}
