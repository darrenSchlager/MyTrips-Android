package com.regis.darren.mytrips;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.regis.darren.mytrips.domain.Location;
import com.regis.darren.mytrips.domain.Trip;

import java.util.Calendar;

public class TripActivity extends AppCompatActivity {

    static Trip trip;
    private Context context = null;
    private ListView listView = null;
    private ListAdapter adapter = null;

    static boolean settingStartDate;
    static Button startDateField;
    static Button endDateField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Intent intent = getIntent();
        EditText tripNameField = (EditText) findViewById(R.id.tripName);
        startDateField = (Button) findViewById(R.id.startDate);
        endDateField = (Button) findViewById(R.id.endDate);

        trip = (Trip) intent.getSerializableExtra("trip");
        tripNameField.setText(trip.getName());
        startDateField.setText(trip.getStartDate());
        endDateField.setText(trip.getEndDate());

        initWithLocations();
    }

    private void initWithLocations() {
        context = this;
        listView = (ListView) findViewById(R.id.locationListView);

        adapter = new ArrayAdapter<Location>(context, android.R.layout.simple_list_item_1, trip.getLocations());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location location = (Location) listView.getItemAtPosition(position);
                //Intent intent = new Intent(context, LocationActivity.class);
                //intent.putExtra("location", location);
                //startActivity(intent);
            }
        });
    }

    public void selectStartDate(View view) {
        settingStartDate = true;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void selectEndDate(View view) {
        settingStartDate = false;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String[] match, matchOther;
            int month, day, year;
            final Calendar c = Calendar.getInstance();

            if(settingStartDate) {
                match = trip.getStartDate().split("-");
                matchOther = trip.getEndDate().split("-");
            }
            else {
                match = trip.getEndDate().split("-");
                matchOther = trip.getStartDate().split("-");
            }

            if(match.length==3) {
                month = Integer.parseInt(match[0])-1;
                day = Integer.parseInt(match[1]);
                year = Integer.parseInt(match[2]);
            }
            else
            {
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                year = c.get(Calendar.YEAR);
            }

            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), android.R.style.Theme_Material_Dialog, this, year, month, day);
            if(matchOther.length==3) {
                c.set(Integer.parseInt(matchOther[2]), Integer.parseInt(matchOther[0])-1, Integer.parseInt(matchOther[1]));
                if(settingStartDate) {
                    datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
                }
                else {
                    datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                }
            }
            return datePicker;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date = (month+1)+"-"+day+"-"+year;
            if(settingStartDate) {
                trip.setStartDate(date);
                startDateField.setText(trip.getStartDate());
            }
            else {
                trip.setEndDate(date);
                endDateField.setText(trip.getEndDate());
            }

        }
    }
}
