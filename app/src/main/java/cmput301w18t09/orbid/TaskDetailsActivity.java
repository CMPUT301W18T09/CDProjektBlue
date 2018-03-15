package cmput301w18t09.orbid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.StackView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ALL")
public class TaskDetailsActivity extends NavigationActivity{

    private DrawerLayout mDrawerLayout;
    private ArrayList<Task> taskList = new ArrayList<>();
    private String id;
    protected boolean mine = false;
    public Task task;
    public Bid bid;
    public int isAssigned = 0;
    public int isBid = 0;
    public Context context = this;
    private TextView task_title;
    private TextView task_description;
    private TextView text_lowest_bid;
    private TextView text_task_status;
    private TextView title;
    private TextView description;

    /**
     * Inflates the layout for task details. Sets the details of the task
     * being viewed. Initialises the stackView for the images of the task.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout the XML that goes to the corresponding child that is being inflated
        // Then setup the generic parts.
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(R.layout.activity_task_details, frameLayout);

        // Use the id of the task to get it from the Data Manager
        id = getIntent().getStringExtra("_id");
        try {
            isAssigned = getIntent().getIntExtra("isAssigned", 0);
        } catch(Error e) {
        }
        try {
            isBid = getIntent().getIntExtra("isBid", 0);
        } catch(Error e) {
        }
        ArrayList<String> query = new ArrayList<>();
        query.add("and");
        query.add("_id");
        query.add(id);
        DataManager.getTasks getTasks = new DataManager.getTasks(this);
        getTasks.execute(query);
        try {
            taskList = getTasks.get();
            task = taskList.get(0);
        } catch (InterruptedException e) {
            Log.e("MSG", "interrupted execution");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.e("MSG", "execution");
            e.printStackTrace();
        }
        // Find the text views in the layout
        task_title = findViewById(R.id.details_task_title);
        task_description = findViewById(R.id.details_task_description);
        text_lowest_bid = findViewById(R.id.details_lowest_bid);
        text_task_status = findViewById(R.id.details_task_status);
        title = findViewById(R.id.assignedBidTitle);
        description = findViewById(R.id.assignedBidDescription);

        //Log.i("MAP", "Location is " + task.getLocation().toString());
        // find lowest bid
        Bid lowest_bid = null;
        if (task == null) {
            Log.i("MSG", "task is null here");
        }

        // Display your bid price
        if(isBid == 1) {
            ArrayList<Bid> temp;
            temp = task.getBidList();
            for(Bid b: temp) {
                if(b.getProvider().toLowerCase().equals(thisUser.toLowerCase())){
                    bid = b;
                }
            }
            text_lowest_bid.setText("Your bid: $"+Double.toString(bid.getPrice()));
            // Set necessary elements to visible
            title.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            // Set the text to the items
            title.setText(bid.getProvider());
            description.setText(bid.getDescription());
        } else {
            // Check if the task is completed
            if (task.getStatus() == Task.TaskStatus.COMPLETED || task.getStatus() == Task.TaskStatus.ASSIGNED) {
                text_lowest_bid.setText("TASK FULFILLED");
            } else if(task.getBidList().size() == 0) {
                text_lowest_bid.setText("Price:$"+Double.toString(task.getPrice()));
            } else {
                // Find the lowest bid to display
                for (Bid bid : task.getBidList()) {
                    if (lowest_bid != null) {
                        if (bid.getPrice() < lowest_bid.getPrice()) {
                            lowest_bid = bid;
                        }
                    } else {
                        lowest_bid = bid;
                    }
                }
                if (lowest_bid != null) {
                    text_lowest_bid.setText("Lowest Bid:$" + Double.toString(lowest_bid.getPrice()));
                }
            }
        }
        // Set the task title and description
        task_title.setText(task.getTitle());
        task_description.setText(task.getDescription());
        text_task_status.setText(task.getStatus().toString());
        Log.i("MSG", task_title.getText().toString());
        // Set the username button
        Button usernameBtn = (Button) findViewById(R.id.usernameButton);
        usernameBtn.setText("Poster: " + task.getRequester());

        usernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username", task.getRequester());
                UserProfileDialog dialog = new UserProfileDialog();
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "User Profile Dialog");
            }
        });

        // Setting up the assigned bid layout
        // 1 means assigned, 2 means completed, 0 is for recent listings
        if(isAssigned == 1 || isAssigned == 2) {
            int b = task.getAcceptedBid();
            bid = task.getBidList().get(b);
            // Show the buttons if the task is Assigned
            if(isAssigned == 1) {
                Button fulfilledBtn = (Button) findViewById(R.id.fulfilledButton);
                Button repostBtn = (Button) findViewById(R.id.repostButton);
                fulfilledBtn.setVisibility(View.VISIBLE);
                repostBtn.setVisibility(View.VISIBLE);
            }
            // Set necessary elements to visible
            title.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);

            // Set the text to the items
            text_lowest_bid.setText("Bid price: $" + Double.toString(bid.getPrice()));
            title.setText(bid.getProvider());
            description.setText(bid.getDescription());
        }

        // Setting up the stack view for the images when you add a Task
        StackView stackView = findViewById(R.id.ImageStack);
        stackView.setInAnimation(this, android.R.animator.fade_in);
        stackView.setOutAnimation(this, android.R.animator.fade_out);
        ImageViewAdapter imageAdapter = new ImageViewAdapter(this, task.getPhotoList());
        stackView.setAdapter(imageAdapter);
        stackView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Bitmap> temp;
                // Get the bitmap that was tapped from the photo list
                temp = task.getPhotoList();
                Bitmap image = temp.get(position);
                // Create a new intent and send it the byte array for bitmap
                Intent intent = new Intent(context, FullScreenImage.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra("BitmapImage",bytes);
                intent.putExtra("isMyTask", 0);
                intent.putExtra("_id", task.getID());
                intent.putExtra("position", position);
                startActivity(intent);

            }
        });
        if (task.getRequester().equals(this.thisUser)) {
            mine = true;
        }

    }


    /**
     * Function for when an options menu item is selected.
     * @param item
     * @return boolean if the item was selected
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
     * sets the task status to completed and kills the activity
     * @param view
     */
    public void fulfilled(View view) {
        task.setStatus(Task.TaskStatus.COMPLETED);
        save();
        finish();

    }

    /**
     * Sets the task status to requested/bidded and deletes the bid
     * @param view
     */
    public void repost(View view) {
        // Remove the previously accepted bid
        ArrayList<Bid> temp = new ArrayList<>();
        task.setBidList(temp);
        task.acceptBid(-1);
        task.setStatus(Task.TaskStatus.REQUESTED);
        save();
        finish();
    }

    /**
     * Opens the user info dialog when pressed
     * @param view
     */
    public void openUserInfo(View view) {
        Toast.makeText(this, "Open user info", Toast.LENGTH_SHORT).show();
    }

    /**
     * Saves the current task in the database
     */
    private void save() {
        ArrayList<Task> n = new ArrayList<>();
        n.add(task);
        DataManager.updateTasks object = new DataManager.updateTasks(this);
        object.execute(n);
    }
}
