package cmput301w18t09.orbid;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by david on 13/03/18.
 */

public class LoginActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public LoginActivityTest() {
        super(cmput301w18t09.orbid.LoginActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

//    public ArrayList<String> findUserID(String username) {
//        DataManager.getUsers getUser = new DataManager.getUsers(getActivity());
//        ArrayList<String> queryList = new ArrayList<>();
//        ArrayList<User> usersList = new ArrayList<>();
//
//        queryList.add("username");
//        queryList.add(username);
//        getUser.execute(queryList);
//
//        try {
//            usersList = getUser.get();
//        } catch (Exception e) {}
//
//        if (!usersList.isEmpty()) {
//            String stringQuery = usersList.get(0).getID();
//            queryList.clear();
//            queryList.add(stringQuery);
//        }
//
//        return queryList;
//    }

    public void testLogin() {
        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        DataManager.deleteUsers delUser = new DataManager.deleteUsers(context);
        DataManager.getUsers getUser = new DataManager.getUsers(getActivity());
        ArrayList<String> queryList = new ArrayList<>();
        ArrayList<User> usersList = new ArrayList<>();

        //Get the ID of testUser and delete it if it exists
        //TODO create findUserID(String username)
        queryList.add("username");
        queryList.add("testUser");
        getUser.execute(queryList);

        try {
            usersList = getUser.get();
        } catch (Exception e) {}

        if (!usersList.isEmpty()) {
            String stringQuery = usersList.get(0).getID();
            queryList.clear();
            queryList.add(stringQuery);
            delUser.execute(queryList);
        }

        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Wrong Activity", CreateAccountActivity.class);

        //(Re)Create the testUser
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etFirstName), "Barry");
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etLastName), "Bonds");
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etEmail), "BigGunz@762runs.com");
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etPhoneNumber), "7805551234");
        solo.enterText((EditText) solo.getView(R.id.create_account_etUsername), "testUser");
        solo.clickOnButton("Create");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        //Sign in as testUser
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "testUser");
        solo.clickOnButton("Sign In");
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);

        //Open up Navigation Drawer and select Edit Profile
        //https://stackoverflow.com/questions/23053593/correct-way-to-open-navigationdrawer-and-select-items-in-robotium
        solo.clickOnImageButton(0); // open menu
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN); // select the fifth item
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);

        //Edit First name, go back to recent listings, return to edit profile to ensure change was saved
        solo.assertCurrentActivity("Wrong Activity", EditProfileActivity.class);
        solo.clearEditText(0);
        solo.enterText(0, "James");
        solo.clickOnButton("Save");
        solo.goBack();
        solo.clickOnImageButton(0); // open menu
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN); // select the fifth item
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
        solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
        solo.searchText("James");

        //Create a second user on server and add a task from them

        //Go to second users task and view their profile
    }

}