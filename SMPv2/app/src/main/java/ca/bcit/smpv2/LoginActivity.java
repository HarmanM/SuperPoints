package ca.bcit.smpv2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    //TODO does this need to be destroyed
    static User user;

    private EditText usernameField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usernameField = (EditText) findViewById(R.id.cityEditText);
        passwordField = (EditText) findViewById(R.id.postalEditText);

    }

    public void login(View view) {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString();

        new DatabaseObj(this).getUsers("userName='" + username + "' AND password='" + password + "'", (ArrayList<Object> objects)->{
            if(objects.size() == 1){
                user = (User) objects.get(0);
                new DatabaseObj(this).getUserSettings("userID=" + user.getUserID(), (ArrayList<Object> settings)->{
                    for(Object setting : settings)
                        user.addSetting((UserSetting) setting);
                    Intent i;
                    if (user.getBusinessID() != -1) {
                        i = new Intent(this, BusinessDashboard.class);
                    } else if (user.getUserID() == 0) {
                        i = new Intent(this, AdministratorDashboard.class);
                    } else {
                        i = new Intent(this, MapsActivity.class);
                    }
                    i.putExtra("username", username);
                    this.startActivity(i);

                    usernameField.setText("");
                    passwordField.setText("");
                });
            } else {
                Toast.makeText(this, "Please check your credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static public void logout(){
        user = null;
    }
}
