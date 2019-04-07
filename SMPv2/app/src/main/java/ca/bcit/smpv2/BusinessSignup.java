package ca.bcit.smpv2;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BusinessSignup extends AppCompatActivity {

    private String username, password, businessName, businessAddress;
    private EditText businessEditText, addressEditText;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_business_signup);
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        username = i.getStringExtra("USERNAME");
        password = i.getStringExtra("PASSWORD");
    }

    public void businessRegister(View view) throws IOException {
        businessEditText = findViewById(R.id.businessNameEditText);
        businessName = businessEditText.getText().toString();
        addressEditText = findViewById(R.id.businessAddressEditText);
        businessAddress = addressEditText.getText().toString();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(businessAddress, 1);
        Address address = addresses.get(0);
        longitude = address.getLongitude();
        latitude = address.getLatitude();

        Business b = new Business(0, businessName, latitude, longitude, "");
        User u = new User(0, 0, password, username, 3);

        new DatabaseObj (BusinessSignup.this).setBusiness(b, new Consumer<ArrayList<Object>>() {
            @Override
            public void accept(ArrayList<Object> objects) {
                String result = (String) objects.get(0);
                Log.i("666BID", result);
            }
        });

        new DatabaseObj (BusinessSignup.this).setUser(u, new Consumer<ArrayList<Object>>() {
            @Override
            public void accept(ArrayList<Object> objects) {
                String result = (String) objects.get(0);
                Log.i("2222UID", result);
            }
        });
    }



}
