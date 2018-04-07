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
        super(NavigationActivity.class);
    }

    public void setUp() {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testTaskDetails() throws Exception {

        //Creating a new task by a different user than the one we are going to use
        Task task = new Task("Mica", "testing UC 02.01.01", "Task details test", 50.00, Task.TaskStatus.REQUESTED);
        DataManager.addTasks addTasks = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task);

        //This is the user we are going to use
        NavigationActivity.thisUser = "NotMica";

        //Going from navigation to Recent Listings page
        solo.clickOnImageButton(0);
        solo.clickOnText("Recent Listings");
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);

        //Clicking on task to see the details
        assertTrue(solo.waitForText("Task details test", 1, 3000));
        //solo.clickOnText("Task details test");
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", TaskDetailsActivity.class);

        assertTrue(solo.waitForText("Task details test"));

        //Deleting the task to keep the server clean from testing
        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);
    }
    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

