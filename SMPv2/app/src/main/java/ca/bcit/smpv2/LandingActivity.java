package ca.bcit.smpv2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        AWSMobileClient.getInstance().initialize(this).execute();

        Intent l = getIntent();
        if(l != null && l.getBooleanExtra("logout", false)) {
            LoginActivity.user = null;
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            editor.remove("username");
            editor.remove("password");
            editor.commit();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_ACCESS_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        if(!username.isEmpty() && !password.isEmpty()) {
            new DatabaseObj(this).getUsers("userName='" + DatabaseObj.SQLSafe(username)
                    + "' AND password='" + DatabaseObj.SQLSafe(password) + "'", (ArrayList<Object> objects) -> {
                if (objects.size() == 1) {
                    LoginActivity.user = (User) objects.get(0);
                    new DatabaseObj(this).getUserSettings("userID=" + LoginActivity.user.getUserID(), (ArrayList<Object> settings) -> {
                        for (Object setting : settings)
                            LoginActivity.user.addSetting((UserSetting) setting);
                        Intent i;
                        if (LoginActivity.user.getBusinessID() != -1) {
                            i = new Intent(this, BusinessDashboard.class);
                        } else if (LoginActivity.user.getUserID() == 0) {
                            i = new Intent(this, AdminDashboardActivity.class);
                        } else {
                            i = new Intent(this, MapsActivity.class);
                        }
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("username", username);
                        this.startActivity(i);
                    });
                } else {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    editor.remove("username");
                    editor.remove("password");
                    editor.commit();
                }
            });
        }
    }

    public void onclickSignUp(View v) {
        Intent i = new Intent(getBaseContext(), SignupActivity.class);
        startActivity(i);
    }

    public void onclickLogin(View v) {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
    }
}
