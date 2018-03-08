package cmput301w18t09.orbid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileActivity extends NavigationActivity {

    private Button btnSave;
    private EditText etPhoneNumber;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: REMOVE, FOR TESTING
        Button buttonOne = (Button) findViewById(R.id.btnTest);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                TextView tv = findViewById(R.id.edit_profile_text_view);
                tv.setText("Orbid!");
            }
        });
    }

    // TODO: REMOVE, FOR TESTING
    private void testBtn() {
        TextView tv = findViewById(R.id.edit_profile_text_view);
        tv.setText("Orbid!");
    }

    private void save()
    {
        
    }
}
