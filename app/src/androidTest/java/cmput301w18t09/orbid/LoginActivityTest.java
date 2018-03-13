package cmput301w18t09.orbid;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

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
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Wrong Activity", CreateAccountActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }
}