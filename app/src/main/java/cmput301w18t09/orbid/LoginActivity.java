package cmput301w18t09.orbid;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity{

    private Button btnLogin;
    private Button btnCreateAccount;
    private EditText etUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    private void openRecentListingsActivity()
    {

    }

    private void openCreateAccountActivity()
    {

    }
}
