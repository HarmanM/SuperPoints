package ca.bcit.smp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        userName = (TextView) findViewById(R.id.usrTextView);

        Intent intent = getIntent();
        String intentUserName = intent.getStringExtra("username");

        this.userName.setText(intentUserName);
    }
}
