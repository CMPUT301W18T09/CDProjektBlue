package cmput301w18t09.orbid;

import android.content.Context;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.google.maps.model.LatLng;
import com.robotium.solo.Solo;

import java.util.ArrayList;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by zachredfern on 2018-04-07.
 *
 * @author Zach Redfern
 */

public class GeolocationTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public GeolocationTest() {
        super(cmput301w18t09.orbid.LoginActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    // Credit: David Laycock
    // Method to return the server ID of a User based on the username
    public ArrayList<String> findUserID(String username) {
        DataManager.getUsers getUser = new DataManager.getUsers(getActivity());
        ArrayList<String> queryList = new ArrayList<>();
        ArrayList<User> usersList = new ArrayList<>();

        queryList.add("username");
        queryList.add(username);
        getUser.execute(queryList);

        try {
            usersList = getUser.get();
        } catch (Exception e) {}

        if (!usersList.isEmpty()) {
            String stringQuery = usersList.get(0).getID();
            queryList.clear();
            queryList.add(stringQuery);
        }

        return queryList;
    }

    // Returns the server ID of a Task based on the task title
    public ArrayList<String> findTaskID(String title) {
        DataManager.getTasks getTask = new DataManager.getTasks(getActivity());
        ArrayList<String> queryList = new ArrayList<>();
        ArrayList<Task> taskList = new ArrayList<>();

        // Fetch the task from the server with a given title
        queryList.clear();
        queryList.add("or");
        queryList.add("title");
        queryList.add(title);

        try {
            getTask.execute(queryList);
            taskList = getTask.get();
        }
        catch (Exception e) {
            Log.e("Task ID Error", "Error getting Task ID from server");
            e.printStackTrace();;
            assertTrue(Boolean.FALSE);
        }

        // If we got a task back, return it's ID to the caller, else report the error
        if (!taskList.isEmpty()) {
            queryList.clear();
            queryList.add(taskList.get(0).getID());
        }

        return queryList;
    }

    public void testViewLocation() {

        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        ArrayList<String> queryList = new ArrayList<>();


        // Delete mocks, if they exist


        // Delete the tester if their account already exists
        DataManager.deleteUsers delRequester = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("GeoTester");
        if (!queryList.get(0).toString().equals("username")) {
            Log.e("found", queryList.get(0).toString());
            delRequester.execute(queryList);
        }

        solo.sleep(1000);


        // Delete test task  if it already exists
        DataManager.deleteTasks delTask = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("Help Me Study");
        delTask.execute(queryList);

        solo.sleep(1000);


        // Create mocks


        // Create an account for the second test provider
        // NOTE: Creating accounts is covered in another use case
        DataManager.addUsers addUser = new DataManager.addUsers(context);
        User user= new User("GeoTester", "test","friday@hotmail.com", "9805567812", "Rebecca", "Black");
        addUser.execute(user);

        // Create sample tests to bid on
        // NOTE: Adding tasks is covered in another use case)
        DataManager.addTasks addTask = new DataManager.addTasks(context);
        Task task = new Task("NotSmart23", "I need help to study for my calculus final", "Help Me Study", 60.00, Task.TaskStatus.REQUESTED);

        // Get the task locations details
        LatLng location = new LatLng(53.526757, -113.527397);
        String geoResult = null;
        try {
            geoResult = MapActivity.getAddress(location, context.getResources());
        }
        catch (Exception e) {
            assertTrue(Boolean.FALSE);

        }
        task.setLocation(location);
        task.setStringLocation(geoResult);
        addTask.execute(task);


        // Login as the first test provider
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "GeoTester");
        solo.enterText((EditText) solo.getView(R.id.login_etPassword), "test");
        solo.sleep(2000);
        solo.clickOnButton("Sign In");

        // Assert that we have entered the recent listings activity
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);
        RecentListingsActivity recentListingsActivity = (RecentListingsActivity) solo.getCurrentActivity();
        ArrayList<Task> taskList1 = recentListingsActivity.getTaskList();

        // Get the position of the test tasks in the task list
        int posTask1 = -1;
        for (int i = 0; i < taskList1.size(); ++i) {
            if (taskList1.get(i).getTitle().equals("Help Me Study")) {
                posTask1 = i;
            }
        }

        // If the test task was not found, fail the test
        if (posTask1 == -1) {
            assertTrue(Boolean.FALSE);
        }

        // Click the test task
        solo.sleep(3000);
        solo.scrollDownRecyclerView(posTask1);
        solo.clickInRecyclerView(posTask1);
        solo.assertCurrentActivity("Wrong Activity", TaskDetailsActivity.class);

        solo.sleep(2000);

        EditText etLocation = (EditText) solo.getView(R.id.details_location);
        solo.clickOnView(etLocation);
        solo.clickOnView(etLocation);

        solo.sleep(6000);

        DataManager.deleteUsers delUsers = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList.add(user.getID());
        delUsers.execute(queryList);


        solo.sleep(1000);

        // Delete test task 1 if it already exists
        DataManager.deleteTasks delTasks = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList.add(task.getID());
        delTasks.execute(queryList);

    }

    public void testTasksInRadius() {

        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        ArrayList<String> queryList = new ArrayList<>();


        // Delete mocks, if they exist


        // Delete the tester if their account already exists
        DataManager.deleteUsers delRequester = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("GeoTester");
        if (!queryList.get(0).toString().equals("username")) {
            Log.e("found", queryList.get(0).toString());
            delRequester.execute(queryList);
        }

        solo.sleep(1000);


        // Delete test task  if it already exists
        DataManager.deleteTasks delTask = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("Help Me Study");
        delTask.execute(queryList);

        solo.sleep(1000);


        // Create mocks


        // Create an account
        DataManager.addUsers addUser = new DataManager.addUsers(context);
        User user= new User("GeoTester", "test","friday@hotmail.com", "9805567812", "Rebecca", "Black");
        addUser.execute(user);

        // Create sample tasks
        DataManager.addTasks addTask = new DataManager.addTasks(context);
        Task task = new Task("NotSmart23", "I need help to study for my calculus final", "Help Me Study", 60.00, Task.TaskStatus.REQUESTED);

        // Get the task locations details
        LatLng location = new LatLng(53.526757, -113.527397);
        String geoResult = null;
        try {
            geoResult = MapActivity.getAddress(location, context.getResources());
        }
        catch (Exception e) {
            assertTrue(Boolean.FALSE);

        }
        task.setLocation(location);
        task.setStringLocation(geoResult);
        addTask.execute(task);


        // Login as the first test provider
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "GeoTester");
        solo.enterText((EditText) solo.getView(R.id.login_etPassword), "test");
        solo.sleep(2000);
        solo.clickOnButton("Sign In");

        // Assert that we have entered the recent listings activity
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);

        // Set our current location to University of Alberta
        NavigationActivity navActivity = (NavigationActivity) solo.getCurrentActivity();
        String geoResult1 = null;
        try {
            geoResult1 = MapActivity.getAddress(new LatLng(53.526177, -113.527976), context.getResources());
        }
        catch (Exception e) {
            assertTrue(Boolean.FALSE);

        }
        navActivity.thisLocation = new Location(geoResult1);

        RecentListingsActivity recentListingsActivity = (RecentListingsActivity) solo.getCurrentActivity();
        solo.clickOnView(recentListingsActivity.tbtnToggle);
        
        solo.sleep(6000);

        DataManager.deleteUsers delUsers = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList.add(user.getID());
        delUsers.execute(queryList);


        solo.sleep(1000);

        // Delete test task 1 if it already exists
        DataManager.deleteTasks delTasks = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList.add(task.getID());


    }
}

