package cmput301w18t09.orbid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class MyBid extends NavigationActivity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout the XML that goes to the corresponding child that is being inflated
        // Then setup the generic parts.
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(R.layout.activity_task_details, frameLayout);

        // Use the id of the task to get it from the Data Manager
        id = getIntent().getStringExtra("_id");
    }
}
