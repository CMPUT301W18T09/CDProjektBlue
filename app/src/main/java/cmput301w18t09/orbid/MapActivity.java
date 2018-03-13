package cmput301w18t09.orbid;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ALL")
public class MapActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Task> taskList = new ArrayList<>();
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
     * @param googleMap
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

        // Get the bundle from the previous activity
        Bundle bundle = getArguments();
        String came_from = bundle.getString("type");

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
                mMap.addMarker(new MarkerOptions().position(task.getLocation()).title(task.getTitle()));
            }
        }
    }

    /**
     * Displays the map with just one task. Centers the camera on that task.
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
                mMap.addMarker(new MarkerOptions().position(task.getLocation()).title(task.getTitle()));
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
}
