package com.regis.darren.mytrips;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class TripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Intent intent = getIntent();
        String item = intent.getStringExtra("trip name");
        Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
    }
}
