package com.regis.darren.mytrips;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class TripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Intent intent = getIntent();
        EditText tripNameField = (EditText) findViewById(R.id.tripName);
        EditText startDateField = (EditText) findViewById(R.id.startDate);
        EditText endDateField = (EditText) findViewById(R.id.endDate);
        String tn = intent.getStringExtra("trip name");
        String sd = intent.getStringExtra("start date");
        String ed = intent.getStringExtra("end date");

        tripNameField.setText(tn);
        startDateField.setText(sd);
        endDateField.setText(ed);


    }
}
