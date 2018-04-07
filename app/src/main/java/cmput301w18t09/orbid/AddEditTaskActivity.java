package cmput301w18t09.orbid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.Manifest;
import com.google.gson.Gson;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

/**
 * An activity class used to display the Requesting users
 * Add task interface, edit task interface, and bidded task interface
 *
 * @author Chady Haidar, Aidan Kosik, Zach Redfern
 * @see Task
 */
public class AddEditTaskActivity extends NavigationActivity implements ItemClickListener {

    private EditText etDescription;
    private EditText etTitle;
    private EditText etPrice;
    private EditText etLocation;
    private EditText etStatus;
    private Context context = this;
    private static final int SELECT_PICTURE = 1;
    private static final int DELETE_PICTURE = 3;
    private int imagePos;
    private int isAdd;
    private BidListAdapter bidAdapter;
    private String id;
    private ImageViewAdapter imageAdapter;
    private ArrayList<Bid> bidList = new ArrayList<Bid>();
    private Task task;
    private Task changeTask;
    private DrawerLayout mDrawerLayout;
    private boolean permissionsGranted = true;
    private boolean fromMap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Receive the layout ID from navigation activity
        isAdd = getIntent().getIntExtra("isAdd", -1);
        id = getIntent().getStringExtra("_id");

        // Check if the user had just come from setting their location
        if (getIntent().getStringExtra("from_map") != null) {
            fromMap = true;
            Toast.makeText(this, "Location successfully chosen", Toast.LENGTH_LONG).show();
        }

        // Inflate the layout ID that was received
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);

        if (isAdd == 0 || isAdd == 1) {
            inflater.inflate(R.layout.activity_new_add_edit_task, frameLayout);
            etTitle = findViewById(R.id.EditTaskTitle);
            etDescription = findViewById(R.id.EditTaskComment);
            etPrice = findViewById(R.id.EditPrice);
            etLocation = findViewById(R.id.etLocation);
            etStatus = findViewById(R.id.etStatus);
        }
        else if (isAdd == 3) {
            inflater.inflate(R.layout.activity_new_bidlist_activity, frameLayout);
            etTitle = findViewById(R.id.etTitle);
            etDescription = findViewById(R.id.etComment);
            etPrice = findViewById(R.id.etPrice);
            etLocation = findViewById(R.id.etLocation);
            etStatus = findViewById(R.id.etStatus);
        }
        frameLayout.requestFocus();

        // Load the Task and User if it's not adding a new task
        if (isAdd != 1) {
            load(false);
            // Don't create the activity if the task is null
            if(task == null) {
                Toast.makeText(context, "This no longer exists", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        // Load the proper views
        activityTypeInit();

        // Initiate the stack view
        if (isAdd == 1 || isAdd == 0) {
            stackViewInit();
        }


        // Initiate Location Client
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M || permissionsGranted) {
            checkLocationPermission();
        }


    }

    /**
     * On click for the location button to open the map activity.
     * Different on click for adding and for on editing.
     * @param view - the button associated with the onClick
     * @see MapActivity
     */
    public void onClickLocation(View view) {
        if(!permissionsGranted) {
            checkLocationPermission();
        } else {
            if (!DataManager.isNetworkAvailable(this)) {
                Toast.makeText(this, "Cannot connect to Google Maps while offline.", Toast.LENGTH_SHORT).show();
                return;
            }

            // If we are not adding a task
            if (isAdd != 1) {
                MapActivity mapActivity = new MapActivity();
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("came_from", "edit_task");
                bundle.putInt("isAdd", 0);
                bundle.putString("_id", task.getID());
                try {
                    Log.i("GEO", "ID before: " + id + "and location" + MapActivity.getAddress(task.getLocation(), getResources()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mapActivity.setArguments(bundle);
                fm.beginTransaction().replace(R.id.navigation_content_frame, mapActivity).commit();
            }
            // If we are adding a task
            else {
                MapActivity mapActivity = new MapActivity();
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("came_from", "add_task");
                bundle.putInt("isAdd", 1);
                // Bring all of the entered info with us so we can reload it when we come back
                if (!etTitle.getText().toString().isEmpty()) {
                    bundle.putString("title", etTitle.getText().toString());
                }
                if (!etDescription.getText().toString().isEmpty()) {
                    bundle.putString("description", etDescription.getText().toString());
                }
                if (!etPrice.getText().toString().isEmpty()) {
                    bundle.putString("price", etPrice.getText().toString());
                }
                mapActivity.setArguments(bundle);
                fm.beginTransaction().replace(R.id.navigation_content_frame, mapActivity).commit();
            }
        }
    }

    /**
     * Displays the appropriate views for the type of task
     */
    private void activityTypeInit() {

        // Get the task title and comment Edit Texts
        Button btnSavePost;
        Button delete;

        if (isAdd == 1) {
            btnSavePost = findViewById(R.id.SavePostTaskButton);
            delete = findViewById(R.id.DeleteButton);
            btnSavePost.setVisibility(View.VISIBLE);
            btnSavePost.setText("Post");
            task = new Task(this.thisUser, "", "", 0, Task.TaskStatus.REQUESTED);
            delete.setVisibility(View.GONE);
            if (fromMap && isAdd == 1) {
                setAfterLocationValues();
            }
        } else if (isAdd == 3) {

            bidList = task.getBidList();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.BidListEdit);
            recyclerView.setVisibility(View.VISIBLE);
            bidAdapter = new BidListAdapter(this, bidList);
            bidAdapter.setClickListener(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(bidAdapter);
            recyclerView.setHasFixedSize(true);
            return;

        } else {
            // Show the price and bid list if you're only editing a task
            btnSavePost = findViewById(R.id.SavePostTaskButton);
            btnSavePost.setVisibility(View.VISIBLE);
            btnSavePost.setText("Save");
        }
    }

    /**
     * Sets the values that were entered when the user went to the map activity
     * so that they do not have to renter forms.
     */
    private void setAfterLocationValues() {
        if (getIntent().getStringExtra("title") != null) {
            String title = getIntent().getStringExtra("title");
            etTitle.setText(title);
            task.setTitle(title);
        }
        if (getIntent().getStringExtra("description") != null) {
            String description = getIntent().getStringExtra("description");
            etDescription.setText(description);
            task.setDescription(description);
        }
        if (getIntent().getStringExtra("location") != null) {
            String location = getIntent().getStringExtra("location");
            etLocation.setText(location);
        }

    }

    /**
     * Initiates the stack view
     */
    private void stackViewInit() {

        // Setting up the stack view for the images when you add a Task
        StackView stackView = findViewById(R.id.ImageStack);
        stackView.setInAnimation(this, android.R.animator.fade_in);
        stackView.setOutAnimation(this, android.R.animator.fade_out);
        imageAdapter = new ImageViewAdapter(this, task.getPhotoList());
        stackView.setAdapter(imageAdapter);

        // Set the click listener for the images
        stackView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Bitmap> temp;

                // Get the bitmap that was tapped from the photo list
                temp = task.getPhotoList();
                Bitmap image = temp.get(position);

                // Create a new intent and send it the byte array for bitmap
                Intent intent = new Intent(context, FullScreenImageActivity.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra("BitmapImage", bytes);
                intent.putExtra("isMyTask", 1);
                intent.putExtra("_id", task.getID());
                intent.putExtra("position", position);
                imagePos = position;
                startActivityForResult(intent, DELETE_PICTURE);
            }
        });
    }

    /**
     * Checks if the user has given the app permissions to access location services.
     * If not, it prompts the user to allow them.
     */
    private void checkLocationPermission() {
        final Activity activity = this;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permissionsGranted = true;
        }
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
     * Saves the current task in the database
     *
     * @see DataManager
     */
    @SuppressLint("MissingPermission")
    private void save() throws InterruptedException, ApiException, IOException {
        task.setTitle(etTitle.getText().toString());
        task.setDescription(etDescription.getText().toString());
        // Add location to the task
        try {
            task.setLocation(new LatLng(thisLocation.getLatitude(), thisLocation.getLongitude()));
        } catch(Exception e) {
            Log.e("LatLng", "Could not get location");
        }


        // Check to make sure all the fields are filled in properly
        if (task.getTitle().length() > 30) {
            Toast.makeText(context, "Title cannot be longer than 30 characters.", Toast.LENGTH_SHORT).show();
        } else if (task.getTitle().length() < 1) {
            Toast.makeText(context, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
        } else if (task.getDescription().length() > 300) {
            Toast.makeText(context, "Description cannot be longer than 300 characters.", Toast.LENGTH_SHORT).show();
        } else if (task.getDescription().length() < 1) {
            Toast.makeText(context, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
        } else {
            findViewById(R.id.loadingPanelAdd).setVisibility(View.VISIBLE);
            // Add the human readable location to the task if we have an internet connection
            if (etLocation.getText().length() > 0 && DataManager.isNetworkAvailable(this)) {
                String locationString = etLocation.getText().toString();
                LatLng latLng = MapActivity.fromAddress(locationString, getResources());
                task.setStringLocation(locationString);
                task.setLocation(latLng);
            }
            // Else, we still add the task but do not include a location
            else if (etLocation.getText().length() > 0 && !DataManager.isNetworkAvailable(this)){
                Toast.makeText(this, "Task added, but no location recorded due to failed network connection", Toast.LENGTH_SHORT).show();
            }


            // Save the new task to the DM
            Button btnSavePost = (Button) findViewById(R.id.SavePostTaskButton);
            if (btnSavePost.getText().equals("Post")) {
                DataManager.addTasks object = new DataManager.addTasks(this);
                object.execute(task);
                DataManager.backupTasks.add(task);
            }

            // Update the task only if we are editing
            if (btnSavePost.getText().equals("Save")) {
                update();
            }

            finish();
        }
    }

    /**
     * Update the task in the database
     *
     * @see DataManager
     */
    private void update() {

        // Update the task if it's being editted
        Button btnSavePost = (Button) findViewById(R.id.SavePostTaskButton);
        ListIterator<Task> it = DataManager.backupTasks.listIterator();
        ArrayList<Task> n = new ArrayList<>();
        n.add(task);

        // If we are editing a task that was created offline and we have not yet made a
        // connection, do a special update that will insert the task in the correct spot in the
        // cached list. If connection is regained, we will send the item to the server as normal
        if (task.getID() == null && btnSavePost.getText().equals("Save")) {

            Task oldTask = new Gson().fromJson(getIntent().getStringExtra("backupTask"), Task.class);
            DataManager.updateTaskNoID object = new DataManager.updateTaskNoID(context, oldTask);
            ArrayList<Task> backupTasks = DataManager.backupTasks;
            object.execute(n);

            for (int i = 0; i < backupTasks.size(); ++i) {
                if (Task.compareTasks(oldTask, backupTasks.get(i))) {
                    backupTasks.set(i, task);
                    break;
                }
            }

        }
        else {

            DataManager.updateTasks object = new DataManager.updateTasks(context);
            object.execute(n);

            while (it.hasNext()) {
                if (it.next().getID().equals(task.getID())) {
                    it.set(task);
                    break;
                }
            }
        }
    }

    /**
     * Loads the task that was opened
     *
     * @see DataManager
     * @see MapActivity
     */
    private void load(Boolean checkChanged) {

        // Load the task from the Data manager
        ArrayList<Task> taskList = new ArrayList<>();
        ArrayList<String> query = new ArrayList<>();
        query.add("and");
        query.add("_id");
        query.add(id);
        DataManager.getTasks getTasks = new DataManager.getTasks(this);
        getTasks.execute(query);
        try {
            taskList = getTasks.get();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Failed to load task", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(this, "Failed to load task", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if(!checkChanged) {
            // If there is no network available, fetch the backup task
            if (!DataManager.isNetworkAvailable(this)) {
                task = new Gson().fromJson(getIntent().getStringExtra("backupTask"), Task.class);
            } else {
                if (taskList.size() > 0) {
                    task = taskList.get(0);
                } else {
                    Toast.makeText(context, "There was an error. This task may no longer exist.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ListTaskActivity.class);
                    intent.putExtra("tasks_layout_id", R.layout.activity_list_requested_tasks);
                    intent.putExtra("isMyBids", 0);
                    this.startActivity(intent);
                }
            }



        } else {
            changeTask = taskList.get(0);
        }


        // Set the lowest bid in the view, if there is one
        if (task.getLowestBid() != -1) {
            etPrice.setText("Lowest Bid: $" + Double.toString(task.getLowestBid()));
        }
        else {
            etPrice.setText("Lowest Bid: N/A");
        }

        etTitle.setText(task.getTitle());
        etDescription.setText(task.getDescription());
        if (task.getStatus() == Task.TaskStatus.REQUESTED) {
            etStatus.setText("Status: Requested");
        }
        else if (task.getStatus()  == Task.TaskStatus.BIDDED) {
            etStatus.setText("Status: Bidded");
        }

        LatLng location = task.getLocation();
        if (location != null && DataManager.isNetworkAvailable(this)) {
            if (!fromMap) {
                Log.e("GEO", "FROM MAP IS FALSE");
                String geoResult = null;
                try {
                    geoResult = MapActivity.getAddress(location, getResources());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                etLocation.setText(geoResult);
            } else {
                if (getIntent().getStringExtra("location") != null) {
                    String fromLocation = getIntent().getStringExtra("location");
                    etLocation.setText(fromLocation);
                    try {
                        task.setLocation(MapActivity.fromAddress(fromLocation, getResources()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!fromMap) {
            if (task.getStringLocation() == null) {
                etLocation.setText("No Location Specified");
            } else {
                etLocation.setText(task.getStringLocation());
            }
        }
    }


    /**
     * Post/Edit button is pressed
     *
     * @param view The current activity view
     */
    public void postEditTask(View view) {
        // Check if the task has been bidded on if it is being editted
        if(isAdd == 0 && checkChanged()) {
            Toast.makeText(this, "Your task has been bidded on.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            try {
                save();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes the current task that is being worked on
     *
     * @param view The current activity view
     */
    public void deleteButton(View view) {

        // Don't allow a user to delete tasks while offline
        if (!DataManager.isNetworkAvailable(this )) {
            Toast.makeText(this, "Cannot delete tasks while offline", Toast.LENGTH_SHORT).show();
            return;
        }
        findViewById(R.id.loadingPanelAdd).setVisibility(View.VISIBLE);

        ArrayList<String> n = new ArrayList<>();
        n.add(task.getID());
        DataManager.deleteTasks object = new DataManager.deleteTasks(this);

        // Find the task in the backup list and remove it
        ListIterator<Task> it = DataManager.backupTasks.listIterator();
        while (it.hasNext()) {
            if (it.next().getID().equals(task.getID())) {
                it.remove();
            }
        }

        object.execute(n);
        finish();
    }

    /**
     * When the button is tapped, it will prompt the user to take/select an image
     * Code taken from https://stackoverflow.com/questions/2708128/single-intent-to-let-user-take-picture-or-pick-image-from-gallery-in-android
     *
     *  @param view
     */
    public void addImage(View view) {
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pickIntent, SELECT_PICTURE);
    }

    /**
     * Adds the bitmap to the image list after a user selects/takes a photo
     *
     * @param requestCode The code passed to the intent before it is started
     * @param resultCode The code returned when intent finishes
     * @param data The data added to the intent that was started
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Updates the recycler image view to show the image selected
        if (resultCode==RESULT_OK) {
                Uri selectedimg = data.getData();
                int dataSize = 0;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);
                    // Get the size of the image
                    try {
                        InputStream fileInputStream=getApplicationContext().getContentResolver().openInputStream(selectedimg);
                        dataSize = fileInputStream.available();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Check if the image is too large
                    if(dataSize > 65536) {
                        Toast.makeText(this, "Image size is too large", Toast.LENGTH_SHORT).show();
                    } else {
                        task.addPhoto(bitmap);
                        imageAdapter.updateList(task.getPhotoList());
                    }

                } catch (IOException e) {

                }
        } else if(resultCode == 188) {
            ArrayList<Bitmap> temp;
            temp = task.getPhotoList();
            temp.remove(imagePos);
            task.setPhotoList(temp);
            imageAdapter.updateList(task.getPhotoList());
        }
    }

    /**
     * Handles when a bid in My Bidded Tasks is tapped
     *
     * @param view The current activity view
     * @param position The index of the bid tapped
     * @param type
     */
    @Override
    public void onClick(View view, int position, int type) {

        // Don't allow a user to accept or decline bids while off the network
        if (!DataManager.isNetworkAvailable(this )) {
            Toast.makeText(this, "Cannot accept or decline bids while offline", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the bid that was tapped
        final Bid bid = bidList.get(position);

        //Inflate the dialog
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialog_view = layoutInflater.inflate(R.layout.dialog_accept_bid, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(AddEditTaskActivity.this)
                .setMessage("Accept bid for " + Double.toString(bid.getPrice()) + "?")
                .setTitle("Accept or Decline bid?")
                .setView(dialog_view);
        final AlertDialog dialog = builder.create();
        TextView bidUsername = dialog_view.findViewById(R.id.tv_username);
        bidUsername.setText(bid.getProvider());
        dialog.show();

        bidUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserInfo(bid.getProvider());
            }
        });

        Button btnAccept = dialog_view.findViewById(R.id.btnAccept);
        Button btnDecline = dialog_view.findViewById(R.id.btnDecline);
        Button btnCancel = dialog_view.findViewById(R.id.btnCancel);

        // Handles the scenario if accept bid is tapped
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = task.getBidList().indexOf(bid);
                // Accept the bid in the task class
                task.acceptBid(index);
                // Update the Task in data manager
                update();
                dialog.dismiss();
                finish();
            }
        });

        // Close the dialog if cancel is tapped
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Handles the scenario if Decline Bid is tapped
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidList = task.getBidList();
                // Removes the declined bid from the bidList
                bidList.remove(bid);
                // Sets the new bidList and updates the Task in DM
                task.setBidList(bidList);
                // If there are no more bids on the Task, set it back to Requested
                if(bidList.size() == 0) {
                    task.setStatus(Task.TaskStatus.REQUESTED);
                }
                update();
                dialog.dismiss();
                bidAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * Checks if the task status has changed to bidded if you save it
     */
    private Boolean checkChanged() {

        if (!DataManager.isNetworkAvailable(this)) {
            return false;
        }

        load(true);
        if(changeTask.getStatus().equals(Task.TaskStatus.BIDDED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Opens the user info dialog when pressed
     */
    public void openUserInfo(String username) {

        // Inform the user if they attempt to get user information while offline
        if (!DataManager.isNetworkAvailable(this )) {
            Toast.makeText(this, "User information cannot be fetched while offline", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        UserProfileDialog dialog = new UserProfileDialog();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "User Profile Dialog");

    }
}
