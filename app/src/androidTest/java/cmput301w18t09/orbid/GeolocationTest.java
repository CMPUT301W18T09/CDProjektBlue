package cmput301w18t09.orbid;

import android.content.Context;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.maps.model.LatLng;
import com.robotium.solo.Solo;

import java.util.ArrayList;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

/**
 * The functions below intent test the Geolocation use cases (UC 10.01.01, UC 10.02.01, UC 10.03.01)
 * In particular, we test the ability to add a location to a task, the ability to view the location
 * that was provided on someone elses task, and also the ability to see the tasks within a 5km
 * radius of your current location.
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

    public void testAddLocation() {

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

//
//        // Delete test task  if it already exists
//        DataManager.deleteTasks delTask = new DataManager.deleteTasks(context);
//        queryList.clear();
//        queryList = findTaskID("Taxi Please");
//        delTask.execute(queryList);
//
//        solo.sleep(1000);

        // Create user

        // Create an account for the second test provider
        // NOTE: Creating accounts is covered in another use case
        DataManager.addUsers addUser = new DataManager.addUsers(context);
        User user= new User("GeoTester", "test","friday@hotmail.com", "9805567812", "Rebecca", "Black");
        addUser.execute(user);


        // Login as the first test provider
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "GeoTester");
        solo.enterText((EditText) solo.getView(R.id.login_etPassword), "test");
        solo.sleep(2000);
        solo.clickOnButton("Sign In");

        // Assert that we have entered the recent listings activity
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);

        // Create a new listing
        solo.clickOnImageButton(0);
        solo.sleep(1000);
        solo.clickOnText("My Listings");
        solo.sleep(2500);

//        MenuItem item = (MenuItem) solo.getView(R.id.MenuItem_AddButton);
//        solo.clickOnMenuItem(item);
        solo.clickOnView(solo.getView(R.id.MenuItem_AddButton));

        solo.sleep(2000);
        EditText etLocation = (EditText) solo.getView(R.id.etLocation);
        solo.clickOnView(etLocation);
        solo.sleep(6000);
        solo.clickOnScreen(500, 780);
        solo.sleep(3000);
        solo.clickOnText("Yes");
        solo.sleep(5000);


        // Delete the tester if their account already exists
        DataManager.deleteUsers delTester = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("GeoTester");
        if (!queryList.get(0).toString().equals("username")) {
            Log.e("found", queryList.get(0).toString());
            delTester.execute(queryList);
        }

        solo.sleep(1000);

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

        // Delete test task  if it already exists
        DataManager.deleteTasks delTask1 = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("I am a bad singer and need instruction");
        delTask1.execute(queryList);

        solo.sleep(1000);


        // Delete test task  if it already exists
        DataManager.deleteTasks delTask2 = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("I am Hungry");
        delTask2.execute(queryList);

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


        // Create sample tasks
        DataManager.addTasks addTask1 = new DataManager.addTasks(context);
        Task task1 = new Task("NeedStudy36", "I am a bad singer and need instruction", "I Need To Learn To Sing", 120.00, Task.TaskStatus.REQUESTED);

        // Get the task locations details
        location = new LatLng(53.521865, -113.528888);
        geoResult = null;
        try {
            geoResult = MapActivity.getAddress(location, context.getResources());
        }
        catch (Exception e) {
            assertTrue(Boolean.FALSE);
        }
        task1.setLocation(location);
        task1.setStringLocation(geoResult);
        addTask1.execute(task1);


        // Create sample tasks
        DataManager.addTasks addTask3 = new DataManager.addTasks(context);
        Task task3 = new Task("FoodLover77", "BRING ME FOOD", "I am Hungry", 25.00, Task.TaskStatus.REQUESTED);

        // Get the task locations details
        location = new LatLng(53.520844, -113.512448);
        geoResult = null;
        try {
            geoResult = MapActivity.getAddress(location, context.getResources());
        }
        catch (Exception e) {
            assertTrue(Boolean.FALSE);
        }
        task3.setLocation(location);
        task3.setStringLocation(geoResult);
        addTask3.execute(task3);


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
        Location targetLocation = new Location("");
        targetLocation.setLatitude(53.525341d);
        targetLocation.setLongitude(-113.527393d);
        navActivity.thisLocation = targetLocation;

        RecentListingsActivity recentListingsActivity = (RecentListingsActivity) solo.getCurrentActivity();
        solo.clickOnView(recentListingsActivity.tbtnToggle);

        solo.sleep(6000);

        DataManager.deleteUsers delUsers = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList.add(user.getID());
        delUsers.execute(queryList);


        solo.sleep(1000);

        // Delete test task  if it already exists
        DataManager.deleteTasks delTask3 = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("Help Me Study");
        delTask3.execute(queryList);

        solo.sleep(1000);

        // Delete test task  if it already exists
        DataManager.deleteTasks delTask4 = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("I am a bad singer and need instruction");
        delTask4.execute(queryList);

        solo.sleep(1000);


        // Delete test task  if it already exists
        DataManager.deleteTasks delTask5 = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("I am Hungry");
        delTask5.execute(queryList);

        solo.sleep(1000);
    }
}

