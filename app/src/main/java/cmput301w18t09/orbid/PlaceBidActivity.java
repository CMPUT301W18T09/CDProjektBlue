package cmput301w18t09.orbid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class PlaceBidActivity extends TaskDetailsActivity {

    private Button btnBid;
    private EditText etPrice;
    private EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_bid);
    }

    private void makeBid()
    {
        
    }
}
