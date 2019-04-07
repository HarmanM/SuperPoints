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
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        new SigninTask(this, 0).execute(username, password);
    }

    public class SigninTask extends AsyncTask {

        private Context context;

        private int byGetOrPost = 0;

        private String username;

        //flag 0 means get and 1 means post.(By default it is get.)
        public SigninTask(Context context, int flag) {
            this.context = context;
            byGetOrPost = flag;
        }


        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Object[] arg0) {
            if (byGetOrPost == 0) { //means by Get Method

                try {
                    username = (String) arg0[0];
                    String password = (String) arg0[1];
                    String link = "http://ec2-99-79-49-31.ca-central-1.compute.amazonaws.com/scripts.php?function=login&username="
                            + username + "&password=" + password;
                    URL url = new URL(link);
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    request.setURI(new URI(link));
                    HttpResponse response = client.execute(request);
                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(response.getEntity().getContent()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            } else {
                try {
                    String username = (String) arg0[0];
                    String password = (String) arg0[1];
                    // TODO: Can add POST option instead of GET to call PHP
                    String link = "";
                    String data = URLEncoder.encode("username", "UTF-8") + "=" +
                            URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                            URLEncoder.encode(password, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        @Override
        protected void onPostExecute(Object obj) {
            String strObject = (String) obj;
            if (!strObject.trim().isEmpty()) {
                String objArr[] = strObject.split("~s", 0);
                new DatabaseObj (LoginActivity.this).getUsers("USERID=" + objArr[0], new Consumer<ArrayList<Object>>() {
                    @Override
                    public void accept(ArrayList<Object> objects) {
                        user = (User) objects.get(0);
                        Log.i("2222UID", Integer.toString(user.getUserID()));

                        if (user.getBusinessID() != -1) {
                            Intent i = new Intent(context, BusinessDashboard.class);
                            i.putExtra("username", username);
                            context.startActivity(i);

                        } else if (user.getBusinessID() == -1){
                            Intent i = new Intent(context, MapsActivity.class);
                            i.putExtra("username", username);
                            context.startActivity(i);
                        }
                     else {
                        Toast.makeText(context, "Please check your credentials", Toast.LENGTH_SHORT).show();
                    }
                }});
                //TODO: send intent to proper activity

        }}
    }
}
