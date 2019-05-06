package ca.bcit.smpv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        //beaconAdapter = new BeaconAdapter(this, beaconList);

    }
}
