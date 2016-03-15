package com.regis.darren.mytrips;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private final String ADMIN_USERNAME = "";
    private final String ADMIN_PASSWORD = "";

    private EditText usernameField;
    private EditText passwordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
    }

    public void onSubmit(View view) {

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if(username.compareTo(ADMIN_USERNAME)==0 && password.compareTo(ADMIN_PASSWORD)==0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
