package cmput301w18t09.orbid;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.SearchView;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by zachredfern on 2018-04-07.
 */
public class SearchTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public SearchTest() {
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

    public void testSearch() {

        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        ArrayList<String> queryList = new ArrayList<>();



        // Delete old mocks


        // Delete the test searcher if their account already exists
        DataManager.deleteUsers delSearcher = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("searchTester");
        if (!queryList.get(0).toString().equals("username")) {
            delSearcher.execute(queryList);
        }

        solo.sleep(1500);

        // Delete test task 1 if it already exists
        DataManager.deleteTasks delTask1 = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("Clean my Home");
        if (!queryList.get(0).toString().equals("or")) {
            delTask1.execute(queryList);
        }

        solo.sleep(2000);


        // Delete test task 1 if it already exists
        DataManager.deleteTasks delTask2 = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("Need Maid");
        if (!queryList.get(0).toString().equals("or")) {
            delTask2.execute(queryList);
        }

        solo.sleep(1500);


        // Delete test task 1 if it already exists
        DataManager.deleteTasks delTask3 = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("Pizza Delivery");
        if (!queryList.get(0).toString().equals("or")) {
            delTask3.execute(queryList);
        }

        solo.sleep(1500);

        assertTrue(Boolean.FALSE);



        // Create new mocks



        // Create an account for the test requester
        // NOTE: Creating accounts is covered in another use case
        DataManager.addUsers addSearcher = new DataManager.addUsers(context);
        User searcher = new User("searchTester", "test", "zred@hotmail.com", "7809396963", "Zach", "Redfern");
        addSearcher.execute(searcher);

        // Create sample tests to bid on
        // NOTE: Adding tasks is covered in another use case)
        DataManager.addTasks addTask1 = new DataManager.addTasks(context);
        Task task1 = new Task("NeatGuy12", "I need someone to clean my house weekly", "Clean my Home", 50.00, Task.TaskStatus.REQUESTED);
        addTask1.execute(task1);

        // Create sample tests to bid on
        // NOTE: Adding tasks is covered in another use case)
        DataManager.addTasks addTask2 = new DataManager.addTasks(context);
        Task task2 = new Task("CleanFreak66", "I don't have much time anymore so I need someone to clean up after my kids.", "Need Maid", 45.00, Task.TaskStatus.REQUESTED);
        addTask2.execute(task2);

        // Create sample tests to bid on
        // NOTE: Adding tasks is covered in another use case)
        DataManager.addTasks addTask3 = new DataManager.addTasks(context);
        Task task3 = new Task("RegularPerson1", "I would like someone to deliver me a nice pizza.", "Pizza Delivery", 24.50, Task.TaskStatus.REQUESTED);
        addTask3.execute(task3);

        solo.sleep(3000);


        // Login as the test searcher


        // Login as the first test provider
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "searchTester");
        solo.enterText((EditText) solo.getView(R.id.login_etPassword), "test");
        solo.sleep(2000);
        solo.clickOnButton("Sign In");

        // Assert that we have entered the recent listings activity
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);
        RecentListingsActivity recentListingsActivity = (RecentListingsActivity) solo.getCurrentActivity();



        // Perform a search
        final SearchView searchView = (SearchView) solo.getView(R.id.search_view);
        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchView.setQuery("clean", true);
            }
        });
        //searchView.setQuery("clean", true);
        solo.sleep(3000);


        solo.waitForText("Clean my Home", 1, 2000);

        solo.waitForText("Need Maid", 1, 2000);


        // Remove mock data from server


        // Delete the test searcher
        DataManager.deleteUsers delSearcher1 = new DataManager.deleteUsers(context);
        ArrayList<String> userID = new ArrayList<>();
        userID.add(searcher.getID());
        delSearcher1.execute(userID);

        solo.sleep(1000);

        // Delete the test tasks
        DataManager.deleteTasks deleteTasks = new DataManager.deleteTasks(context);
        ArrayList<String> taskID = new ArrayList<>();
        taskID.add(task1.getID());
        taskID.add(task2.getID());
        taskID.add(task3.getID());
        deleteTasks.execute(taskID);

    }
}