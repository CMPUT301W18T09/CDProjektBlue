package cmput301w18t09.orbid;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import java.util.ArrayList;

import com.robotium.solo.Solo;

/**
 * Created by micag on 2018-03-18.
 *
 * This covers all of the user cases for Task Basics (UC 01.01.01, UC 01.02.01, UC 01.03.01, UC 01.04.01).
 */

public class TaskBasicsTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public TaskBasicsTest() {
        super(LoginActivity.class);
    }

    public void setUp() {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testTaskBasics() throws Exception {

        //Adding test user
        User testUser = new User("mica", "test", "mica@test.com", "1234567890", "Mica", "g");
        DataManager.addUsers addUsers = new DataManager.addUsers(solo.getCurrentActivity().getBaseContext());
        addUsers.execute(testUser);

        // Login
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText(0, "mica");
        solo.enterText(1, "test");
        solo.clickOnText("Sign In");

        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);

        // Going to My listings through navigation drawer
        solo.clickOnImageButton(0);
        solo.clickOnText("My Listings");
        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        assertTrue(solo.waitForText("New Listings"));

        //Testing adding a task and posting it
        solo.clickOnView(solo.getView(R.id.MenuItem_AddButton));;
        solo.assertCurrentActivity("Wrong Activity",AddEditTaskActivity.class);
        solo.enterText((EditText) solo.getView(R.id.EditTaskTitle), "Adding Test");
        solo.enterText((EditText) solo.getView(R.id.EditTaskComment), "Testing that we can add tasks");
        solo.clickOnButton("Post");
        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        solo.sleep(1000);

        //Testing editing a task by changing all of the editable fields and saving
        solo.clickInRecyclerView(0);
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity",AddEditTaskActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.EditTaskTitle));
        solo.clearEditText((EditText) solo.getView(R.id.EditTaskComment));
        solo.enterText((EditText) solo.getView(R.id.EditTaskTitle), "Editing Test");
        solo.enterText((EditText) solo.getView(R.id.EditTaskComment), "Testing that we can edit tasks");
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);

        //Now we can see if the edited version shows up in the My Requested page
        solo.sleep(1000);

        //Testing to delete a task
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity",AddEditTaskActivity.class);
        solo.clickOnButton("Delete");
        solo.assertCurrentActivity("Wrong Activity",ListTaskActivity.class);
        solo.sleep(1000);


        // Delete test user
        ArrayList<String> ID = new ArrayList<>();
        DataManager.deleteUsers deleteUsers = new DataManager.deleteUsers(solo.getCurrentActivity().getBaseContext());
        ID.clear();
        ID.add(testUser.getID());
        deleteUsers.execute(ID);
    }
    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}