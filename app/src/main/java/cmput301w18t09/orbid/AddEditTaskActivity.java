package cmput301w18t09.orbid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.StackView;
import android.widget.Switch;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

/**
 * An activity class used to display the Requesting users
 * Add task interface, edit task interface, and bidded task interface
 *
 * @author Chady Haidar, Aidan Kosik, Zach Refern
 * @see Task
 */
public class AddEditTaskActivity extends NavigationActivity implements ItemClickListener {

    private EditText etDescription;
    private EditText etTitle;
    private EditText etPrice;
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
    private User user;
    private DrawerLayout mDrawerLayout;
    private boolean permissionsGranted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Receive the layout ID from navigation activity
        isAdd = getIntent().getIntExtra("isAdd", 0);
        id = getIntent().getStringExtra("_id");

        // Inflate the layout ID that was received
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(R.layout.activity_add_edit_task, frameLayout);
        frameLayout.requestFocus();

        etTitle = findViewById(R.id.EditTaskTitle);
        etDescription = findViewById(R.id.EditTaskComment);
        etPrice = findViewById(R.id.EditPrice);

        // Load the Task and User if it's not adding a new task
        if (isAdd != 1) {
            load();
            // Don't create the activity if the task is null
            if(task == null) {
                Toast.makeText(context, "This no longer exists", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        // Load the proper views
        activityTypeInit();
        // Initiate the stack view
        stackViewInit();
        // Initiate Location Client
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M || permissionsGranted) {
            checkLocationPermission();
        }
    }

    /**
     * Displays the appropriate views for the type of task
     */
    private void activityTypeInit() {

        // Get the task title and comment Edit Texts
        Button btnSavePost = (Button) findViewById(R.id.SavePostTaskButton);
        Button delete = (Button) findViewById(R.id.DeleteButton);

        if (isAdd == 1) {
            btnSavePost.setText("Post");
            task = new Task(this.thisUser, "", "", 0, Task.TaskStatus.REQUESTED);
            delete.setVisibility(View.GONE);
        } else if (isAdd == 3) {
            etPrice.setVisibility(View.GONE);
            btnSavePost.setVisibility(View.GONE);
            // Initiate the recycler view for bids
            bidList = task.getBidList();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.BidListEdit);
            recyclerView.setVisibility(View.VISIBLE);
            bidAdapter = new BidListAdapter(this, bidList);
            bidAdapter.setClickListener(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(bidAdapter);
            recyclerView.setHasFixedSize(true);
            etTitle.setEnabled(false);
            etDescription.setEnabled(false);
            etPrice.setEnabled(false);
        } else {
            // Show the price and bid list if you're only editing a task
            btnSavePost.setText("Save");
        }

        // Set the generic text watcher to save changes
        etTitle.addTextChangedListener(new GenericTextWatcher(etTitle));
        etDescription.addTextChangedListener(new GenericTextWatcher(etDescription));
        etPrice.addTextChangedListener(new GenericTextWatcher(etPrice));
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
    private void save() {

        // Add location to the task
        try {
            task.setLocation(new LatLng(thisLocation.getLatitude(), thisLocation.getLongitude()));
        } catch(Exception e) {
            Log.e("LatLng", "Could not get location");
        }

        // Check to make sure all the fields are filled in properly
        if (task.getTitle().length() > 30) {
            Toast.makeText(context, "Title cannot be longer than 30 characters.", Toast.LENGTH_LONG).show();
        } else if (task.getTitle().length() < 1) {
            Toast.makeText(context, "Please fill in all the fields.", Toast.LENGTH_LONG).show();
        } else if (task.getDescription().length() > 300) {
            Toast.makeText(context, "Description cannot be longer than 300 characters.", Toast.LENGTH_LONG).show();
        } else if (task.getDescription().length() < 1) {
            Toast.makeText(context, "Please fill in all the fields.", Toast.LENGTH_LONG).show();
        } else if(task.getPrice() == 0) {
            Toast.makeText(context, "Please enter a price above $0.", Toast.LENGTH_LONG).show();
        } else {
            findViewById(R.id.loadingPanelAdd).setVisibility(View.VISIBLE);
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
     */
    private void load() {

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

            // If there is no network available, fetch the backup task
            if (!DataManager.isNetworkAvailable()) {
                task = new Gson().fromJson(getIntent().getStringExtra("backupTask"), Task.class);
            }
            else {
              if (taskList.size() > 0) {
                  task = taskList.get(0);
              } else {
                  Toast.makeText(context, "There was an error. This task may no longer exist.", Toast.LENGTH_LONG).show();
                  Intent intent = new Intent( this, ListTaskActivity.class);
                  intent.putExtra("tasks_layout_id", R.layout.activity_list_requested_tasks);
                  intent.putExtra("isMyBids",0);
                  this.startActivity(intent);
              }   
            }

            etPrice.setText(Double.toString(task.getPrice()));
            etTitle.setText(task.getTitle());
            etDescription.setText(task.getDescription());
        } catch (InterruptedException e) {
            Toast.makeText(this, "Failed to load task", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(this, "Failed to load task", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        // Load the user from the Data manager
        DataManager.getUsers userDM = new DataManager.getUsers(this);
        ArrayList<String> n = new ArrayList<>();
        ArrayList<User> usersList;
        n.add("username");
        n.add(thisUser);
        userDM.execute(n);
        try {
            usersList = userDM.get();
            if (usersList.size() > 0) {
                user = usersList.get(0);
            } else {
                Toast.makeText(context, "This user may no longer exist", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // TODO: Handle the exception
        }
    }

    /**
     * Post/Edit button is pressed
     *
     * @param view The current activity view
     */
    public void postEditTask(View view) {
        save();
    }

    /**
     * Deletes the current task that is being worked on
     *
     * @param view The current activity view
     */
    public void deleteButton(View view) {

        // Don't allow a user to delete tasks while offline
        if (!DataManager.isNetworkAvailable()) {
            Toast.makeText(this, "Cannot delete tasks while offline", Toast.LENGTH_LONG).show();
            return;
        }

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
        if(resultCode==RESULT_OK) {
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
     * Handles when a task in My Bidded Tasks is tapped
     *
     * @param view The current activity view
     * @param position The index of the bid tapped
     * @param type
     */
    @Override
    public void onClick(View view, int position, int type) {

        // Don't allow a user to accept or decline bids while off the network
        if (!DataManager.isNetworkAvailable()) {
            Toast.makeText(this, "Cannot accept or decline bids while offline", Toast.LENGTH_LONG).show();
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
        dialog.show();

        Button btnAccept = dialog_view.findViewById(R.id.btnAccept);
        Button btnDecline = dialog_view.findViewById(R.id.btnDecline);
        Button btnCancel = dialog_view.findViewById(R.id.btnCancel);

        // Handles the scenario if accept bid is tapped
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Bid> temp = new ArrayList<>();
                temp.add(bid);
                // Accept the bid in the task class
                task.acceptBid(task.getBidList().indexOf(bid));
                // Set the tasks bidList to only the accepted bid
                task.setBidList(temp);
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
     * Textwatcher code taken from https://stackoverflow.com/questions/8543449/how-to-use-the-textwatcher-class-in-android
     * Textwatcher inner class to watch over text boxes
     *
     * @see TextWatcher
     */
    private class GenericTextWatcher implements TextWatcher {

        private View view;

        /**
         * Constructor for the text watcher
         *
         * @param view
         */
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        /**
         * Called after the text has been changed
         *
         * @param s
         */
        public void afterTextChanged(Editable s) {
        }

        /**
         * Called Before the text is changed
         *
         * @param s
         * @param start
         * @param count
         * @param after
         */
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        /**
         * Called when the text is being changed, makes sure all values being
         * changed are legal, then saves them
         *
         * @param s
         * @param start
         * @param before
         * @param count
         */
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (view.getId() == R.id.EditTaskTitle) {
                String r = s.toString();
                task.setTitle(r);
            }
            if (view.getId() == R.id.EditTaskComment) {
                String r = s.toString();
                task.setDescription(r);
            }
            if(view.getId() == R.id.EditPrice) {
                String r = s.toString();
                if(!r.isEmpty()) {
                    task.setPrice(Double.parseDouble(r));
                }
            }
        }
    }


}
