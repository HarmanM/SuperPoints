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

import java.util.ArrayList;

public class BusinessBeaconDetailsActivity extends AppCompatActivity {

    TextView businessName, businessAddress;
    EditText businessRegion;
    ListView beaconListView;
    BeaconAdapter beaconAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_beacon_details);

        Bundle extras = getIntent().getExtras();
        Business business = (Business) extras.get("business");
        ArrayList<Beacon> businessesBeacons = (ArrayList<Beacon>) extras.get("businessesBeacons");
        setUpBusinessViews(business);
        setUpBeaconListView(businessesBeacons);
    }

    public void setUpBusinessViews(Business business)
    {
        businessName = (TextView) findViewById(R.id.businessNameTextView);
        businessAddress = (TextView) findViewById(R.id.businessAddressTextView);
        businessRegion = (EditText) findViewById(R.id.businessRegionEditText);

        businessName.setText(business.getBusinessName());
        businessAddress.setText(business.getBusinessAddress(this));
        //businessRegion.setText(business.getRegion());

        businessRegion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                businessRegion.setText(business.getRegion());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                business.setRegion(businessRegion.getText().toString());
                new DatabaseObj(BusinessBeaconDetailsActivity.this).setBusiness(business, null);
            }
        });
    }

    public void setUpBeaconListView (ArrayList<Beacon> businessesBeacons)
    {
        beaconListView = findViewById(R.id.beaconListView);
        beaconAdapter = new BeaconAdapter(this, businessesBeacons);
        beaconListView.setAdapter(beaconAdapter);
    }
}
