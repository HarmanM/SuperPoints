package ca.bcit.smpv2;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.widget.ListView;

import java.util.ArrayList;

public class UserPointsActivity extends AppCompatActivity {

    ArrayList<Pair<Business, Points>> userPoints = new ArrayList<>();
    ListView listView = (ListView) findViewById(R.id.lvPromotions);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_points);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_person_black_18dp));


        final PointsAdapter adapter = new PointsAdapter(this, userPoints);
        listView.setAdapter(adapter);

        ArrayList<Points> points = new ArrayList<>();
        ArrayList<Business> businesses = new ArrayList<>();
        new DatabaseObj(this).getPoints("userID=" + LoginActivity.user.getUserID(), (ArrayList<Object> objects)->{
            String whereClause = "businessID IN (";
            for(int i = 0; i < objects.size(); ++i) {
                points.add((Points) objects.get(i));
                whereClause += ((Points)objects.get(i)).getBusinessID() + ((i == objects.size() - 1) ? "" : ",");
            }
            whereClause += ")";
            new DatabaseObj(this).getBusinesses(whereClause, (ArrayList<Object> busObjects)->{
                for(int i = 0; i < busObjects.size(); ++i) {
                    businesses.add((Business) busObjects.get(i));
                    userPoints.add(new Pair<>((Business) busObjects.get(i), points.get(i)));
                }
                listView.setAdapter(new PointsAdapter(this, userPoints));
            });

        });

        // Construct the data source, maybe construct arraylist beforehand
    }
}