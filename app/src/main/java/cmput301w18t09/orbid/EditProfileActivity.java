package cmput301w18t09.orbid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class EditProfileActivity extends NavigationActivity {

    private Button btnSave;
    private EditText etPhoneNumber;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_profile);
        super.onCreate(savedInstanceState);
    }

    private void save()
    {
        
    }
}
