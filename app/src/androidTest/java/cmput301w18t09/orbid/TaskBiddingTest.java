package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by zachredfern on 2018-03-18.
 *
 * This test is very extensive and covers the following use cases:
 * UC 05.01.01, UC 05.02.01, UC 05.04.01, UC 05.05.01, UC 05.06.01, and UC 05.07.01.
 *
 * This covers all user stories for Task Bidding with the exception of notifications.
 *
 * The test flow is as follows:
 * 1. Login s the first task provider, make a bid on an existing task and check that the bid
 * is shown to the provider. (UC 05.01.01, UC 05.02.01).
 * 2. Login s the second task provider, make a bid on an existing task and check that the bid
 * is shown to the provider. (UC 05.01.01, UC 05.02.01).
 * 3. Login as the task requester and view a list that shows the tasks you have bids on (UC 05.04.01)
 * 4. Select the task with bids on it to view the bids (UC 05.05.01).
 * 5. Select the bid with the higher price and decline it (UC 05.07.01).
 * 6. Select the bid with the lower price and accept it. Navigate to the list view that shows that
 * the task now has a status of assigned.
 * 7. Login as the provider who was assigned to the task. Navigate to the list view that shows that
 * they were indeed assigned the task.
 */
public class TaskBiddingTest extends ActivityInstrumentationTestCase2{

    private Solo solo;

    public TaskBiddingTest() {
        super(cmput301w18t09.orbid.LoginActivity.class);
    }

    public void setUp() throws Exception{
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

    // Covers UC 05.01.01 and 05.02.01
    // Shows a task provider bidding on a task then a list of tasks the provider has bid on
    public void testBidOnTask() {

        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        ArrayList<String> queryList = new ArrayList<>();

        // Delete the test requester if their account already exists
        DataManager.deleteUsers delRequester = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("bidTestRequester");
        delRequester.execute(queryList);

        // Delete the first test provider if their account already exists
        DataManager.deleteUsers delProvider1 = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("bitTestProvider1");
        delProvider1.execute(queryList);

        // Delete the second test provider if their account already exists
        DataManager.deleteUsers delProvider2 = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("bitTestProvider2");
        delProvider2.execute(queryList);

        // Delete test task 1 if it already exists
        DataManager.deleteTasks delTask1 = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("Mow My Lawn (Bid Test)");
        Log.e("Mow My Lawn ID2", queryList.get(0).toString());
        delTask1.execute(queryList);

        // Create an account for the test requester
        // NOTE: Creating accounts is covered in another use case
        DataManager.addUsers addRequester = new DataManager.addUsers(context);
        User requester = new User("bidTestRequester", "zred@hotmail.com", "7809396963", "Zach", "Redfern");
        addRequester.execute(requester);

        // Create an account for the first test provider
        // NOTE: Creating accounts is covered in another use case
        DataManager.addUsers addProvider1 = new DataManager.addUsers(context);
        User provider1 = new User("bidTestProvider1", "bpanda@hotmail.com", "5875551234", "Bobbi", "Pandachuck");
        addProvider1.execute(provider1);

        // Create an account for the second test provider
        // NOTE: Creating accounts is covered in another use case
        DataManager.addUsers addProvider2 = new DataManager.addUsers(context);
        User provider2 = new User("bidTestProvider2", "friday@hotmail.com", "9805567812", "Rebecca", "Black");
        addProvider2.execute(provider2);

        // Create sample tests to bid on
        // NOTE: Adding tasks is covered in another use case)
        DataManager.addTasks addTask1 = new DataManager.addTasks(context);
        Task task1 = new Task("bidTestRequester", "Mow my lawn.", "Mow My Lawn (Bid Test)", 60.00, Task.TaskStatus.REQUESTED);
        addTask1.execute(task1);






        // Login as the first test provider
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "bidTestProvider1");
        solo.sleep(2000);
        solo.clickOnButton("Sign In");

        // Assert that we have entered the recent listings activity
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);
        RecentListingsActivity recentListingsActivity = (RecentListingsActivity) solo.getCurrentActivity();
        ArrayList<Task> taskList1 = recentListingsActivity.getTaskList();

        // Get the position of the test tasks in the task list
        int posTask1 = -1;
        for (int i = 0; i < taskList1.size(); ++i) {
            Log.e("Task", taskList1.get(i).getTitle());
            if (taskList1.get(i).getTitle().equals("Mow My Lawn (Bid Test)")) {
                posTask1 = i;
            }
        }

        // If the test task was not found, fail the test
        if (posTask1 == -1) {
            assertTrue(Boolean.FALSE);
        }

        // Click the first test task and place an initial bid on it
        solo.sleep(2000);
        solo.scrollDownRecyclerView(posTask1);
        solo.clickInRecyclerView(posTask1);
        solo.assertCurrentActivity("Wrong Activity", PlaceBidActivity.class);
        TextView title1 = (TextView) solo.getView(R.id.details_task_title);
        assertTrue(title1.getText().toString().equals("Mow My Lawn (Bid Test)"));
        FrameLayout frameLayout1 = (FrameLayout) solo.getView(R.id.details_frame_layout);
        solo.enterText((EditText) frameLayout1.findViewById(R.id.my_bid_amount), "59.99");
        solo.enterText((EditText) frameLayout1.findViewById(R.id.my_bid_description), "I will do it!");
        solo.sleep(2000);
        solo.clickOnButton("Bid");
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);

        // View the tasks the provider has bid on
        solo.clickOnImageButton(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        solo.sleep(2000);





        // Logout and login as the second task provider
        solo.goBack();
        solo.clickOnImageButton(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "bidTestProvider2");
        solo.sleep(2000);
        solo.clickOnButton("Sign In");

        // Assert that we have entered the recent listings activity
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);
        RecentListingsActivity recentListingsActivity2 = (RecentListingsActivity) solo.getCurrentActivity();
        ArrayList<Task> taskList2 = recentListingsActivity2.getTaskList();

        // Get the position of the test tasks in the task list
        int posTask2 = -1;
        for (int i = 0; i < taskList2.size(); ++i) {
            Log.e("Task", taskList2.get(i).getTitle());
            if (taskList2.get(i).getTitle().equals("Mow My Lawn (Bid Test)")) {
                posTask2 = i;
            }
        }

        // If the test task was not found, fail the test
        if (posTask2 == -1) {
            assertTrue(Boolean.FALSE);
        }

        // Click the first test task and place an initial bid on it
        solo.sleep(2000);
        solo.scrollDownRecyclerView(posTask2);
        solo.clickInRecyclerView(posTask2);
        solo.assertCurrentActivity("Wrong Activity", PlaceBidActivity.class);
        TextView title2 = (TextView) solo.getView(R.id.details_task_title);
        assertTrue(title1.getText().toString().equals("Mow My Lawn (Bid Test)"));
        FrameLayout frameLayout2 = (FrameLayout) solo.getView(R.id.details_frame_layout);
        solo.enterText((EditText) frameLayout2.findViewById(R.id.my_bid_amount), "40.00");
        solo.enterText((EditText) frameLayout2.findViewById(R.id.my_bid_description), "I have a GrassMaster6000!");
        solo.sleep(2000);
        solo.clickOnButton("Bid");
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);

        // View the tasks the provider has bid on
        solo.clickOnImageButton(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        solo.sleep(2000);



        // Logout and log in as the requester
        solo.goBack();
        solo.clickOnImageButton(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "bidTestRequester");
        solo.sleep(2000);
        solo.clickOnButton("Sign In");

        // View the tasks that the requester has bids on
        solo.assertCurrentActivity("WrongActivity", RecentListingsActivity.class);
        solo.clickOnImageButton(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.assertCurrentActivity("WrongActivity", ListTaskActivity.class);
        solo.clickOnImageButton(1);
        solo.sleep(3000);

        // Get the position of the test task in the task list
        ListTaskActivity taskListActivity = (ListTaskActivity) solo.getCurrentActivity();
        ArrayList<Task> taskList3 = taskListActivity.getTaskList();
        int pos = -1;
        for (int i = 0; i < taskList3.size(); ++i) {
            Log.e("Task", taskList3.get(i).getTitle());
            if (taskList3.get(i).getTitle().equals("Mow My Lawn (Bid Test)")) {
                pos = i;
            }
        }

        // Click the test task in the recycler view to view the bids made on the test
        solo.sleep(2000);
        solo.scrollDownRecyclerView(pos);
        solo.clickInRecyclerView(pos);
        solo.assertCurrentActivity("Wrong Activity", AddEditTaskActivity.class);
        solo.sleep(2000);

        // Decline one of the bids, show it is no longer in the list of bids on that task
        solo.scrollDownRecyclerView(0);
        solo.clickInRecyclerView(0);
        solo.sleep(2000);
        solo.clickOnButton("Decline");

        // Accept one bid and show it as accepted
        solo.scrollDownRecyclerView(0);
        solo.clickInRecyclerView(0);
        solo.sleep(2000);
        solo.clickOnButton("Accept");
        solo.clickOnImageButton(1);
        solo.sleep(2000);




        // Logout and login as the second task provider
        solo.goBack();
        solo.clickOnImageButton(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.sleep(2000);
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "bidTestProvider2");
        solo.sleep(2000);
        solo.clickOnButton("Sign In");

        // View the tasks the provider has bid on
        solo.clickOnImageButton(0);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        solo.sleep(2000);
        solo.clickOnImageButton(1);
        solo.sleep(3000);

        // Delete the test requester if their account already exists
        DataManager.deleteUsers delRequester2 = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("bidTestRequester");
        delRequester2.execute(queryList);

        // Delete the first test provider if their account already exists
        DataManager.deleteUsers delProvider3 = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("bitTestProvider1");
        delProvider3.execute(queryList);

        // Delete the second test provider if their account already exists
        DataManager.deleteUsers delProvider4 = new DataManager.deleteUsers(context);
        queryList.clear();
        queryList = findUserID("bitTestProvider2");
        delProvider4.execute(queryList);

        // Delete test task 1 if it already exists
        DataManager.deleteTasks delTask4 = new DataManager.deleteTasks(context);
        queryList.clear();
        queryList = findTaskID("Mow My Lawn (Bid Test)");
        Log.e("Mow My Lawn ID2", queryList.get(0).toString());
        delTask4.execute(queryList);
    }
}
