package cmput301w18t09.orbid;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by zachredfern on 2018-04-07.
 */

public class GeolocationLocationTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public GeolocationLocationTest() {
        super(cmput301w18t09.orbid.LoginActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }
}
