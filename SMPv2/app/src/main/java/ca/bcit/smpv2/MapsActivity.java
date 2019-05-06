package ca.bcit.smpv2;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    public static final String TAG = MapsActivity.class.getSimpleName();
    public static int notifSent = 0;

    //Map related variables
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private float defaultZoom = 16.0f;
    private int locationRequestInterval = 1500; //in seconds, how often maps will update
    int MY_PERMISSION_ACCESS_FINE_LOCATION = 100;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    static ArrayList<Business> businessesNearby = new ArrayList<>();
    static ArrayList<Business> oldBusinessesNearby = new ArrayList<>();

    //Preferred business variables
    FloatingActionButton preferBusinessButton;
    Map<LatLng, BusinessMapMarker> markerExtras;

    BeaconRanger br;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        br = new BeaconRanger(this);

        //Pref business related
        preferBusinessButton = findViewById(R.id.prefer_business_button);
        markerExtras = new HashMap<>();

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_person_black_18dp));

        //TODO useless?
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //API client for maps
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
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.map_style));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                preferBusinessButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mMap.clear();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else
        {
            handleNewLocation(location);
            generateBusinessesNearby(location);
            //TODO if log out onconnected is called and user is destroyed
            genPrefBusinesses(LoginActivity.user.getUserID());
        }
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
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

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
            case R.id.logOut:
                Intent m = new Intent(this, MapsActivity.class);
                m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(m);
                LoginActivity.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //TODO check if this works
        oldBusinessesNearby = businessesNearby;
        generateBusinessesNearby(location);
        if(compareOldNearbyWithNewNearby(oldBusinessesNearby, businessesNearby))
        {
            genPrefBusinesses(LoginActivity.user.getUserID());
        }
        handleNewLocation(location);
        Log.i(TAG, "LOCATION CHANGED.");
    }

    public void generateBusinessesNearby (Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        new DatabaseObj (MapsActivity.this).getBusinessesNearby("lat=" + lat + "%20" + "long=" + lon
                , (ArrayList<Object> objects)->{
                for(Object o: objects)
                    businessesNearby.add((Business) o);
                if(compareOldNearbyWithNewNearby(oldBusinessesNearby, businessesNearby) && businessesNearby.size() != 0)
                {
                    Intent intent;
                    PendingIntent pendingIntent;
                    intent = new Intent(MapsActivity.this, MapsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    pendingIntent = PendingIntent.getActivity(MapsActivity.this, 0, intent, 0);
                    NotificationHandler.showNotification(getString(R.string.title_activity_maps), getString(R.string.notif_business_nearby), pendingIntent, MapsActivity.this, 2);
                }
        });
    }

    void genPrefBusinesses(int userID)
    {
        ArrayList<Business> preferredBusinesses = new ArrayList<>();

        new DatabaseObj(MapsActivity.this).getPreferredBusinesses("businessID IN (SELECT businessID FROM superpoints.PreferredBusinesses WHERE userID = " + userID + " )",(ArrayList<Object> objects)->{
            for(Object o: objects)
                preferredBusinesses.add((Business) o);
            Map<Business, Boolean> preferredBusinessesNearby = genPrefAndUnprefBusinesses(businessesNearby, preferredBusinesses);
            generateBusinessMarkers(preferredBusinessesNearby);
        });
    }


    public Map<Business, Boolean> genPrefAndUnprefBusinesses(ArrayList<Business> businessesNearby, ArrayList<Business> preferredBusinesses)
    {
        Map<Business, Boolean> allBusinessesNearby = new HashMap<>();

        for(int i = 0; i < businessesNearby.size(); ++i)
        {
            boolean found = false;
            for(int k = 0; k < preferredBusinesses.size(); ++k)
            {
                if(businessesNearby.get(i).getBusinessID() == preferredBusinesses.get(k).getBusinessID())
                {
                    allBusinessesNearby.put(businessesNearby.get(i), true);
                    found = true;
                    break;
                }
            }
            if(!found)
                allBusinessesNearby.put(businessesNearby.get(i), false);
        }
        return allBusinessesNearby;
    }

    public void generateBusinessMarkers(Map<Business, Boolean> prefAndUnpreBusinessesNearby) {
        String BusinessName;
        String BusinessAddress;
        double BusinessLatitude;
        double BusinessLongitude;
        Marker marker;
        MarkerOptions options;
        Geocoder geocoder;
        List<Address> addresses;


        for (Map.Entry<Business, Boolean> pair : prefAndUnpreBusinessesNearby.entrySet()) {
            BusinessLatitude = pair.getKey().getLatitude();
            BusinessLongitude = pair.getKey().getLongitude();
            BusinessName = pair.getKey().getBusinessName();
            BusinessAddress = "";
            LatLng latLng = new LatLng(BusinessLatitude, BusinessLongitude);

            options = new MarkerOptions()
                    .position(latLng)
                    .icon((pair.getValue()) ? BitmapDescriptorFactory.fromResource(R.drawable.preferred_business_icon_resized) : BitmapDescriptorFactory.fromResource(R.drawable.shop_resized))
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
            markerExtras.put(latLng, new BusinessMapMarker(pair.getKey(), marker, options, pair.getValue()));
            marker.setVisible(true);
        }
    }


    public boolean compareOldNearbyWithNewNearby (ArrayList<Business> oldBusinessesNearby, ArrayList<Business> businessesNearby)
    {
        if(oldBusinessesNearby.size() != businessesNearby.size())
            return true;
        for(int i = 0; i < oldBusinessesNearby.size(); ++i)
        {
            if (oldBusinessesNearby.get(i).getBusinessID() != businessesNearby.get(i).getBusinessID())
                return true;
        }
        return false;
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
                MarkerOptions oldMarkerOptions = markerExtras.get(oldMarkerLatLng).getOptions();
                PreferredBusiness prefBusiness = new PreferredBusiness(LoginActivity.user.getUserID(), markerExtras.get(oldMarkerLatLng).getBusiness().getBusinessID());

                oldMarker.remove();
                if(!preferred)
                {
                    oldMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.preferred_business_icon_resized));
                    preferBusinessButton.setImageResource(R.drawable.unprefer_business);
                    new DatabaseObj(MapsActivity.this).setPreferredBusiness(prefBusiness,(ArrayList<Object> objects)->{
                        BusinessMapMarker updated = markerExtras.get(oldMarkerLatLng);
                        updated.setPreferred(!preferred);
                        Marker newMarker = mMap.addMarker(oldMarkerOptions);
                        updated.setMarker(newMarker);
                        markerExtras.put(oldMarkerLatLng, updated);
                        newMarker.showInfoWindow();
                    });
                }
                else
                {
                    oldMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.shop_resized));
                    preferBusinessButton.setImageResource(R.drawable.prefer_business_icon);
                    new DatabaseObj(MapsActivity.this).deletePreferredBusiness(prefBusiness,(ArrayList<Object> objects)->{
                        BusinessMapMarker updated = markerExtras.get(oldMarkerLatLng);
                        updated.setPreferred(!preferred);
                        Marker newMarker = mMap.addMarker(oldMarkerOptions);
                        updated.setMarker(newMarker);
                        markerExtras.put(oldMarkerLatLng, updated);
                        newMarker.showInfoWindow();
                    });
                }
            }
        });
        return true;
    }
}
