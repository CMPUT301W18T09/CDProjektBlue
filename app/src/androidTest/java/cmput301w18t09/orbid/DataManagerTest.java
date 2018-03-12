package cmput301w18t09.orbid;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by david on 24/02/18.
 */
public class DataManagerTest extends ActivityInstrumentationTestCase2
{

    private final User user = new User("myUsername", "myemail@ualberta.ca",
            "7805555555", "FirstName", "LastName");
    private final Task task = new Task(user, "Task Description", "testTask90210",
            20.00f, 1);

    public DataManagerTest () { super(DataManager.class); }

    public void testTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        ArrayList<String> queryList = new ArrayList<>();
        ArrayList<String> IDList = new ArrayList<>();

        DataManager.addTasks addTasksDM = new DataManager.addTasks();
        DataManager.getTasks getTasksDM = new DataManager.getTasks();
        DataManager.getTasks getTasksDM2 = new DataManager.getTasks();
        DataManager.getTasks getTasksDM3 = new DataManager.getTasks();
        DataManager.deleteTasks delTasksDM = new DataManager.deleteTasks();
        queryList.add("_type");
        queryList.add("task");


        //Verify task is not on server
        getTasksDM.execute(queryList);
        try {
            taskList = getTasksDM.get();
        } catch (Exception e) {}
//        Boolean testString = taskList.isEmpty();
//        Log.i("whatsinhere", "Before Addition: Tasklist empty? " + testString.toString());
//        assertTrue(taskList.isEmpty());


        //Add Task and verify it is on server
        taskList.clear();
        addTasksDM.execute(task);
        getTasksDM2.execute(queryList);
        try {
            taskList = getTasksDM2.get();
        } catch (Exception e) {}
        Integer testString = taskList.size();
        Log.i("whatsinhere", "After Addition: Task empty? " + testString.toString());
        for (int i = 0; i < taskList.size(); i++) {
            Task ttask = taskList.get(i);
            Log.i("whatsinhere", "ID: " + ttask.getID());
        }
//        assertFalse(taskList.isEmpty());


        //Delete task and verify it's not on server
        if (!taskList.isEmpty()) {
            Log.i("whatsinhere", "tasklist is " + taskList.get(0).getID());
            IDList.add(taskList.get(0).getID());
            delTasksDM.execute(IDList);
        }
        taskList.clear();
        getTasksDM3.execute(queryList);
        try {
            taskList = getTasksDM3.get();
        } catch (Exception e) {}
//        assertTrue(taskList.isEmpty());
    }






//    public void testDeleteTasks() {
//        ArrayList<Task> taskList = new ArrayList<>();
//        DataManager.deleteTasks delTasksDM = new DataManager.deleteTasks();
//        DataManager.getTasks getTasksDM = new DataManager.getTasks();
//        DataManager.addTasks addTasksDM = new DataManager.addTasks();
//        ArrayList<String> queryList = new ArrayList<>();
//
//        queryList.add("title");
//        queryList.add("testTask90210");
//        getTasksDM.execute(queryList);
//        try {
//            taskList = getTasksDM.get();
//        } catch (Exception e) {}
//        assertTrue(taskList.isEmpty());
//
//        User user = new User("myUsername", "myemail@ualberta.ca",
//                "7805555555", "FirstName", "LastName");
//        Task task = new Task(user, "Task Description", "testDeleteTasks",
//                20.00f, 1);
//        addTasksDM.execute(task);
//
//        getTasksDM.execute(queryList);
//        try {
//            taskList = getTasksDM.get();
//        } catch (Exception e) {}
//        assertFalse(taskList.isEmpty());
//
//        queryList.clear();
//        queryList.add(taskList.get(0).getID());
//        delTasksDM.execute(queryList);
//        assertTrue(taskList.isEmpty());
//    }

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