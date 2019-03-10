package ca.bcit.smpv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField,passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameField = (EditText)findViewById(R.id.cityEditText);
        passwordField = (EditText)findViewById(R.id.passwordEditText);

    }


    public void login(View view) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        new SigninTask(this, 0).execute(username, password);
    }
}
