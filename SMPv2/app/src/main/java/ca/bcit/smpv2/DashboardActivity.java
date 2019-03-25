package ca.bcit.smpv2;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_person_black_18dp));

        // Construct the data source, maybe construct arraylist beforehand
        ArrayList<Promotions> usersPromotions = new ArrayList<Promotions>();
        final PromotionsAdapter adapter = new PromotionsAdapter(this, usersPromotions);

        ListView listView = (ListView) findViewById(R.id.lvPromotions);
        listView.setAdapter(adapter);

        //TODO The sql script needs to query based on user, need to generate list based on database return script

        new DatabaseObj (DashboardActivity.this).getApplicablePromotions(LoginActivity.user.getUserID(), new Consumer<ArrayList<Object>>() {
            @Override
            public void accept(ArrayList<Object> objects) {
                for(Object o : objects)
                    adapter.add((Promotions) o);
            }
        });

        /*
        Promotions tProm = new Promotions("3 777666555 1 Details 0 BCIT");
        Promotions tProm2 = new Promotions("3 213123123 1 Details2 0 SFUBUSINESSNAME");
        adapter.add(tProm);
        adapter.add(tProm2);*/

        //TODO add items to listView

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home:
                Intent h = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(h);
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
