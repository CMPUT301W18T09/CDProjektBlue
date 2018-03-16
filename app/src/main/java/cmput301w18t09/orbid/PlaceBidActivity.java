package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


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

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.details_frame_layout);
        inflater.inflate(R.layout.activity_place_bid, frameLayout);

        etPrice = frameLayout.findViewById(R.id.my_bid_amount);
        etDescription = frameLayout.findViewById(R.id.my_bid_description);

        // If this task is owned by the user, hide the frame layout that shows
        // the bidding section.
        if (mine) {
            frameLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Opens drawer when option menu is selected.
     * @param item
     * @return
     */
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
        Log.i("BID", "title is empty: " + etPrice.getText().toString());
        Log.i("BID", "description is empty: " + etDescription.getText().toString());
        // Let the user know this task has been completed and bidding is closed.
        if(task.getStatus() == Task.TaskStatus.COMPLETED){
            Toast.makeText(this, "This task has already been completed!", Toast.LENGTH_SHORT).show();
        } else if (!etPrice.getText().toString().isEmpty() && !etDescription.getText().toString().isEmpty()) {
            ArrayList<Bid> temp = task.getBidList();
            // Check if the user had a previous bid and delete it if so
            for(Bid b: temp) {
                if(b.getProvider().toLowerCase().equals(thisUser.toLowerCase())){
                    task.removeBid(b);
                }
            }
            // Add the new bid to the bid list
            Bid bid = new Bid(this.thisUser, Double.parseDouble(etPrice.getText().toString()), etDescription.getText().toString());
            task.addBid(bid);
            task.setStatus(Task.TaskStatus.BIDDED);
            Log.i("TASKDATE", "updating task with new bid");
            DataManager.updateTasks updateTasks = new DataManager.updateTasks(this);
            updateTasks.execute(taskList);
            finish();
        } else {
            Toast.makeText(this, "You need to fill out both bid fields properly", Toast.LENGTH_LONG).show();
        }

    }
}
