package ca.bcit.smpv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.BeaconRegion;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.common.KontaktSDK;

import java.util.ArrayList;

public class BusinessBeaconDetailsActivity extends AppCompatActivity {

    Business business;
    TextView businessName, businessAddress;
    EditText businessRegion;
    ListView beaconListView;
    BeaconAdapter beaconAdapter;
    private static final String API_KEY = "oFrQVZYzoDiXHMIwIjQzxSJXTDsmbvHO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDependencies();
        setContentView(R.layout.activity_business_beacon_details);

        Bundle extras = getIntent().getExtras();
        business = (Business) extras.get("business");
        ArrayList<Beacon> businessesBeacons = (ArrayList<Beacon>) extras.get("businessesBeacons");
        setUpBusinessViews(business);
        setUpBeaconListView(businessesBeacons);

        ///

        //scanForDevice("jtGYXm");
    }

    public void setUpBusinessViews(Business business)
    {
        businessName = (TextView) findViewById(R.id.businessNameTextView);
        businessAddress = (TextView) findViewById(R.id.businessAddressTextView);
        businessRegion = (EditText) findViewById(R.id.businessRegionEditText);

        businessName.setText(business.getBusinessName());
        businessAddress.setText(business.getBusinessAddress(this));
        businessRegion.setText(business.getRegion());
    }

    private void initializeDependencies() {
        KontaktSDK.initialize(API_KEY);
    }

    //new DatabaseObj(BusinessBeaconDetailsActivity.this).getBeacons()

    /*//TODO grab beacons for the business
    BeaconRegion region = new BeaconRegion.Builder()
            .identifier("The Office")
            .proximity("f7826da6-4fa2-4e98-8024-bc5b71e0893e")
            .major(12837)
            .build();*/


    public void setUpBeaconListView (ArrayList<Beacon> businessesBeacons)
    {
        beaconListView = findViewById(R.id.beaconListView);
        beaconAdapter = new BeaconAdapter(this, businessesBeacons);
        beaconListView.setAdapter(beaconAdapter);
    }
    /*

    private String targetUniqueId;
    private ProximityManager proximityManager;

    private void scanForDevice(String uniqueId) {
        targetUniqueId = uniqueId;
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
                setStatus("Looking for device...");
            }
        });
    }

    private void setStatus(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusText.setText(text);
            }
        });
    }*/

    public void updateRegion(View view) {
        EditText businessRegionEditText = findViewById(R.id.businessRegionEditText);
        if(!businessRegionEditText.getText().toString().trim().isEmpty())
        {
            business.setRegion(businessRegion.getText().toString());
            new DatabaseObj(BusinessBeaconDetailsActivity.this).setBusiness(business, null);
                Toast.makeText(this, "Business region updated.", Toast.LENGTH_LONG).show();
        } else {
                Toast.makeText(this, "Ensure a region has been entered", Toast.LENGTH_LONG).show();
        }
    }



}
