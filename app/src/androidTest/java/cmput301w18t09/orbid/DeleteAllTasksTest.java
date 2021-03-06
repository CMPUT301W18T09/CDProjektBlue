package cmput301w18t09.orbid;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * This test clears all tasks off the server
 */
public class DeleteAllTasksTest extends ActivityInstrumentationTestCase2 {


    public DeleteAllTasksTest () { super(DataManager.class); }

    public void testDeleteAllTasks() {

        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        DataManager.deleteTasks deleteAll = new DataManager.deleteTasks(context);
        ArrayList<String> queryList = new ArrayList<>();

        try {
            queryList.add("");
            deleteAll.execute(queryList);
        } catch (Exception e) {
            Log.e("Uh oh", "Error trying to delete all");
        }
    }


}