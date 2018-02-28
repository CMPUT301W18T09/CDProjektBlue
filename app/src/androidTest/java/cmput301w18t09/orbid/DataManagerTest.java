package cmput301w18t09.orbid;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by david on 24/02/18.
 */
public class DataManagerTest  {

    //public DataManagerTest () { super(DataManager.class); }

    public void testAddObject() {
        //Same as testGetObject?
    }

    public void testGetObject() {
        ArrayList<Task> taskList = new ArrayList<>();
        DataManager.getObject getObjectTask = new DataManager.getObject();
        DataManager.addObject addObjectTask = new DataManager.addObject();

        assertTrue(taskList.isEmpty());

        User user = new User("myUsername", "myemail@ualberta.ca",
                "7805555555", "FirstName", "LastName");
        Task task = new Task(user, "Task Description", "Task Title",
                20.00f, Task.TaskStatus.REQUESTED);

        addObjectTask.execute(task);
        getObjectTask.execute("");
        try {
            taskList = getObjectTask.get();
        } catch (Exception e) {}

        assertFalse(taskList.isEmpty());
    }

    public void testUpdateObject() {
        ArrayList<Task> taskList = new ArrayList<>();
        DataManager.getObject getObjectTask = new DataManager.getObject();
        DataManager.addObject addObjectTask = new DataManager.addObject();
        DataManager.updateObject updateObjectTask = new DataManager.updateObject();


        User user = new User("myUsername", "myemail@ualberta.ca",
                "7805555555", "FirstName", "LastName");
        Task task = new Task(user, "Task Description", "Task Title1",
                20.00f, Task.TaskStatus.REQUESTED);

        //Create a task, add it to the server, check to ensure it's there
        addObjectTask.execute(task);
        getObjectTask.execute("Task Title1");
        try {
            taskList = getObjectTask.get();
        } catch (Exception e) {}

        assertFalse(taskList.isEmpty());

        //Change the title of the task and update the server, check if it's there
        taskList.clear();
        task.setTitle("Task Title2");
        updateObjectTask.execute(task);
        getObjectTask.execute("Task Title2");
        try {
            taskList = getObjectTask.get();
        } catch (Exception e) {}

        assertFalse(taskList.isEmpty());

    }

    public void testDoesUserExist() {
        String username = "testusername";
        DataManager.addObject addObjectTask = new DataManager.addObject();
        User user = new User("testusername", "myemail@ualberta.ca",
                "7805555555", "FirstName", "LastName");

        assertFalse(DataManager.doesUserExist(username));
//        Will change when implementation is solved
//        addObjectTask.execute(user);
        assertTrue(DataManager.doesUserExist(username));
    }


}