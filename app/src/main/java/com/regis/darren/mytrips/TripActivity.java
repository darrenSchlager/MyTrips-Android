package com.regis.darren.mytrips;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.regis.darren.mytrips.domain.Location;
import com.regis.darren.mytrips.domain.Trip;
import com.regis.darren.mytrips.service.ITripSvc;
import com.regis.darren.mytrips.service.TripSvcSIOImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TripActivity extends AppCompatActivity {

    private boolean readyToDelete = false;
    private int tripIndex;
    private List<Trip> trips = new ArrayList<Trip>();
    private static Trip trip;
    private Context context = null;
    private ListView listView = null;
    private ArrayAdapter adapter = null;

    private Boolean addingNew = false;

    private EditText tripNameField;

    private static boolean settingStartDate;
    private static Button startDateButton;
    private static Button endDateButton;
    private Button dynamicButton1;
    private Button dynamicButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        try {
            ITripSvc tripSvc = TripSvcSIOImpl.getInstance(this);
            trips = tripSvc.retrieveAll();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Intent intent = getIntent();
        tripNameField = (EditText) findViewById(R.id.tripName);
        startDateButton = (Button) findViewById(R.id.startDate);
        endDateButton = (Button) findViewById(R.id.endDate);
        dynamicButton1 = (Button) findViewById(R.id.tripDynamicButton1);
        dynamicButton2 = (Button) findViewById(R.id.tripDynamicButton2);

        tripIndex = intent.getIntExtra("tripIndex", -1);
        if(tripIndex != -1) {
            trip = trips.get(tripIndex);
            tripNameField.setText(trip.getName());
            startDateButton.setText(trip.getStartDate());
            endDateButton.setText(trip.getEndDate());
            dynamicButton1.setText("Save");
            dynamicButton2.setText("Delete");
        }
        else {
            trip = new Trip();
            dynamicButton1.setText("Add");
            dynamicButton2.setText("Cancel");
            addingNew = true;
        }
        initWithLocations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_itinerary) {
            if(!addingNew) {
                if(trip.equals(new Trip(tripNameField.getText().toString(), startDateButton.getText().toString(), endDateButton.getText().toString()))) {
                    Intent intent = new Intent(this, ItineraryActivity.class);
                    intent.putExtra("tripIndex", tripIndex);
                    startActivity(intent);
                    return true;
                }
                else {
                    Toast.makeText(this, "Please SAVE the trip first", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Please ADD the trip first", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initWithLocations() {
        context = this;
        listView = (ListView) findViewById(R.id.locationListView);

        adapter = new ArrayAdapter<Location>(context, android.R.layout.simple_list_item_1, trip.getLocations());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(trip.equals(new Trip(tripNameField.getText().toString(), startDateButton.getText().toString(), endDateButton.getText().toString()))) {
                    Intent intent = new Intent(context, LocationActivity.class);
                    intent.putExtra("tripIndex", tripIndex);
                    intent.putExtra("locationIndex", position);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context, "Please SAVE the trip first", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void addLocation(View view) {

        if(!addingNew) {
            if(trip.equals(new Trip(tripNameField.getText().toString(), startDateButton.getText().toString(), endDateButton.getText().toString()))) {
                Intent intent = new Intent(this, LocationActivity.class);
                intent.putExtra("tripIndex", tripIndex);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Please SAVE the trip first", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Please ADD the trip first", Toast.LENGTH_SHORT).show();
        }

    }

    public void onTripDynamic1(View view) {

        try {
            ITripSvc tripSvc = TripSvcSIOImpl.getInstance(this);

            String tripName = tripNameField.getText().toString();
            String startDate = startDateButton.getText().toString();
            String endDate = endDateButton.getText().toString();

            if(addingNew) {
                if(tripName.compareTo("Trip Name") == 0) {
                    Toast.makeText(this, "Please provide a Trip Name", Toast.LENGTH_SHORT).show();
                }
                else if(startDate.compareTo("Date") == 0) {
                    Toast.makeText(this, "Please provide a Start Date", Toast.LENGTH_SHORT).show();
                }
                else if(endDate.compareTo("Date") == 0) {
                    Toast.makeText(this, "Please provide a End Date", Toast.LENGTH_SHORT).show();
                }
                else {
                    trip.setName(tripName);
                    trip.setStartDate(startDate);
                    trip.setEndDate(endDate);
                    tripSvc.create(trip);
                    finish();
                }
            }
            else {
                trip.setName(tripName);
                trip.setStartDate(startDate);
                trip.setEndDate(endDate);
                tripSvc.update(trip, tripIndex);
                finish();
            }
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onTripDynamic2(View view) {
        try {
            ITripSvc tripSvc = TripSvcSIOImpl.getInstance(this);

            if(addingNew) {
                finish();
            }
            else {
                if(readyToDelete) {
                    tripSvc.delete(trip, tripIndex, -1, -1);
                    finish();
                }
                else {
                    dynamicButton2.setText("Confirm Delete");
                    readyToDelete = true;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                match = startDateButton.getText().toString().split("-");
                matchOther = endDateButton.getText().toString().split("-");
            }
            else {
                match = endDateButton.getText().toString().split("-");
                matchOther = startDateButton.getText().toString().split("-");
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
                startDateButton.setText(date);
            }
            else {
                endDateButton.setText(date);
            }

        }
    }
}
