package cmput301w18t09.orbid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

@SuppressWarnings("ALL")
/**
 * The first screen when the application opens. Prompts user to login or make an account.
 *
 * @author Zach Redfern, Chady Haidar
 */
public class LoginActivity extends AppCompatActivity{

    private Button btnLogin;
    private Button btnCreateAccount;
    private EditText etUsername;
    private static String currentUsername;

    public static ArrayList<Task> backupTasks;
    public static final String backupTasksFile = "backupTasks.sav";

    /**
     * Instantiates the login activity.
     *
     * @param savedInstanceState Any information that needs to be passed to this activity instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.login_etUsername);

        // Assign on click listener to the sign in button
        btnLogin = findViewById(R.id.login_btnSignIn);
        btnLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                findViewById(R.id.loadingPanelLogin).setVisibility(View.VISIBLE);
                openRecentListingsActivity();
            }
        });

        // Assign on click listener to the sign up button
        btnCreateAccount = findViewById(R.id.login_btnSignUp);
        btnCreateAccount.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                openCreateAccountActivity();
            }
        });
    }

    /**
     * Opens the recent listings activity on a successful login attempt.
     *
     * @see RecentListingsActivity
     * @see Task
     */
    private void openRecentListingsActivity() {

        // Tell the user if they cannot login due to being offline
        if (!DataManager.isNetworkAvailable()) {
            Toast.makeText(this, "Cannot login while offline", Toast.LENGTH_LONG).show();
            return;
        }


        // Set up the data manager
        DataManager.getUsers getUsers = new DataManager.getUsers(this);
        ArrayList<String> queryParameters = new ArrayList<>();
        ArrayList<User> returnUsers;


        // Query server to ensure username exists
        queryParameters.add("username");
        queryParameters.add(etUsername.getText().toString());
        getUsers.execute(queryParameters);
        try {
            returnUsers = getUsers.get();
        }
        catch (Exception e) {
            Log.e("Error", "Failed to get ArrayList intended as return from getUsers");
            e.printStackTrace();
            Toast.makeText(this, "Error checking uniqueness of username", Toast.LENGTH_SHORT).show();
            return;
        }

        // If the user name does not exist, tell the user
        if (returnUsers.isEmpty()) {
            Toast.makeText(this, "That user name does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the recent listings
        Intent intent = new Intent(this, RecentListingsActivity.class);
        currentUsername = etUsername.getText().toString();
        intent.putExtra("recent_listings_layout_id", R.layout.activity_recent_listings);
        intent.putExtra("isLogin", "true");
        intent.putExtra("username", currentUsername);

        // Get all tasks related to the user
        DataManager.getTasks getTasks = new DataManager.getTasks(this);
        ArrayList<String> query = new ArrayList<>();
        query.add("and");
        query.add("requester");
        query.add(currentUsername);
        try {
            getTasks.execute(query);
            DataManager.backupTasks = getTasks.get();
        } catch (Exception e) {
            // TODO: Handle
        }


        this.startActivity(intent);
    }

    /**
     * Opens the create account activity to create a new user account
     *
     * @see User
     */
    private void openCreateAccountActivity()
    {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        this.startActivity(intent);
    }

    /**
     *  Gets the name of the currently logged in user
     *
     * @return The name of the currently logged in user
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Overrides the android back button so that a user may not enter back into the
     * application after they (or someone else) has just recently logged out.
     */
    @Override
    public void onBackPressed() {

    }

}
