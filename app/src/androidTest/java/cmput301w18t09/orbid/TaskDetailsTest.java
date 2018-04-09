package cmput301w18t09.orbid;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by micag on 2018-03-15.
 *
 * This test covers all of the user cases for Task Details (UC 02.01.01)
 */

public class TaskDetailsTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public TaskDetailsTest() {
        super(LoginActivity.class);
    }

    public void setUp() {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testTaskDetails() throws Exception {

        // Adding test user
        User testUser = new User("mica", "test", "mica@test.com", "1234567890", "Mica", "g");
        DataManager.addUsers addUsers = new DataManager.addUsers(solo.getCurrentActivity().getBaseContext());
        addUsers.execute(testUser);

        // Create a new task
        Task task = new Task("notMica", "Need someone to look after my dog.", "Looking for dog sitter", 99.99, Task.TaskStatus.REQUESTED);
        DataManager.addTasks addTasks = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task);

        // Login
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText(0, "mica");
        solo.enterText(1, "test");
        solo.clickOnText("Sign In");

        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);

        //Clicking on task to see the details
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", TaskDetailsActivity.class);
        assertTrue(solo.waitForText("Status"));

        //Deleting the task to keep the server clean from testing
        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);

        solo.sleep(1000);
        // Delete test user
        DataManager.deleteUsers deleteUsers = new DataManager.deleteUsers(solo.getCurrentActivity().getBaseContext());
        ID.clear();
        ID.add(testUser.getID());
        deleteUsers.execute(ID);
    }
    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

