package cmput301w18t09.orbid;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
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

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testLogin() {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_etUsername), "david");
        solo.clickOnButton("Sign In");
        solo.assertCurrentActivity("Wrong Activity", RecentListingsActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    public void testSignUp() {
        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        DataManager.deleteUsers delUser = new DataManager.deleteUsers(context);
        DataManager.getUsers getUser = new DataManager.getUsers(getActivity());

        ArrayList<String> queryList = new ArrayList<>();
        ArrayList<User> usersList = new ArrayList<>();
        queryList.add("username");
        queryList.add("david2");
        getUser.execute(queryList);
        try {
            usersList = getUser.get();
        } catch (Exception e) {
        }

        String stringQuery = usersList.get(0).getID();
        queryList.clear();
        queryList.add(stringQuery);

        delUser.execute(queryList);

        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Wrong Activity", CreateAccountActivity.class);

        solo.enterText((EditText) solo.getView(R.id.edit_profile_etFirstName), "First Name");
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etLastName), "Last Name");
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etEmail), "Intent@intent.com");
        solo.enterText((EditText) solo.getView(R.id.edit_profile_etPhoneNumber), "0123456789");
        solo.enterText((EditText) solo.getView(R.id.create_account_etUsername), "david2");
        solo.clickOnButton("Create");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }
}