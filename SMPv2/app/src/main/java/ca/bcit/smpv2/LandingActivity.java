package ca.bcit.smpv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amazonaws.mobile.client.AWSMobileClient;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        //Intent i = new Intent(getBaseContext(), MainActivity.class);
        //startActivity(i);
        AWSMobileClient.getInstance().initialize(this).execute();
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
