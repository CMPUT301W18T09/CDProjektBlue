package cmput301w18t09.orbid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditProfileActivity extends NavigationActivity {

    private Button btnSave;
    private TextView tvUsername;
    private EditText etPhoneNumber;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout
        int layoutID = getIntent().getIntExtra("edit_profile_layout_id", 0);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(layoutID, frameLayout);

        // Assign the edit text boxes of the layout
        tvUsername = findViewById(R.id.edit_profile_tvUsername);
        etEmail = findViewById(R.id.edit_profile_etEmail);
        etPhoneNumber = findViewById(R.id.edit_profile_etPhoneNumber);
        etFirstName = findViewById(R.id.edit_profile_etFirstName);
        etLastName = findViewById(R.id.edit_profile_etLastName);

        // Get the currently logged in user
        String username = LoginActivity.getCurrentUsername();
        DataManager.getUsers getUsers = new DataManager.getUsers();
        ArrayList<String> queryParameters = new ArrayList<>();
        ArrayList<User> returnUsers;
        queryParameters.add("username");
        queryParameters.add(username);
        getUsers.execute(queryParameters);
        try {
            returnUsers = getUsers.get();
            currentUser = returnUsers.get(0);
        }
        catch (Exception e) {
            Log.e("Error", "Failed to get ArrayList intended as return from getUsers");
            e.printStackTrace();
            Toast.makeText(this, "Error getting user information", Toast.LENGTH_SHORT).show();
            return;
        }

        // Populate the text fields with user information
        tvUsername.setText(currentUser.getUsername());
        etEmail.setText(currentUser.getEmail());
        etPhoneNumber.setText(currentUser.getPhoneNumber());
        etFirstName.setText(currentUser.getFirstName());
        etLastName.setText(currentUser.getLastName());

        // Assign on click listener to save profile button
        btnSave = findViewById(R.id.edit_profile_btnSave);
        btnSave.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });


    }

    private void save()
    {
        User user;

        DataManager.updateUsers updateUsers = new DataManager.updateUsers();
        ArrayList<User> queryParameters = new ArrayList<>();

        String username = tvUsername.getText().toString();
        String email = etEmail.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();

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
        if (phoneNumber.length() == 0) {
            Toast.makeText(this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check phone number does not exceed 10 digits
        if (phoneNumber.length() > 10) {
            Toast.makeText(this, "Phone number cannot exceed 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check phone number format is acceptable
        if (!containsOnlyNumbers(phoneNumber)) {
            Toast.makeText(this, "Phone number may contain only digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the new user to the server
        user = new User(username, email, phoneNumber, firstName, lastName);
        //Log.v("Current User ID:", currentUser.getID());
        user.setID(currentUser.getID());


        //
        queryParameters.add(user);
        updateUsers.execute(queryParameters);

        // If all goes well, tell the user
        Toast.makeText(this, "Save successful", Toast.LENGTH_LONG).show();
        return;

    }

    // Taken from user Jean-Charles on Monday, March 5th, 2018.
    // https://stackoverflow.com/questions/7607260/check-non-numeric-characters-in-string
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
