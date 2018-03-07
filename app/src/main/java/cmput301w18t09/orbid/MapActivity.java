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

        Bundle bundle = getArguments();
        String came_from = bundle.getString("type");
        ArrayList<String> query = new ArrayList<>();

        // If we came from recent_listings
        if (came_from.equals("recent_listings")) {
            query.add("_type");
            query.add("task");
        }
        // If we came from a single ad - * id needs to be passed in arguments *
        else if (came_from.equals("single_ad")) {
            String id = bundle.getString("id");
            query.add("_id");
            query.add(id);
        }
        // If we are looking at a users ads - * username needs to be passed in arguments *
        else {
            String username = bundle.getString("username");
            query.add("username");
            query.add(username);
        }

        DataManager.getTasks getTasks = new DataManager.getTasks(getActivity());
        getTasks.execute(query);
        try {
            if (!taskList.isEmpty()) {
                taskList = getTasks.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Todo place markers
        for (Task task : taskList) {
            if (task.getLocation() != null) {
                mMap.addMarker(new MarkerOptions().position(task.getLocation()).title(task.getTitle()));
            }
        }
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
