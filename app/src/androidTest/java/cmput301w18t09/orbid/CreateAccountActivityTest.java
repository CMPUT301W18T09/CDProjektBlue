package cmput301w18t09.orbid;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by david on 13/03/18.
 */

public class CreateAccountActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public CreateAccountActivityTest() {
        super(cmput301w18t09.orbid.CreateAccountActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

//    public void testCreateAccount() {
//        DataManager.deleteUsers delUser = new DataManager.addUsers(this);
//
//        solo.assertCurrentActivity("Wrong Activity", CreateAccountActivity.class);
//        solo.enterText((EditText) solo.getView(R.id.edit_profile_etFirstName), "First Name");
//        solo.enterText((EditText) solo.getView(R.id.edit_profile_etLastName), "Last Name");
//        solo.enterText((EditText) solo.getView(R.id.edit_profile_etEmail), "Intent@intent.com");
//        solo.enterText((EditText) solo.getView(R.id.edit_profile_etPhoneNumber), "0123456789");
//        solo.enterText((EditText) solo.getView(R.id.create_account_etUsername), "david2");
//        solo.clickOnButton("Create");
//
//        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
//        ArrayList<String> userList = new ArrayList<>();
//        userList.add("david2");
//        delUser.execute(userList);
//    }

}
