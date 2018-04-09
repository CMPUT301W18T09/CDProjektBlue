package cmput301w18t09.orbid;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by david on 13/03/18.
 * This Intent tests performs tests to create a user, edit a user, and open up a users details
 * Covers: UC 03.01.01, UC 03.02.01, UC 03.03.01
 */

public class UserProfileTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public UserProfileTest() {
        super(cmput301w18t09.orbid.LoginActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    //Method to return the server ID of a user based on the username
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

    public void testLogin() {
        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        DataManager.deleteUsers delUser = new DataManager.deleteUsers(context);

        //Delete the testUser if it already exists
        ArrayList<String> queryList = findUserID("testUser");
        delUser.execute(queryList);

        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Wrong Activity", CreateAccountActivity.class);

        //(Re)Create the testUser
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etFirstName), "Barry");
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etLastName), "Bonds");
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etEmail), "BigGunz@762runs.com");
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etPhoneNumber), "7805551234");
        solo.enterText((EditText) solo.getView(R.id.create_account_etUsername), "testUser");
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etPassword), "testUser");
        solo.clickOnButton("Create");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        //Sign in as testUser
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "testUser");
        solo.enterText((EditText) solo.getView(R.id.login_etPassword), "testUser");
        solo.clickOnButton("Sign In");
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);

        //Open up Navigation Drawer and select Edit Profile
        solo.clickOnImageButton(0); // open menu
        solo.clickOnText("Edit Profile");

        //Edit First name, go back to recent listings, return to edit profile to ensure change was saved
        solo.assertCurrentActivity("Wrong Activity", EditProfileActivity.class);
        solo.clearEditText(0);
        solo.enterText(0, "James");
        solo.enterText(4, "testUser");
        solo.clickOnButton("Save");
//        solo.goBack();

        solo.clickOnImageButton(0); // open menu
        solo.clickOnText("Edit Profile");
        solo.searchText("James");

        //Create a task, open its details and view user details from there
        Task task = new Task("testUser", "Girls just want to have fun",
                "Cut My Hair", 20, Task.TaskStatus.REQUESTED);
        new DataManager.addTasks(solo.getCurrentActivity().getBaseContext()).execute(task);
        solo.goBack();
        solo.clickOnImageButton(0);
        solo.clickOnText("Recent Listings");
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);

        solo.clickOnText("Cut My Hair");
        solo.clickOnText("testUser");
        solo.searchText("BigGunz@762runs.com");

        //Delete task
        ArrayList<String> ID = new ArrayList<>();
        ID.clear();
        ID.add(task.getID());
        new DataManager.deleteTasks(solo.getCurrentActivity().getBaseContext()).execute(ID);
    }

}