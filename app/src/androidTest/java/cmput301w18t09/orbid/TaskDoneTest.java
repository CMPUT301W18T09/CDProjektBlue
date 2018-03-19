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
        super(ListTaskActivity.class);
    }

    public void setUp(){
        solo = new Solo(getInstrumentation(), getActivity());
    }

    /**
     * This test shows that a task that has been assigned is set to status completed
     * once the task has been fulfilled. (UC 07.01.01)
     */
    public void testTaskCompleted(){
        Task task = new Task("ceeg", "My dishwasher broke", "Fix My Dishwasher", 30.00, Task.TaskStatus.REQUESTED);
        task.addBid(new Bid("Zach36", 10.0, "I can fix the leak"));
        task.acceptBid(0);
        DataManager.addTasks addTasks = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task);
        NavigationActivity.thisUser = "ceeg";

        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        solo.waitForText("My Requested", 1, 3000);
        solo.clickOnImageButton(1);

        solo.waitForText("My Bidded Tasks");
        solo.clickOnImageButton(1);

        solo.waitForText("Assigned Tasks", 1, 3000);
        solo.clickOnText("Fix My Dishwasher");
        solo.sleep(2000);

        solo.clickOnText("Fulfilled");
        solo.waitForText("Assigned Tasks", 1, 3000);
        solo.clickOnImageButton(1);

        solo.waitForText("Completed Tasks");
        assertTrue(solo.waitForText("Fix My Dishwasher"));
        solo.sleep(2000);

        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);
    }

    /**
     * This test reposts a task that was not completed. changing its status back to requested
     * from assigned. (UC 07.02.01)
     */
    public void testTaskReposted(){
        Task task = new Task("ceeg", "Getting scratched hurts!", "Cat Declawing", 80.00, Task.TaskStatus.ASSIGNED);
        task.addBid(new Bid("Zach36", 11.0, "test bid"));
        task.acceptBid(0);
        DataManager.addTasks addTasks = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task);
        NavigationActivity.thisUser = "ceeg";

        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        solo.waitForText("My Requested", 1, 3000);
        solo.clickOnImageButton(1);

        solo.waitForText("My Bidded Tasks", 1, 3000);
        solo.clickOnImageButton(1);

        solo.waitForText("My Assigned Tasks", 1, 3000);
        solo.clickOnText("Cat Declawing");
        solo.sleep(2000);

        solo.clickOnText("Repost");

        solo.waitForText("My Assigned Tasks", 1, 3000);
        solo.clickOnImageButton(2);

        solo.waitForText("My Bidded Tasks", 1, 3000);
        solo.clickOnImageButton(2);
        assertTrue(solo.waitForText("Cat Declawing"));
        solo.sleep(2000);

        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);
    }
}
