package cmput301w18t09.orbid;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Task> taskList;
    private ToggleButton tbtnToggle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getContext(), TaskDetailsActivity.class);
                Log.i("TEST OLD TEST", marker.getId());
                intent.putExtra("task_details_layout_id", R.layout.activity_task_details);
                intent.putExtra("id", marker.getId());
                startActivity(intent);
                return false;
            }
        });

        User user = new User("NAN", "nan@gmail.com", "1", "NAN", "THE MAN");
        Task task = new Task(user, "SOME TASK", "TESTING TASK", 10, Task.TaskStatus.BIDDED);

        // Add a marker in Sydney and move the camera
        LatLng task_location = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(task_location).title(task.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(task_location));
    }

    private void displayAllListings()
    {

    }

    private void displaySingleListing()
    {

    }

    private void openRecentListingsActivity()
    {

    }
}
