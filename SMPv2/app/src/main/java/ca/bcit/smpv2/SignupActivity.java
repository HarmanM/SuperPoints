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
import android.widget.RadioButton;
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

public class SignupActivity extends AppCompatActivity {

    private EditText usernameField, passwordField, passwordConfirmField;
    private RadioButton businessRB;
    private RadioButton userRB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameField = (EditText) findViewById(R.id.usernameEditText);
        passwordField = (EditText) findViewById(R.id.passwordEditText);
        passwordConfirmField = (EditText) findViewById(R.id.confirmEditText);
        businessRB = findViewById(R.id.businessRB);
        userRB = findViewById(R.id.userRB);
        userRB.setChecked(true);

    }


    public void register(View view) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = passwordConfirmField.getText().toString();
        if (confirmPassword.equals(password)) {
            if (businessRB.isChecked()) {
                Intent i = new Intent(this, BusinessSignup.class);
                i.putExtra("USERNAME", username);
                i.putExtra("PASSWORD", password);
                startActivity(i);
            }
            else {
                User u = new User(-1, -1, password, username);


                new DatabaseObj (SignupActivity.this).setUser(u, new Consumer<ArrayList<Object>>() {
                    @Override
                    public void accept(ArrayList<Object> objects) {

                        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                });
            }
        } else {
            Toast.makeText(this, "The passwords do not match, please check again", Toast.LENGTH_SHORT).show();
        }
    }
}
