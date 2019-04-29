package ca.bcit.smpv2;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class Analytics extends AppCompatActivity {

    TextView avgVisits;
    TextView avgDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        avgDuration = (TextView) findViewById(R.id.avgDurationResultTextView);
        avgVisits = (TextView) findViewById(R.id.avgVisitsResultTextView);

        new DatabaseObj(Analytics.this).calcAverageDuration(LoginActivity.user.getBusinessID(), (ArrayList<Object> objects) -> {
            avgDuration.setText(String.valueOf(objects.get(0)));
        });

        new DatabaseObj(Analytics.this).calcAverageVisits(LoginActivity.user.getBusinessID(), (ArrayList<Object> objects) -> {
            avgVisits.setText(String.valueOf(objects.get(0)));
        });

        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_person_black_18dp));
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
        if(item.getItemId() != R.id.analytics)
            finish();
        switch (item.getItemId()) {
            case R.id.dashboard:
                Intent i = new Intent(getBaseContext(), BusinessDashboard.class);
                startActivity(i);
                return true;
            case R.id.analytics:
//                Intent j = new Intent(getBaseContext(), Analytics.class);
//                startActivity(j);
//                return true;
            case R.id.settings:
                Intent j = new Intent(getBaseContext(), BusinessSettingsActivity.class);
                startActivity(j);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
