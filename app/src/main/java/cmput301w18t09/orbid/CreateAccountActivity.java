package cmput301w18t09.orbid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Allows a user register an account with the server to use the application
 *
 * @author Zach Redfern
 * @see User
 */
public class CreateAccountActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private EditText etFirstName;
    private EditText etPassword;
    private EditText etLastName;
    private Button btnCreateAccount;

    /**
     * Method is ran when the create account activity is first created to instantiate its elements.
     *
     * @param savedInstanceState Any information that needs to be passed to this activity instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Assign edit text boxes
        etUsername = findViewById(R.id.create_account_etUsername);
        etEmail = findViewById(R.id.edit_profile_etEmail);
        etPhoneNumber = findViewById(R.id.edit_profile_etPhoneNumber);
        etFirstName = findViewById(R.id.edit_profile_etFirstName);
        etLastName = findViewById(R.id.edit_profile_etLastName);
        etPassword = findViewById(R.id.edit_profile_etPassword);

        // Assign on click listener to create account button
        btnCreateAccount = findViewById(R.id.create_account_btnCreate);
        btnCreateAccount.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                createAccount();
            }

        });
    }

    /**
     * Attempt to add a new user to the server after error checking user information.
     *
     * @see User
     */
    private void createAccount()
    {
        User user;

        // Set up the data manager
        DataManager.getUsers getUsers = new DataManager.getUsers(this);
        DataManager.addUsers addUsers = new DataManager.addUsers(this);
        ArrayList<String> queryParameters = new ArrayList<>();
        ArrayList<User> returnUsers;

        // Get currently populated information from the activity
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String password = etPassword.getText().toString();

        // Ensure username is not yet taken
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


        // Check if the PW length is 0 or > 30
        if (password.length() == 0  || password.length() > 30) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure password has at least one character
        if (StringUtils.isNumeric(password)) {
            Toast.makeText(this, "Password must contain at least one character", Toast.LENGTH_SHORT).show();
            return;
        }

        // If the user name was taken, tell the user
        if (!returnUsers.isEmpty()) {
            Toast.makeText(this, "That user name already exists.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check username length is not zero
        if (username.length() == 0) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check username length does not exceed the maximum
        if (username.length() > 15) {
            Toast.makeText(this, "Username cannot exceed 15 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check first name length is not zero
        if (firstName.length() == 0) {
            Toast.makeText(this, "First name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check last name length is not zero
        if (lastName.length() == 0) {
            Toast.makeText(this, "Last name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check e-mail length is not zero
        if (email.length() == 0) {
            Toast.makeText(this, "E-mail cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check e-mail format is acceptable
        if (email.indexOf('@') == -1 || email.indexOf('@') == 0 || email.indexOf('@') == email.length() - 1) {
            Toast.makeText(this, "Correct e-mail format: example@example.com", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check phone number is not empty
        if (phoneNumber.length() != 10) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check phone number format is acceptable
        if (!containsOnlyNumbers(phoneNumber)) {
            Toast.makeText(this, "Phone number may contain only digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the new user to the server
        user = new User(username, password, email, phoneNumber, firstName, lastName);
        addUsers.execute(user);

        // If all goes well, then re-open the login activity
        openLoginActivity();
    }

    /**
     * Opens the login activity.
     */
    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
    }

    /**
     * Checks an input string to ensure it contains only digits/numbers.
     *
     * Taken from user Jean-Charles on Monday, March 5th, 2018.
     * https://stackoverflow.com/questions/7607260/check-non-numeric-characters-in-string
     *
     * @param str The input string to be checked
     * @return True if input string contained only numbers, false otherwise
     */
    public boolean containsOnlyNumbers(String str) {

        //It can't contain only numbers if it's null or empty...
        if (str == null || str.length() == 0)
            return false;

        for (int i = 0; i < str.length(); i++) {

            //If we find a non-digit character we return false.
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }

        return true;
    }
}
