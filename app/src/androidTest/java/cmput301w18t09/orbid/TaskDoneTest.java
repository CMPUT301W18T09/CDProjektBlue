package cmput301w18t09.orbid;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by Ceegan on 2018-03-17.
 */

public class TaskDoneTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public TaskDoneTest(){
        super(ListTaskActivity.class);
    }

    public void setUp(){
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testTaskCompleted(){
        Task task = new Task("ceeg", "test", "intent Test", 99.9, Task.TaskStatus.ASSIGNED);
        task.addBid(new Bid("test", 11.0, "test bid"));
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
        solo.clickOnText("intent Test");

        solo.clickOnText("Fulfilled");

        solo.waitForText("Assigned Tasks", 1, 3000);
        solo.clickOnImageButton(1);

        solo.waitForText("Completed Tasks");
        assertTrue(solo.waitForText("intent Test"));

        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);
    }

    public void testTaskReposted(){
        Task task = new Task("ceeg", "test", "intent Test", 99.9, Task.TaskStatus.ASSIGNED);
        task.addBid(new Bid("test", 11.0, "test bid"));
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
        solo.clickOnText("intent Test");

        solo.clickOnText("Repost");

        solo.waitForText("Assigned Tasks", 1, 3000);
        solo.clickOnImageButton(2);

        solo.waitForText("Bidded Tasks", 1, 3000);
        solo.clickOnImageButton(2);

        assertTrue(solo.waitForText("intent Test"));
        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);
    }
}
