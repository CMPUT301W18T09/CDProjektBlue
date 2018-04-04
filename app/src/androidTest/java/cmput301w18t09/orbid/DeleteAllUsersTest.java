package cmput301w18t09.orbid;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.util.ArrayList;

/**
 * This test deletes all users from the server
 */
public class DeleteAllUsersTest extends ActivityInstrumentationTestCase2 {


    public DeleteAllUsersTest () { super(DataManager.class); }

    public void testDeleteAllUsers() {

        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        DataManager.deleteUsers deleteAll = new DataManager.deleteUsers(context);
        ArrayList<String> queryList = new ArrayList<>();

        try {
            queryList.add("");
            deleteAll.execute(queryList);
        } catch (Exception e) {
            Log.e("Uh oh", "Error trying to delete all");
        }
    }


}