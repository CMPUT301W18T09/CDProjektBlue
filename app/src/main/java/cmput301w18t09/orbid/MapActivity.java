package cmput301w18t09.orbid;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

@SuppressWarnings("ALL")
/**
 * Uses a google maps API to view some area/location of the planet
 *
 * @author Aidan Kosik, Zach Redfern, Google
 */
public class MapActivity extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private ArrayList<Task> taskList = new ArrayList<>();
    private ToggleButton tbtnToggle;
    private GoogleApiClient googleApiClient;
    private Location myLocation = null;


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
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
//        if (myLocation != null) {
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 13));
//
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))      // Sets the center of the map to location user
//                    .zoom(17)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
//                    .build();                   // Creates a CameraPosition from the builder
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getContext(), TaskDetailsActivity.class);
                Log.i("TEST OLD TEST", marker.getId());
                intent.putExtra("task_details_layout_id", R.layout.activity_task_details);
                intent.putExtra("_id", marker.getSnippet());
                startActivity(intent);
                return false;
            }
        });

        // Get the bundle from the previous activity
        Bundle bundle = getArguments();
        String came_from = bundle.getString("came_from");

        // If we came from recent_listings
        if (came_from.equals("recent_listings")) {
            displayAllListings();
        }
        // If we came from a single ad - * id needs to be passed in arguments *
        else if (came_from.equals("single_ad")) {
            String id = bundle.getString("id");
            displaySingleListing(id);
        }
    }

    /**
     * Displays all the tasks on the map. Used when coming from recent
     * listings list view mode.
     */
    private void displayAllListings()
    {
        // Todo
        // Get the task using the query
        DataManager.getTasks getTasks = new DataManager.getTasks(getActivity());
        getTasks.execute(new ArrayList<String>());
        try {
            taskList = getTasks.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Place all of the markers on the map and center on current location
        for (Task task : taskList) {
            if (task.getLocation() != null) {
                Log.i("MAP", "Location " + task.getTitle() + " is " + task.getLocation().toString());
                if (withinDistance(NavigationActivity.thisLocation.getLatitude(), NavigationActivity.thisLocation.getLongitude(),
                        task.getLocation().lat, task.getLocation().lng)) {
                    LatLng latLng = new LatLng(task.getLocation().lat, task.getLocation().lng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(task.getTitle()).snippet(task.getID()));
                }
            }
        }
    }

    /**
     * Checks if a task is within 50km of another.
     *
     * @param lat1 - lat of the origin
     * @param lon1 - lon of the origin
     * @param lat2 - lat of task to check if within
     * @param lon2 - lon of task to check if within
     * @return if the task is within the 50km radius of the origin, true within, false outside of
     */
    private boolean withinDistance(double lat1, double lon1, double lat2, double lon2) {
        double d = acos(sin(lat1)*sin(lat2)+cos(lat1)*cos(lat2)*cos(lon1-lon2));
        double distance_km = 6371 * d;
        Log.i("Distance", "The distance is: " + d);
        return distance_km <= 50;
    }

    /**
     * Displays the map with just one task. Centers the camera on that task.
     *
     * @param id
     */
    private void displaySingleListing(String id)
    {
        // Todo
        // Get the task using the query
        ArrayList<String> query = new ArrayList<>();
        query.add("_id");
        query.add(id);

        DataManager.getTasks getTasks = new DataManager.getTasks(getActivity());
        getTasks.execute(new ArrayList<String>());
        try {
            taskList = getTasks.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Place all of the markers on the map and center on Task
        for (Task task : taskList) {
            if (task.getLocation() != null) {
                LatLng latLng = new LatLng(task.getLocation().lat, task.getLocation().lng);
                mMap.addMarker(new MarkerOptions().position(latLng).title(task.getTitle()).snippet(task.getID()));
            }
        }
    }

    /**
     * opens the recent listings activity in list view mode.
     */
    private void openRecentListingsActivity()
    {
        // Todo
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (location != null) {
            this.myLocation = location;
            Log.i("MAP", "Location is: " + myLocation.toString());
        } else {
            Log.i("MAP", "Location is null:");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("MAP", "There was an error connecting: " + connectionResult);
    }
}
