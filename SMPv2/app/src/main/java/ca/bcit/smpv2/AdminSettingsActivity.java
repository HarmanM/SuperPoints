package ca.bcit.smpv2;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AdminSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((CheckBox)findViewById(R.id.monthlyKPI)).setChecked(Boolean.valueOf(LoginActivity.user.getSetting(2).getValue()));
    }

    public void updateSettings(View view) {
        EditText pw = findViewById(R.id.passwordEditText);
        EditText cpw = findViewById(R.id.confirmEditText);
        if(!pw.getText().toString().trim().isEmpty() || !cpw.getText().toString().trim().isEmpty()) {
            if (pw.getText().toString().compareTo(cpw.getText().toString()) == 0) {
                LoginActivity.user.setPassword(pw.getText().toString());
                new DatabaseObj(this)
                        .updatePassword(LoginActivity.user.getUserID(), pw.getText().toString());
                pw.setText("");
                cpw.setText("");
                Toast.makeText(this, "Password Updated", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Ensure the two passwords match and are not empty", Toast.LENGTH_LONG).show();
            }
        }

        CheckBox monthlyKPI = findViewById(R.id.monthlyKPI);
        if(monthlyKPI.isChecked() != Boolean.valueOf(LoginActivity.user.getSetting(2).getValue())){
            LoginActivity.user.getSetting(2).setValue(Boolean.toString(monthlyKPI.isChecked()));
            new DatabaseObj(this).setUserSetting(LoginActivity.user.getSetting(2));
            Toast.makeText(this, "Monthly KPI Updated", Toast.LENGTH_LONG).show();
        }

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
                Intent i = new Intent(getBaseContext(), AdminDashboardActivity.class);
                startActivity(i);
                return true;
            case R.id.settings:
//                Intent i = new Intent(getBaseContext(), BusinessSettingsActivity.class);
//                startActivity(i);
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
}
