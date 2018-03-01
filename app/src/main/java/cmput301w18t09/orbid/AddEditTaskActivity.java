package cmput301w18t09.orbid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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
import android.widget.ListView;

import java.util.ArrayList;

public class AddEditTaskActivity extends NavigationActivity {

    private Button btnSave;
    private EditText etDescription;
    private EditText etTitle;
    private EditText etLocation;
    private EditText etPrice;
    private Task task;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutID = getIntent().getIntExtra("addedit_layout_id", 0);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(layoutID, frameLayout);
        etTitle = findViewById(R.id.EditTaskTitle);
        etDescription = findViewById(R.id.EditTaskComment);
        User testUser = new User("NanTheMAN", "Nan@hotmail.com","1800NAN", "NAN", "THEMAN");
        task = new Task(testUser, "", "", 0, Task.TaskStatus.REQUESTED);
        /* an example of setting up list view
        Bid testBid = new Bid(testUser, 3.14, "test");
        bidList.add(testBid);
        ListView listView = (ListView) findViewById(R.id.BidList);
        BidListAdapter bidAdapter = new BidListAdapter(this, bidList);
        listView.setAdapter(bidAdapter);*/
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

    private void postTask(View view) {

    }

    private void editTask(View view) {

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
