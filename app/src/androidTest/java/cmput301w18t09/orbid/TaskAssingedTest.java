package cmput301w18t09.orbid;


import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;


public class TaskAssingedTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public TaskAssingedTest(){
        super(NavigationActivity.class);
    }

    public void setUp(){
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testDisplayAssignedTasks(){
        solo.clickOnImageButton(0);
        solo.clickOnText("My Requests");

        Task task = new Task("ceeg", "test", "intent Test", 99.9, Task.TaskStatus.ASSIGNED);
        DataManager.addTasks addTasks = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task);

        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);

        NavigationActivity.thisUser = "ceeg";
        solo.clickOnImageButton(2);

        assertTrue(solo.waitForText("My Bidded Tasks"));

        solo.clickOnImageButton(1);

        assertTrue(solo.waitForText("My Assigned Tasks", 1, 3000));
        assertTrue(solo.waitForText("intent Test", 1, 3000));

        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);
    }

    public void testDisplayAssingedBids(){
        solo.clickOnImageButton(0);
        solo.clickOnText("My Bids");

        Task task = new Task("test", "test", "intent Test", 99.9, Task.TaskStatus.ASSIGNED);
        task.addBid(new Bid("ceeg", 11, "test"));
        DataManager.addTasks addTasks = new DataManager.addTasks(solo.getCurrentActivity().getBaseContext());
        addTasks.execute(task);

        NavigationActivity.thisUser = "ceeg";

        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);

        solo.waitForText("My Open Bids", 1, 3000);

        solo.clickOnImageButton(1);

        solo.waitForText("My Assigned Bids", 1, 3000);
        assertTrue(solo.waitForText("intent Test", 1, 3000));

        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext());
        ArrayList<String> ID = new ArrayList<>();
        ID.add(task.getID());
        deleteTasks.execute(ID);
    }
}
