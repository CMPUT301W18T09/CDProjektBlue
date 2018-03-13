package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ALL")
public class PlaceBidActivity extends TaskDetailsActivity {

    // private Button btnBid; not needed
    private EditText etPrice;
    private EditText etDescription;
    private DrawerLayout mDrawerLayout;
    private ArrayList<Task> taskList = new ArrayList<>();
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the id of the task to get it from the Data Manager
        id = getIntent().getStringExtra("_id");
        ArrayList<String> query = new ArrayList<>();
        query.add("_id");
        query.add(id);
        DataManager.getTasks getTasks = new DataManager.getTasks();
        getTasks.execute(query);
        try {
            taskList = getTasks.get();
            task = taskList.get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        etPrice = findViewById(R.id.my_bid_amount);
        etDescription = findViewById(R.id.my_bid_description);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * On click function for the bid button. Adds the bid if the inputted information
     * is valid. Otherwise, it makes a toast telling the user to properly fill out
     * the fields.
     * @param view
     */
    public void makeBid(View view) {
        if (!etPrice.getText().toString().isEmpty() && !etDescription.getText().toString().isEmpty()) {
            User user = new User("McChicken Man", "", "", "Nan", "Man");
            Bid bid = new Bid(user, Double.parseDouble(etPrice.getText().toString()), etDescription.getText().toString());
            task.addBid(bid);
            task.setStatus(Task.TaskStatus.BIDDED);
            DataManager.updateTasks updateTasks = new DataManager.updateTasks();
            updateTasks.execute(taskList);

        } else {
            Toast.makeText(this, "You need to fill out both bid fields properly", Toast.LENGTH_SHORT).show();
        }

    }
}
