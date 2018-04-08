package cmput301w18t09.orbid;


import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * This class tests the Task Assigned use cases. (UC 06.01.01, UC 06.02.01)
 */
public class TaskAssignedTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public TaskAssignedTest(){
        super(LoginActivity.class);
    }

    public void setUp(){
        solo = new Solo(getInstrumentation(), getActivity());
    }

    /**
     * This test shows that a task that has a status of assigned is shown to the
     * task requester correctly with its status, details, and accepted bid (UC 06.02.01).
     */
    public void testDisplayAssignedTasks(){
        // Create a test user
        User testProvider = new User("zach36", "test", "test@provider.com", "1234567890", "test", "provider");
        DataManager.addUsers addUsers = new DataManager.addUsers(solo.getCurrentActivity().getBaseContext());
        addUsers.execute(testProvider);

        // Create a new task and accept a bid on it
        Task task = new Task("ceeg", "Wash my car.", "Wash My Car", 99.99, Task.TaskStatus.REQUESTED);
        Bid bid = new Bid("Zach36", 90.00, "I have all the right tools.");
        task.addBid(bid);
        task.acceptBid(0);
        DataManager.addTasks addTasks = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task);

        // Login
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText(0, "zach36");
        solo.enterText(1, "test");
        solo.clickOnText("Sign In");

        solo.waitForActivity(RecentListingsActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("My Bids");

        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        assertTrue(solo.waitForText("Active Bids"));

        solo.clickOnImageButton(1);
        assertTrue(solo.waitForText("My Assignments", 1, 3000));

        // Get the position of the test task in the task list
        ListTaskActivity taskListActivity = (ListTaskActivity) solo.getCurrentActivity();
        ArrayList<Task> taskList = taskListActivity.getTaskList();
        int pos = -1;
        for (int i = 0; i < taskList.size(); ++i) {
            if (taskList.get(i).getTitle().equals("Wash My Car")) {
                pos = i;
            }
        }

        // If we couldn't find the task, fail the test
        if (pos == -1) {
            assertTrue(Boolean.FALSE);
        }

        // Click the test task in the recycler view to view the bids made on the test
        solo.sleep(2000);
        solo.scrollDownRecyclerView(pos);
        solo.clickInRecyclerView(pos);
        solo.sleep(3000);

        assertTrue(solo.waitForText("Wash My Car", 1, 3000));

        // Delete test task
        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);

        solo.sleep(1000);
        // Delete test user
        DataManager.deleteUsers deleteUsers = new DataManager.deleteUsers(solo.getCurrentActivity().getBaseContext());
        ID.clear();
        ID.add(testProvider.getID());
        deleteUsers.execute(ID);
    }

    /**
     * This test shows that a task assigned to a provider is correctly viewed with its status,
     * details, and the providers bid that was accepted.
     */
    public void testDisplayAssingedBids(){
        // Create a test user
        User testRequester = new User("testCeeg", "test", "test@provider.com", "1234567890", "test", "requester");
        DataManager.addUsers addUsers = new DataManager.addUsers(solo.getCurrentActivity().getBaseContext());
        addUsers.execute(testRequester);

        // Create a new task and accept a bid on it
        Task task = new Task("testCeeg", "Clean my carpet.", "Clean My Carpet", 99.99, Task.TaskStatus.REQUESTED);
        Bid bid = new Bid("Zach36", 90.00, "I have all the right tools.");
        task.addBid(bid);
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

        // Get the position of the test task in the task list
        ListTaskActivity taskListActivity = (ListTaskActivity) solo.getCurrentActivity();
        ArrayList<Task> taskList = taskListActivity.getTaskList();
        int pos = -1;
        for (int i = 0; i < taskList.size(); ++i) {
            if (taskList.get(i).getTitle().equals("Clean My Carpet")) {
                pos = i;
            }
        }

        // If we couldn't find the task, fail the test
        if (pos == -1) {
            assertTrue(Boolean.FALSE);
        }

        // Click the test task in the recycler view to view the bid made on the test
        solo.sleep(2000);
        solo.scrollDownRecyclerView(pos);
        solo.clickInRecyclerView(pos);
        solo.sleep(3000);

        assertTrue(solo.waitForText("Clean My Carpet", 1, 3000));

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
