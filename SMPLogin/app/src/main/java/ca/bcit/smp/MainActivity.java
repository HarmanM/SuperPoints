package ca.bcit.smp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText usernameField,passwordField;
    private TextView status,role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameField = (EditText)findViewById(R.id.usernameEditText);
        passwordField = (EditText)findViewById(R.id.passwordEditText);

        status = (TextView)findViewById(R.id.textView6);
        role = (TextView)findViewById(R.id.textView7);
    }


    public void login(View view) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        new SigninActivity(this, status, role, 0).execute(username, password);
    }
}
