package ca.bcit.smpv2;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.widget.ListView;

import java.util.ArrayList;

public class UserPointsActivity extends AppCompatActivity {

    ArrayList<Pair<Business, Points>> userPoints;

}

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_points);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_person_black_18dp));

        // Construct the data source, maybe construct arraylist beforehand
        userPoints = new ArrayList<Pair<Business, Points>>();
        final PointsAdapter adapter = new PointsAdapter(this, userPoints);

        ListView listView = (ListView) findViewById(R.id.lvPromotions);
}
