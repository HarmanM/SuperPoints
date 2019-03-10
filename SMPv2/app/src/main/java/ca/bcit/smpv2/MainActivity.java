package ca.bcit.smpv2;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.google.android.gms.maps.MapView;


public class MainActivity extends AppCompatActivity {

    private MapView mMapView;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mMapView = new MapView(this);
        /* Now delegate the event to the MapView. */
        this.mMapView.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_person_black_18dp));

        Intent i = new Intent(getBaseContext(), LandingActivity.class);
        startActivity(i);


    }

    @Override
    public final void onResume() {
        super.onResume();
        this.getMapView().onResume();
    }

    @Override
    public final void onPause() {
        super.onPause();
        this.getMapView().onPause();
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        this.getMapView().onDestroy();
    }

    @Override
    public final void onSaveInstanceState(final Bundle pSavedInstanceState) {
        super.onSaveInstanceState(pSavedInstanceState);
        this.getMapView().onSaveInstanceState(pSavedInstanceState);
    }

    @Override
    public final void onLowMemory() {
        super.onLowMemory();
        this.getMapView().onLowMemory();
    }

    private final MapView getMapView() {
        return this.mMapView;
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
}
