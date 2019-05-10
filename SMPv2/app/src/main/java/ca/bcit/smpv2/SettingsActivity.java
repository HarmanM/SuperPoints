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

public class SettingsActivity extends AppCompatActivity {

    //Seekbar variables
    private SeekBar seekBarPrivacy;
    private int privacyStep = 50; // 3 levels of user privacy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_person_black_18dp));

        seekBarPrivacy = findViewById(R.id.seekBarPrivacy);
        seekBarPrivacy.setProgress(Integer.parseInt(LoginActivity.user.getSetting(1).getValue()) * privacyStep);
        seekBarPrivacy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = (Math.round(progress/privacyStep)) * privacyStep;
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    int privacySetting = seekBarPrivacy.getProgress();
                    int userID = LoginActivity.user.getUserID();
                    if (userID != 0) {
                        LoginActivity.user.getSetting(1).setValue(Integer.toString(privacySetting/privacyStep));
                        new DatabaseObj (SettingsActivity.this).setUserSetting(LoginActivity.user.getSetting(1));
                    }
                } catch (Exception e) {
                    Log.i("SEEKBAR SETTINGS ACTIVITY:", e.toString());
                }
            }
        });


    }

    public void updateSettings(View view) {
        EditText pw = findViewById(R.id.passwordEditText);
        EditText cpw = findViewById(R.id.confirmEditText);
        if(pw.getText().toString().compareTo(cpw.getText().toString()) == 0 && pw.getText().toString().trim() != ""){
            LoginActivity.user.setPassword(pw.getText().toString());
            new DatabaseObj (SettingsActivity.this)
                    .updatePassword(LoginActivity.user.getUserID(), pw.getText().toString());
            pw.setText("");
            cpw.setText("");
            Toast.makeText(this, "Password Updated", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Ensure the two passwords match and are not empty", Toast.LENGTH_LONG).show();
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
        if(item.getItemId() != R.id.settings)
            finish();
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
                //Intent k = new Intent(getBaseContext(), SettingsActivity.class);
                //startActivity(k);
                return true;
            case R.id.profile:
                Intent j = new Intent(getBaseContext(), ProfileActivity.class);
                startActivity(j);
                return true;
            case R.id.viewPoints:
                Intent p = new Intent(getBaseContext(), UserPointsActivity.class);
                startActivity(p);
                return true;
            case R.id.logOut:
                Intent m = new Intent(this, MapsActivity.class);
                m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(m);
                Intent l = new Intent(getBaseContext(), LandingActivity.class);
                l.putExtra("logout", true);
                startActivity(l);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
