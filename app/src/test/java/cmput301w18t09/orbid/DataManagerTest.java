package cmput301w18t09.orbid;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by david on 24/02/18.
 */
public class DataManagerTest extends ActivityInstrumentationTestCase2 {

    public DataManagerTest () { super(DataManager.class); }

    public void testAddObject() {

    }

    public void testGetObject() {
        DataManager.getObject getObjectTask = new DataManager.getObject();
        DataManager.addObject addObjectTask = new DataManager.addObject();
        ArrayList<Task> taskList = new ArrayList<>();

        assertTrue(taskList.isEmpty());

        User user = new User("myUsername", "myemail@ualberta.ca",
                "7805555555", "FirstName", "LastName");
        Task task = new Task(user, "Task Description", "Task Title",
                20.00f, 1);

        addObjectTask.execute(task);
        getObjectTask.execute("");
        try {
            taskList = getObjectTask.get();
        } catch (Exception e) {}

        assertFalse(taskList.isEmpty());
    }
}