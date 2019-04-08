package ca.bcit.smpv2;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class UserPointsActivity extends AppCompatActivity {

    ArrayList<Pair<Business, Points>> userPoints = new ArrayList<>();
    ListView listView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_points);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_person_black_18dp));

        listView = (ListView) findViewById(R.id.lvUserPoints);

        final PointsAdapter adapter = new PointsAdapter(this, userPoints);
        listView.setAdapter(adapter);

        ArrayList<Points> points = new ArrayList<>();
        ArrayList<Business> businesses = new ArrayList<>();
        new DatabaseObj(this).getPoints("userID=" + LoginActivity.user.getUserID(), (ArrayList<Object> objects)->{
            if(objects.size() > 0) {
                String whereClause = "businessID IN(";
                for (int i = 0; i < objects.size(); ++i) {
                    points.add((Points) objects.get(i));
                    whereClause += ((Points) objects.get(i)).getBusinessID() + ((i == objects.size() - 1) ? "" : ",");
                }
                whereClause += ")";
                new DatabaseObj(this).getBusinesses(whereClause, (ArrayList<Object> busObjects) -> {
                    for (int i = 0; i < busObjects.size(); ++i) {
                        businesses.add((Business) busObjects.get(i));
                        userPoints.add(new Pair<>((Business) busObjects.get(i), points.get(i)));
                    }
                    listView.setAdapter(new PointsAdapter(this, userPoints));
                });
            }

        });
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
        if(item.getItemId() != R.id.viewPoints)
            finish();
        switch (item.getItemId()) {
            case R.id.home:
                //Intent h = new Intent(getBaseContext(), MapsActivity.class);
                //startActivity(h);
                return true;
            case R.id.viewPoints:
                //Intent j = new Intent(getBaseContext(), UserPointsActivity.class);
                //startActivity(j);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}