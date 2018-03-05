package cmput301w18t09.orbid;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by david on 24/02/18.
 */
public class DataManagerTest extends ActivityInstrumentationTestCase2
{

    public DataManagerTest () { super(DataManager.class); }

//    public void testAddObject() {
//        //Same as testGetObject?
//    }

    public void testGetTask() {
        ArrayList<Task> taskList = new ArrayList<>();
        DataManager.getTasks getTasksDM = new DataManager.getTasks();
        DataManager.addTasks addTasksDM = new DataManager.addTasks();

        assertTrue(taskList.isEmpty());

        User user = new User("myUsername", "myemail@ualberta.ca",
                "7805555555", "FirstName", "LastName");
        Task task = new Task(user, "Task Description", "Task Title",
                20.00f, 1);

        addTasksDM.execute(task);
        ArrayList<String> queryList = new ArrayList<>();
        queryList.add("_id");
        queryList.add("AWHudg4S6isPA6QkE2B0");
        getTasksDM.execute(queryList);
        try {
            taskList = getTasksDM.get();
        } catch (Exception e) {}
        assertFalse(taskList.isEmpty());
    }

    public void testDeleteTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        DataManager.deleteTasks delTasksDM = new DataManager.deleteTasks();
        DataManager.getTasks getTasksDM = new DataManager.getTasks();
        DataManager.addTasks addTasksDM = new DataManager.addTasks();
        ArrayList<String> queryList = new ArrayList<>();

        queryList.add("title");
        queryList.add("testDeleteTasks");
        getTasksDM.execute(queryList);
        try {
            taskList = getTasksDM.get();
        } catch (Exception e) {}
        assertTrue(taskList.isEmpty());

        User user = new User("myUsername", "myemail@ualberta.ca",
                "7805555555", "FirstName", "LastName");
        Task task = new Task(user, "Task Description", "testDeleteTasks",
                20.00f, 1);
        addTasksDM.execute(task);

        getTasksDM.execute(queryList);
        try {
            taskList = getTasksDM.get();
        } catch (Exception e) {}
        assertFalse(taskList.isEmpty());

        queryList.clear();
        queryList.add(taskList.get(0).getID());
        delTasksDM.execute(queryList);
//        assertTrue(taskList.isEmpty());
    }

//    public void testUpdateObject() {
//        ArrayList<Task> taskList = new ArrayList<>();
//        DataManager.getObject getObjectTask = new DataManager.getObject();
//        DataManager.addObject addObjectTask = new DataManager.addObject();
//        DataManager.updateObject updateObjectTask = new DataManager.updateObject();
//
//
//        User user = new User("myUsername", "myemail@ualberta.ca",
//                "7805555555", "FirstName", "LastName");
//        Task task = new Task(user, "Task Description", "Task Title1",
//                20.00f, Task.TaskStatus.REQUESTED);
//
//        //Create a task, add it to the server, check to ensure it's there
//        addObjectTask.execute(task);
//        getObjectTask.execute("Task Title1");
//        try {
//            taskList = getObjectTask.get();
//        } catch (Exception e) {}
//
//        assertFalse(taskList.isEmpty());
//
//        //Change the title of the task and update the server, check if it's there
//        taskList.clear();
//        task.setTitle("Task Title2");
//        updateObjectTask.execute(task);
//        getObjectTask.execute("Task Title2");
//        try {
//            taskList = getObjectTask.get();
//        } catch (Exception e) {}
//
//        assertFalse(taskList.isEmpty());
//
//    }

//    public void testDoesUserExist() {
//        String username = "testusername";
//        DataManager.addObject addObjectTask = new DataManager.addObject();
//        User user = new User("testusername", "myemail@ualberta.ca",
//                "7805555555", "FirstName", "LastName");
//
//        assertFalse(DataManager.doesUserExist(username));
////        Will change when implementation is solved
////        addObjectTask.execute(user);
//        assertTrue(DataManager.doesUserExist(username));
//    }


}