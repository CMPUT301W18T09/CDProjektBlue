package cmput301w18t09.orbid;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class LoginActivity extends AppCompatActivity{

    private Button btnLogin;
    private Button btnCreateAccount;
    private EditText etUsername;
    private static String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.login_etUsername);

        // Assign on click listener to the sign in button
        btnLogin = findViewById(R.id.login_btnSignIn);
        btnLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
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

    private void openRecentListingsActivity()
    {

        DataManager.getUsers getUsers = new DataManager.getUsers(this);
        ArrayList<String> queryParameters = new ArrayList<>();
        ArrayList<User> returnUsers;

        // Ensure username exists
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

        Intent intent = new Intent(this, RecentListingsActivity.class);
        intent.putExtra("recent_listings_layout_id", R.layout.activity_recent_listings);
        currentUsername = etUsername.getText().toString();
        this.startActivity(intent);

    }

    private void openCreateAccountActivity()
    {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        this.startActivity(intent);
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void onBackPressed() {

    }
}
