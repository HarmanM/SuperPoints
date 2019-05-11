package ca.bcit.smpv2;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class AdminDashboardActivity extends AppCompatActivity {

    Button sendKPIBtn;
    ListView businessListView;
    ArrayList<Beacon> beaconList;
    ArrayList<Business> businessList;
    ArrayList<Business> newBusinessList;
    User user;
    BusinessAdapter adapter;
    BusinessAdapter newBusinessAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        user = LoginActivity.user;

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_person_black_18dp));

        sendKPIBtn = findViewById(R.id.emailKPIbtn);

        setUpSearchView();
        setUpBusinessListView();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() != R.id.dashboard)
            finish();
        switch (item.getItemId()) {
            case R.id.dashboard:
//                Intent i = new Intent(getBaseContext(), AdminDashboardActivity.class);
//                startActivity(i);
                return true;
            case R.id.settings:
                Intent i = new Intent(getBaseContext(), AdminSettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.logOut:
                Intent l = new Intent(getBaseContext(), LandingActivity.class);
                l.putExtra("logout", true);
                startActivity(l);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSendKPIBtnClick(View view) {
        new DatabaseObj(this).sendEmail();
    }

    public void setUpBusinessListView ()
    {
        businessListView = findViewById(R.id.businessListView);
        businessList = new ArrayList<Business>();
        new DatabaseObj(AdminDashboardActivity.this).getBusinesses("", (ArrayList<Object> businesses) ->
        {
            for(Object o : businesses)
                businessList.add((Business) o);
            adapter = new BusinessAdapter(this, businessList);
            businessListView.setAdapter(adapter);
            beaconList = new ArrayList<Beacon>();
            new DatabaseObj(AdminDashboardActivity.this).getBeacons("", (ArrayList<Object> beacons) -> {
                for (Object o : beacons)
                    beaconList.add((Beacon) o);
                businessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(AdminDashboardActivity.this, BusinessBeaconDetailsActivity.class);

                        Business business = businessList.get(position);
                        ArrayList<Beacon> businessesBeacons = getBusinessesBeacons(business.getBusinessID(), beaconList);
                        i.putExtra("business", business);
                        i.putExtra("businessesBeacons", businessesBeacons);
                        startActivity(i);
                    }
                });
            });
        });
    }

    ArrayList<Beacon> getBusinessesBeacons (int businessID, ArrayList<Beacon> allBeacons)
    {
        ArrayList<Beacon> businessesBeacons = new ArrayList<>();
        for(int i = 0; i < allBeacons.size(); ++i)
        {
            if(allBeacons.get(i).getBusinessID() == businessID)
                businessesBeacons.add(allBeacons.get(i));
        }
        return businessesBeacons;
    }

    public void setUpSearchView()
    {
        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //ListView businessListView = findViewById(R.id.businessListView);
                String userInput = s.toLowerCase();
                newBusinessList = new ArrayList<>();
                for (int i = 0; i < businessList.size(); i++) {
                    Business business = businessList.get(i);
                    if (business.getBusinessName().toLowerCase().contains(userInput.trim())
                            || business.getBusinessAddress(AdminDashboardActivity.this).toLowerCase().contains(userInput.trim())) {
                        newBusinessList.add(business);
                    }
                }
                newBusinessAdapter = new BusinessAdapter(AdminDashboardActivity.this, newBusinessList);
                businessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(AdminDashboardActivity.this, BusinessBeaconDetailsActivity.class);

                        Business business = newBusinessList.get(position);
                        ArrayList<Beacon> businessesBeacons = getBusinessesBeacons(business.getBusinessID(), beaconList);
                        i.putExtra("business", business);
                        i.putExtra("businessesBeacons", businessesBeacons);
                        startActivity(i);
                    }
                });
                businessListView.setAdapter(newBusinessAdapter);
                return true;
            }
        });


    }

}
