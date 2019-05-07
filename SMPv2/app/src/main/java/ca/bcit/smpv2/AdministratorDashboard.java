package ca.bcit.smpv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class AdministratorDashboard extends AppCompatActivity {

    Button sendKPIBtn;
    ListView beaconListView;
    ArrayList<Beacon> beaconList;
    //BeaconAdapter beaconAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_dashboard);

        sendKPIBtn = findViewById(R.id.emailKPIbtn);
        beaconListView = findViewById(R.id.beaconListView);

        beaconList = new ArrayList<Beacon>();
        new DatabaseObj(AdministratorDashboard.this).getBeacons("", (ArrayList<Object> settings)->{
            for(Object setting : settings)
                business.addSetting((BusinessSetting) setting);

            setContentView(R.layout.activity_business_dashboard);

            // Find the toolbar view inside the activity layout
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            usersPromotions = new ArrayList<Promotions>();

            listView = (ListView) findViewById(R.id.lvBusinessPromotions);
            listView.setAdapter(adapter);

            int businessID = LoginActivity.user.getBusinessID();
            new DatabaseObj(BusinessDashboard.this).getPromotions("businessID=" + businessID, (ArrayList<Object> objects) -> {
                for (Object o : objects) {
                    usersPromotions.add((Promotions) o);
                }
                adapter = new PromotionsAdapter(this, usersPromotions);
                listView.setAdapter(adapter);
            });)
        //beaconAdapter = new BeaconAdapter(this, beaconList);

    }
}
