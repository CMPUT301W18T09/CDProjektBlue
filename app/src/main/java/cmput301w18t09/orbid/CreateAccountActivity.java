package cmput301w18t09.orbid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private EditText etFirstName;
    private EditText etLastName;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    private void createAccount()
    {

    }

    private void openLoginActivity()
    {

    }
}
