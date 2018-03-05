package cmput301w18t09.orbid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddEditTaskActivity extends NavigationActivity {

    private Button btnSavePost;
    private Button btnAddImage;
    private EditText etDescription;
    private EditText etTitle;
    private EditText etLocation;
    private TextView etPrice;
    private Context context = this;
    private Bitmap bitmap;
    private static Context mContext;
    private static final int SELECT_PICTURE = 1;
    private int isAdd;
    private ImageViewAdapter imageAdapter;
    private ArrayList<Bid> bidList = new ArrayList<Bid>();
    private ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
    private Task task;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Receive the layout ID from navigation activity
        int layoutID = getIntent().getIntExtra("addedit_layout_id", 0);
        isAdd = getIntent().getIntExtra("isAdd", 0);
        // Inflate the layout ID that was received
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(layoutID, frameLayout);
        // Get the task title and comment Edit Texts
        etTitle = findViewById(R.id.EditTaskTitle);
        etDescription = findViewById(R.id.EditTaskComment);
        User testUser = new User("NanTheMAN", "Nan@hotmail.com","1800NAN", "NAN", "THEMAN");
        task = new Task(testUser, "", "", 0, Task.TaskStatus.REQUESTED);

        btnSavePost = (Button)findViewById(R.id.SavePostTaskButton);

        // Setting up the recycler view for the images when you add a Task
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.ImageList);
        imageAdapter = new ImageViewAdapter(this, imageList);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(imageAdapter);
        mRecyclerView.setHasFixedSize(true);

        if(isAdd == 1) {
            btnSavePost.setText("Post");
        } else {
            // Show the price and bid list if you're only editting a task
            btnSavePost.setText("Save");
            etPrice = (TextView)findViewById(R.id.AddEditPrice);
            // Set text to the price
            etPrice.setText(Double.toString(task.getPrice()));
            Bid testBid = new Bid(testUser, 3.14, "test");
            task.addBid(testBid);
            bidList = task.getBidList();

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.BidList);
            BidListAdapter bidAdapter = new BidListAdapter(this, bidList);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(bidAdapter);
            recyclerView.setHasFixedSize(true);
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

    private void save() {

    }

    public void postEditTask(View view) {
        finish();
    }

    /**
     * When the button is tapped, it will prompt the user to take/select an image
     * Code taken from https://stackoverflow.com/questions/2708128/single-intent-to-let-user-take-picture-or-pick-image-from-gallery-in-android
     * @param view
     */
    public void addImage(View view) {

        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[] { takePhotoIntent }
                );

        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Updates the recycler image view to show the image selected
        if(resultCode==RESULT_OK)
        {
            Uri selectedimg = data.getData();
            try {
                imageList.add(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                //img.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
            } catch (IOException e) {

            }
            imageAdapter.notifyDataSetChanged();
        }

    }





    /**
     * Textwatcher code taken from https://stackoverflow.com/questions/8543449/how-to-use-the-textwatcher-class-in-android
     * Textwatcher inner class to watch over text boxes
     * @see TextWatcher
     */
    private class GenericTextWatcher implements TextWatcher {

        private View view;

        /**
         * Constructor for the text watcher
         * @param view
         */
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        /**
         * Called after the text has been changed
         * @param s
         */
        public void afterTextChanged(Editable s) {
        }

        /**
         * Called Before the text is changed
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
         * @param s
         * @param start
         * @param before
         * @param count
         */
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (view.getId() == R.id.TaskTitle) {
                String r = s.toString();
                //checks if any parts of the string are alphanumeric(forbidden)
                if (0 < r.length()) {
                    task.setTitle(r);
                }
            }
            if (view.getId() == R.id.TaskComment) {
                String r = s.toString();
                //checks if any parts of the string are alphanumeric(forbidden)
                if (0 < r.length()) {
                    task.setDescription(r);
                }
            }
        }
    }


}
