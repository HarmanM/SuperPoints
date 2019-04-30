package ca.bcit.smpv2;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static android.app.NotificationManager.IMPORTANCE_HIGH;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {


    public static final String TAG = MapsActivity.class.getSimpleName();
    public static int notifSent = 0;

    //Notification related variables
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static int NOTIFICATION_ID = 0;

    //Map related variables
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private float defaultZoom = 16.0f;
    private int locationRequestInterval = 15; //in seconds, how often maps will update
    int MY_PERMISSION_ACCESS_FINE_LOCATION = 100; //???? why is it a random int, reason its not private?

    static final ArrayList<Business> businessesNearby = new ArrayList<>();

    //Preferred business variables
    FloatingActionButton preferBusinessButton;
    Map<LatLng, BusinessMapMarker> markerExtras;

    BeaconRanger br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        br = new BeaconRanger(this);
        preferBusinessButton = findViewById(R.id.prefer_business_button);
        markerExtras = new HashMap<>();

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_person_black_18dp));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //API client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) // may need to change we dont need incredibly accuracy for promotions
                .setInterval(locationRequestInterval * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        br.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.i(TAG, "Location not found.");
        }
        else
        {
            Log.i(TAG, "Location was found");
            handleNewLocation(location);
            if(notifSent == 0)
                generateBusinessesNearby(location);
        }
        Log.i(TAG, "Location services connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_map_resize_p));
        mMap.addMarker(options);
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));
    }

    @Override
    public void onLocationChanged(Location location) {
        //TODO check if this works
        if(!businessesNearby.isEmpty())
        {
            Intent intent = new Intent(this, MapsActivity.class);
            String title = "SuperPoints are up for grabs near you!";
            String message = "Check it out!";
            mMap.clear();
            //TODO redraw person
            generateBusinessMarkers(businessesNearby);
        }
        handleNewLocation(location);
        Log.i(TAG, "LOCATION CHANGED.");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    //TODO make this into a class
    // action to open maps activity
    static public void showNotification(String title, String text, PendingIntent pendingIntent, Context callingContext) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(callingContext, "MyChannel")
                .setSmallIcon(R.drawable.aptimg)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) callingContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel_Name";
            String description = "Channel_Description";
            NotificationChannel channel = new NotificationChannel("MyChannel", name, IMPORTANCE_HIGH);
            channel.setDescription(description);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            if (channel != null) {
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(++NOTIFICATION_ID, builder.build());
            }
        }
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                //Intent h = new Intent(getBaseContext(), MapsActivity.class);
                //startActivity(h);
                return true;
            case R.id.dashboard:
                Intent i = new Intent(getBaseContext(), DashboardActivity.class);
                startActivity(i);
                return true;

            case R.id.settings:
                Intent k = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(k);
                return true;
            case R.id.profile:
                Intent j = new Intent(getBaseContext(), ProfileActivity.class);
                startActivity(j);
                return true;
            case R.id.viewPoints:
                Intent l = new Intent(getBaseContext(), UserPointsActivity.class);
                startActivity(l);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void generateBusinessesNearby (Location location) {
        notifSent++;
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        new DatabaseObj (MapsActivity.this).getBusinessesNearby("lat=" + lat + "%20" + "long=" + lon
                , (ArrayList<Object> objects)->{
                for(Object o: objects)
                    businessesNearby.add((Business) o);
                if(!businessesNearby.isEmpty())
                {
                    Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MapsActivity.this, 0, intent, 0);

                    showNotification("SuperPoints", "SuperPoints are up for grabs near you!", pendingIntent, MapsActivity.this);
                    generateBusinessMarkers(businessesNearby);
                    onLocationChanged(location);
                }
        });
    }

    public void generateBusinessMarkers(ArrayList<Business> BusinessesNearby) {
        int BusinessID;
        String BusinessName;
        String BusinessAddress;
        double BusinessLatitude;
        double BusinessLongitude;
        Marker marker;
        MarkerOptions options;
        Geocoder geocoder;
        List<Address> addresses;
        //TODO Businesses nearby needs to tell me if its preferred

        for (int i = 0; i < BusinessesNearby.size(); i++) {
            BusinessID = BusinessesNearby.get(i).getBusinessID();
            BusinessLatitude = BusinessesNearby.get(i).getLatitude();
            BusinessLongitude = BusinessesNearby.get(i).getLongitude();
            BusinessName = BusinessesNearby.get(i).getBusinessName();
            BusinessAddress = "";
            LatLng latLng = new LatLng(BusinessLatitude, BusinessLongitude);

            options = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(BusinessName);

            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(BusinessLatitude, BusinessLongitude, 1);
                if (addresses.size() > 0 && addresses != null) {
                    String SubThoroughFare = addresses.get(0).getSubThoroughfare();
                    String ThoroughFare = addresses.get(0).getThoroughfare();
                    BusinessAddress += ((SubThoroughFare == null) ? "" : SubThoroughFare + ", ");
                    BusinessAddress = ((ThoroughFare == null) ? BusinessAddress : BusinessAddress + ThoroughFare);
                    options.snippet(BusinessAddress);
                }
            } catch (IOException e) {
                Log.e("EXCEPTION: GEOCODER MAPSACTIVITY GENERATEBUSINESS MARKERS", e.toString());
            }

            marker = mMap.addMarker(options);
            //TODO preferred query to check if preferred
            markerExtras.put(latLng, new BusinessMapMarker(BusinessesNearby.get(i), marker, options, false));
            marker.setVisible(true);
        }
    }

    public boolean onMarkerClick(final Marker marker) {
        LatLng oldMarkerLatLng = marker.getPosition();
        Boolean preferred = markerExtras.get(oldMarkerLatLng).isPreferred();

        marker.showInfoWindow();
        if(preferred)
            preferBusinessButton.setImageResource(R.drawable.unprefer_business);

        preferBusinessButton.setVisibility(View.VISIBLE);
        preferBusinessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng oldMarkerLatLng = marker.getPosition();
                Boolean preferred = markerExtras.get(oldMarkerLatLng).isPreferred();
                Marker oldMarker = markerExtras.get(oldMarkerLatLng).getMarker();
                oldMarker.remove();
                MarkerOptions oldMarkerOptions = markerExtras.get(oldMarkerLatLng).getOptions();
                if(!preferred)
                {
                    oldMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.preferred_business_icon_resized));
                    preferBusinessButton.setImageResource(R.drawable.unprefer_business);
                }
                else
                {
                    oldMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    preferBusinessButton.setImageResource(R.drawable.prefer_business_icon);
                }
                BusinessMapMarker updated = markerExtras.get(oldMarkerLatLng);
                updated.setPreferred(!preferred);
                Marker newMarker = mMap.addMarker(oldMarkerOptions);
                updated.setMarker(newMarker);
                markerExtras.put(oldMarkerLatLng, updated);
                newMarker.showInfoWindow();
            }
        });
        return true;
    }
}
