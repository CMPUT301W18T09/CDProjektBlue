package cmput301w18t09.orbid;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by micag on 2018-03-18.
 *
 * This covers all of the user cases for Task Basics (UC 01.01.01, UC 01.02.01, UC 01.03.01, UC 01.04.01).
 */

public class TaskBasicsTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public TaskBasicsTest() {
        super(NavigationActivity.class);
    }

    public void setUp() {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testTaskBasics() throws Exception {
        //This will be the user's username that we use for the test
        NavigationActivity.thisUser = "Mica404";

        //Takes us from the navigation to My Requests
        solo.clickOnImageButton(0);
        solo.clickOnText("My Requests");
        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);

        //Testing adding a task and posting it
        solo.clickOnButton("Add");
        solo.assertCurrentActivity("Wrong Activity",AddEditTaskActivity.class);
        solo.enterText((EditText) solo.getView(R.id.EditTaskTitle), "Adding Test");
        solo.enterText((EditText) solo.getView(R.id.EditTaskComment), "Testing that we can add tasks");
        solo.enterText((EditText) solo.getView(R.id.EditPrice), "50.00");
        solo.clickOnButton("Post");
        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);
        solo.sleep(1000);

        //Testing editing a task by changing all of the editable fields and saving
        solo.clickOnText("Adding Test");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity",AddEditTaskActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.EditTaskTitle));
        solo.clearEditText((EditText) solo.getView(R.id.EditTaskComment));
        solo.clearEditText((EditText) solo.getView(R.id.EditPrice));
        solo.enterText((EditText) solo.getView(R.id.EditTaskTitle), "Editing Test");
        solo.enterText((EditText) solo.getView(R.id.EditTaskComment), "Testing that we can edit tasks");
        solo.enterText((EditText) solo.getView(R.id.EditPrice), "60.00");
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Wrong Activity", ListTaskActivity.class);

        //Now we can see if the edited version shows up in the My Requested page
        solo.sleep(1000);

        //Testing to delete a task
        solo.clickOnText("Editing Test");
        solo.assertCurrentActivity("Wrong Activity",AddEditTaskActivity.class);
        solo.clickOnButton("Delete");
        solo.assertCurrentActivity("Wrong Activity",ListTaskActivity.class);
        solo.sleep(1000);

    }
    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}