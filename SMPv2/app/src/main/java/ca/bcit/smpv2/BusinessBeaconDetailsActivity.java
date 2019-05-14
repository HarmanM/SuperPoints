package ca.bcit.smpv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
    TextView businessName, businessAddress, businessRegionName;
    EditText businessRegion;
    ListView beaconListView;
    BeaconAdapter beaconAdapter;
    private static final String API_KEY = "oFrQVZYzoDiXHMIwIjQzxSJXTDsmbvHO";
    private static final int BEACON_UUID_SIZE = 36;

    boolean delHyphen = false;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDependencies();
        setContentView(R.layout.activity_business_beacon_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        business = (Business) extras.get("business");
        ArrayList<Beacon> businessesBeacons = (ArrayList<Beacon>) extras.get("businessesBeacons");
        setUpBusinessViews(business);
        setUpBeaconListView(businessesBeacons);

        ///

        //scanForDevice("jtGYXm");
    }

    public void setUpBusinessViews(Business business) {

        businessName = (TextView) findViewById(R.id.businessNameTextView);
        businessAddress = (TextView) findViewById(R.id.businessAddressTextView);
        businessRegionName = (TextView) findViewById(R.id.businessRegionTextView);
        businessRegion = (EditText) findViewById(R.id.businessRegionEditText);

        businessName.setText(" " + business.getBusinessName());
        businessAddress.setText(" " + business.getBusinessAddress(this));
        businessRegionName.setText(R.string.business_region);
        if(!business.getRegion().equals("null"))
            businessRegion.setText(business.getRegion());
        else
            businessRegion.setText("");

        //f7826da6-4fa2-4e98-8024-bc5b71e0893e

        TextWatcher tw = new TextWatcher() {
            int originalInputLength = 0;
            int currentInputLength = 0;
            boolean backspaceDetected = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = businessRegion.getSelectionStart();
                currentInputLength = businessRegion.getText().length();
                backspaceDetected = (currentInputLength < originalInputLength);

                businessRegion.removeTextChangedListener(this);

                String str = s.toString();

                int dashes = 0;
                for(int j = 0; j < str.length(); ++j)
                    if(str.charAt(j) == '-')
                        dashes++;

                str = str.replace("-", "");

                StringBuilder strB = new StringBuilder(str);
                if(backspaceDetected) {
                    if (str.length() >= 20) {
                        if(4 - dashes > 0) {
                            strB.delete(strB.length() - (4 - dashes), strB.length());
                            i -= (4 - dashes);
                        }
                    } else if (str.length() >= 16) {
                        if(3 - dashes > 0) {
                            strB.delete(strB.length() - (3 - dashes), strB.length());
                            i -= (3 - dashes);
                        }
                    } else if (str.length() >= 12) {
                        if(2 - dashes > 0) {
                            strB.delete(strB.length() - (2 - dashes), strB.length());
                            i -= (2 - dashes);
                        }
                    } else if (str.length() >= 8) {
                        if(1 - dashes > 0) {
                            strB.delete(strB.length() - (1 - dashes), strB.length());
                            i -= (1 - dashes);
                        }
                    }
                }

                if(strB.length() >= 8){
                    strB.insert(8, "-");
                    if(strB.length() >= 12 + 1){
                        strB.insert(12 + 1, "-");
                        if(strB.length() >= 16 + 2){
                            strB.insert(16 + 2, "-");
                            if(strB.length() >= 20 + 3) {
                                strB.insert(20 + 3, "-");
                            }
                        }
                    }
                }

                if(backspaceDetected)
                    i = i;
                else if(str.length() == 8 || str.length() == 12 || str.length() == 16 || str.length() == 20)
                    ++i;


                businessRegion.setText(strB.toString());
                businessRegion.setSelection(i);
                originalInputLength = strB.length();
                businessRegion.addTextChangedListener(this);
            }
        };

        businessRegion.addTextChangedListener(tw);

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


    public void setUpBeaconListView(ArrayList<Beacon> businessesBeacons) {
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
        String businessRegionName = businessRegionEditText.getText().toString().trim();
        if (!businessRegionName.isEmpty() && businessRegionName.length() == BEACON_UUID_SIZE) {
            business.setRegion(businessRegion.getText().toString());
            new DatabaseObj(BusinessBeaconDetailsActivity.this).setBusiness(business, null);
            Toast.makeText(this, "Business region updated.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Ensure a valid region has been entered", Toast.LENGTH_LONG).show();
        }
    }
}
