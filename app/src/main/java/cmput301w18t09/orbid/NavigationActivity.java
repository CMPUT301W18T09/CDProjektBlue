package cmput301w18t09.orbid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

/**
 * The navigation drawer that is seen as the main menu across most activities for ease of use --
 * a superclass to all other activities that feature the menu. Allows flow between application
 * activities.
 *
 * @author Zach Redfern, Chady Haidar, Aidan Kosik
 * @see AddEditTaskActivity
 * @see EditProfileActivity
 * @see ListTaskActivity
 * @see MapActivity
 * @see PlaceBidActivity
 * @see RecentListingsActivity
 * @see TaskDetailsActivity
 */
public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static String thisUser;
    private GoogleApiClient googleApiClient;
    public static Location thisLocation;
    private Context context = this;
    private DataManager.NotificationChecker notificationChecker;
    private int id;

    /**
     * Method is ran when the navigation activity is first created to instantiate its elements.
     *
     * @param savedInstanceState Any information that needs to be passed to this activity instance
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the current user
        try {
            String isLogin = getIntent().getStringExtra("isLogin");
            if (isLogin.equals("true")) {
                thisUser = getIntent().getStringExtra("username");
            }
        } catch(Exception e) {
            // TODO: Handle the exception
        }

        // Instantiate the view elements
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View secondary = navigationView.getHeaderView(0);
        TextView username = secondary.findViewById(R.id.usernameTextView);
        username.setText(thisUser);
        // Use actual ID of layout to inflate it
        int callerLayoutID = getIntent().getIntExtra("layout_id", 0);
        if (callerLayoutID != 0) {
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            FrameLayout frameLayout = findViewById(R.id.navigation_content_frame);
            inflater.inflate(callerLayoutID, frameLayout);
        }
        notificationChecker = new DataManager.NotificationChecker(this);


        // Set up connection to google api for geo location
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();



    }

    /**
     * Closes the navigation drawer when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Inflate the navigation menu; adds items to the action bar if it is present.
     *
     * @param menu The menu and it's items
     * @return Boolean constant true (the menu was inflated)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    /**
     * Handles the action bar item clicks. The action bar will automatically handle clicks
     * on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item The menu item clicked
     * @return Boolean constant true (item was clicked successfully)
     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        return super.onOptionsItemSelected(item);
//    }



    /**
     * Handles the navigation item clicks (like my tasks, logout, etc).
     *
     * @param item The navigation menu item that was clicked
     * @return Boolean constant true (item was clicked successfully)
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (id == R.id.nav_recent_listings) {
                    notificationChecker.setShouldContinue(false);
                    Intent intent = new Intent(context, RecentListingsActivity.class);
                    intent.putExtra("recent_listings_layout_id", R.layout.activity_recent_listings);
                    context.startActivity(intent);
                } else if (id == R.id.nav_my_tasks) {
                    notificationChecker.setShouldContinue(false);
                    Intent intent = new Intent( context, ListTaskActivity.class);
                    intent.putExtra("tasks_layout_id", R.layout.activity_list_requested_tasks);
                    intent.putExtra("isMyBids",0);
                    intent.putExtra("shouldWait",0);
                    context.startActivity(intent);

                } else if (id == R.id.nav_manage) { // TODO: Change name of nav_manage to represent requested tasks
                    notificationChecker.setShouldContinue(false);
                    Intent intent = new Intent( context, ListTaskActivity.class);
                    intent.putExtra("tasks_layout_id", R.layout.activity_list_requested_tasks);
                    intent.putExtra("isMyBids",1);
                    intent.putExtra("shouldWait",0);
                    context.startActivity(intent);

                } else if (id == R.id.nav_edit_profile) {
                    notificationChecker.setShouldContinue(false);
                    Intent intent = new Intent(context, EditProfileActivity.class);
                    intent.putExtra("edit_profile_layout_id", R.layout.activity_edit_profile);
                    context.startActivity(intent);

                } else if (id == R.id.nav_logout) {
                    notificationChecker.setShouldContinue(false);
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    finish();
                }
            }
        }, 330);
        return true;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location myLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (myLocation != null) {
            Log.i("MAP", "Location is: " + myLocation.toString());
            this.thisLocation = myLocation;
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
