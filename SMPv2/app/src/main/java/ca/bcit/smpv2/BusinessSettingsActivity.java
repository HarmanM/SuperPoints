package ca.bcit.smpv2;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class BusinessSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_settings);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_person_black_18dp));

        RadioGroup pointAccumulation = findViewById(R.id.pointOptions);
        if(BusinessDashboard.business.getSetting(0).getValue().equals("duration"))
            pointAccumulation.check(R.id.duration);
        else
            pointAccumulation.check(R.id.visit);
    }

    public void updateSettings(View view) {
        EditText pw = findViewById(R.id.passwordEditText);
        EditText cpw = findViewById(R.id.confirmEditText);
        if(pw.getText().toString().trim() != "" || cpw.getText().toString().trim() != "") {
            if (pw.getText().toString().compareTo(cpw.getText().toString()) == 0) {
                LoginActivity.user.setPassword(pw.getText().toString());
                new DatabaseObj(BusinessSettingsActivity.this)
                        .updatePassword(LoginActivity.user.getUserID(), pw.getText().toString());
            } else {
                Toast.makeText(this, "Ensure the two passwords match and are not empty", Toast.LENGTH_LONG).show();
            }
        }

        RadioGroup pointOptions = findViewById(R.id.pointOptions);
        int selectedOpt = pointOptions.getCheckedRadioButtonId();
        if(selectedOpt == R.id.visit && BusinessDashboard.business.getSetting(0).getValue().equals("duration")){
            BusinessDashboard.business.getSetting(0).setValue("visit");
            new DatabaseObj(this).setBusinessSetting(BusinessDashboard.business.getSetting(0));
        } else if(selectedOpt == R.id.duration && BusinessDashboard.business.getSetting(0).getValue().equals("visit")) {
            BusinessDashboard.business.getSetting(0).setValue("duration");
            new DatabaseObj(this).setBusinessSetting(BusinessDashboard.business.getSetting(0));
        }

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_business, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() != R.id.dashboard)
            finish();
        switch (item.getItemId()) {
            case R.id.dashboard:
                Intent i = new Intent(getBaseContext(), BusinessDashboard.class);
                startActivity(i);
                return true;
            case R.id.analytics:
                Intent j = new Intent(getBaseContext(), Analytics.class);
                startActivity(j);
                return true;
            case R.id.settings:
//                Intent i = new Intent(getBaseContext(), BusinessSettingsActivity.class);
//                startActivity(i);
                return true;
            case R.id.logOut:
                LoginActivity.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
