package com.regis.darren.mytrips;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.regis.darren.mytrips.domain.Trip;

public class TripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Intent intent = getIntent();
        EditText tripNameField = (EditText) findViewById(R.id.tripName);
        Button startDateField = (Button) findViewById(R.id.startDate);
        Button endDateField = (Button) findViewById(R.id.endDate);

        Trip trip = (Trip) intent.getSerializableExtra("trip");
        tripNameField.setText(trip.getName());
        startDateField.setText(trip.getStartDate());
        endDateField.setText(trip.getEndDate());
    }
}
