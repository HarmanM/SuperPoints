package ca.bcit.smpv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;

public class AdministratorDashboard extends AppCompatActivity {

    Button sendKPIBtn;
    ListView beaconListView;
    ArrayList<Beacon> beaconList;
    //BeaconAdapter beaconAdapter;

    User user;
    Boolean sendMonthlyEmail;
    CheckBox kpiCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_dashboard);
        user = LoginActivity.user;
        sendKPIBtn = findViewById(R.id.emailKPIbtn);
        beaconListView = findViewById(R.id.beaconListView);
        kpiCheckbox = findViewById(R.id.kpiCheckbox);
        Log.i("harman", user.getSetting(2).getValue());
        kpiCheckbox.setChecked(Boolean.parseBoolean(user.getSetting(2).getValue()));

//        usersPromotions = new ArrayList<Promotions>();
//        adapter = new PromotionsAdapter(this, usersPromotions);

        beaconList = new ArrayList<Beacon>();


}
