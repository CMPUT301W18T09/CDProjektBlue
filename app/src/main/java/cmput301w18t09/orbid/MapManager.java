package cmput301w18t09.orbid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * Created by aidankosik on 2018-03-28.
 */

public class MapManager extends NavigationActivity implements PlaceSelectionListener {

    PlaceAutocompleteFragment acSearch;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout ID that was received
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
        inflater.inflate(R.layout.activity_map_manager, frameLayout);
        frameLayout.requestFocus();

        acSearch = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.locationSearchBar);
        acSearch.setOnPlaceSelectedListener(this);

    }


    @Override
    public void onPlaceSelected(Place place) {
        Log.i("MAPMAN", "Place Selected: " + place.getName());
        place.getLatLng();

    }

    @Override
    public void onError(Status status) {
        Log.e("MAPMAN", "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }
}
