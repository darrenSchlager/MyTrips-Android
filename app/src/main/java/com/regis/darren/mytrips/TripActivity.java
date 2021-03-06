package com.regis.darren.mytrips;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.regis.darren.mytrips.domain.Location;
import com.regis.darren.mytrips.domain.Trip;
import com.regis.darren.mytrips.service.ILocationSvc;
import com.regis.darren.mytrips.service.ITripSvc;
import com.regis.darren.mytrips.service.TripSvcSIOImpl;
import com.regis.darren.mytrips.service.TripSvcSQLiteImpl;
import com.regis.darren.mytrips.service.LocationSvcSQLiteImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TripActivity extends AppCompatActivity {

    private ITripSvc tripSIOSvc, tripSQLiteSvc;
    private ILocationSvc locationSvc;

    private int tripIndex;
    private List<Trip> trips = new ArrayList<Trip>();
    private List<Location> locations = new ArrayList<Location>();
    private static Trip trip;
    private Context context = null;
    private ListView listView = null;
    private ArrayAdapter arrayAdapter = null;
    private CursorAdapter cursorAdapter = null;

    private Boolean addingNew = false;
    private boolean readyToDelete = false;

    private EditText tripNameField;
    private static boolean settingStartDate;
    private static Button startDateButton;
    private static Button endDateButton;
    private Button dynamicButton1;
    private Button dynamicButton2;

    private boolean usingSQLite = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        try {
            if(usingSQLite) {
                locationSvc = LocationSvcSQLiteImpl.getInstance(this);
                tripSQLiteSvc = TripSvcSQLiteImpl.getInstance(this);
                trips = tripSQLiteSvc.retrieveAll();
            } else {
                tripSIOSvc = TripSvcSIOImpl.getInstance(this);
                trips = tripSIOSvc.retrieveAll();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Intent intent = getIntent();
        tripNameField = (EditText) findViewById(R.id.tripName);
        startDateButton = (Button) findViewById(R.id.startDate);
        endDateButton = (Button) findViewById(R.id.endDate);
        dynamicButton1 = (Button) findViewById(R.id.tripDynamicButton1);
        dynamicButton2 = (Button) findViewById(R.id.tripDynamicButton2);

        context = this;
        listView = (ListView) findViewById(R.id.locationListView);

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

        if(usingSQLite) initSQLite();
        else initSIO();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readyToDelete = false;
        if(!addingNew) {
            dynamicButton2.setText("Delete");
        }

        if(usingSQLite) {
            Cursor cursor = null;
            try {
                cursor = locationSvc.getCursor(trip.getTripId());
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (cursor != null) {
                cursorAdapter.changeCursor(cursor);
            }

            cursorAdapter.notifyDataSetChanged();
        } else {
            arrayAdapter.notifyDataSetChanged();
        }
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

    private void initSQLite() {
        Cursor cursor = null;
        try {
            cursor = locationSvc.getCursor(trip.getTripId());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(cursor!=null) {
            String[] columNames = {"city", "state_country", "arrive_str", "depart_str"};
            int[] textFields = {R.id.activity_title, R.id.statecountry_title, R.id.left_date, R.id.right_date};
            cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_entry_location, cursor, columNames, textFields, 0);
            listView.setAdapter(cursorAdapter);

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
    }

    private void initSIO() {
        arrayAdapter = new ArrayAdapter<Location>(context, android.R.layout.simple_list_item_1, trip.getLocations());
        listView.setAdapter(arrayAdapter);

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
        String tripName = tripNameField.getText().toString();
        String startDate = startDateButton.getText().toString();
        String endDate = endDateButton.getText().toString();

        if(addingNew) {
            if(tripName.compareTo("") == 0) {
                Toast.makeText(this, "Please provide a Trip Name", Toast.LENGTH_SHORT).show();
            }
            else if(startDate.compareTo("") == 0) {
                Toast.makeText(this, "Please provide a Start date", Toast.LENGTH_SHORT).show();
            }
            else if(endDate.compareTo("") == 0) {
                Toast.makeText(this, "Please provide a End date", Toast.LENGTH_SHORT).show();
            }
            else {
                trip.setName(tripName);
                trip.setStartDate(startDate);
                trip.setEndDate(endDate);
                try {
                    if(usingSQLite) tripSQLiteSvc.create(trip);
                    else tripSIOSvc.create(trip);

                }
                catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
        else {
            trip.setName(tripName);
            trip.setStartDate(startDate);
            trip.setEndDate(endDate);
            try {
                if(usingSQLite) tripSQLiteSvc.update(trip);
                else {
                    trip.setTripId(tripIndex);
                    tripSIOSvc.update(trip);
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    public void onTripDynamic2(View view) {
        if(addingNew) {
            finish();
        }
        else {
            if(readyToDelete) {
                try {
                    if(usingSQLite) tripSQLiteSvc.delete(trip);
                    else {
                        trip.setTripId(tripIndex);
                        tripSIOSvc.delete(trip);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
            else {
                dynamicButton2.setText("Confirm Delete");
                readyToDelete = true;

                CountDownTimer Count = new CountDownTimer(1500, 500) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        readyToDelete=false;
                        dynamicButton2.setText("Delete");
                    }
                };
                Count.start();
            }
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
            //String date = (month+1)+"-"+day+"-"+year;
            String date;
            if(month+1<10) date = "0"+(month+1)+"-";
            else date = (month+1)+"-";
            if(day<10) date+="0"+day+"-"+year;
            else date+=day+"-"+year;

            if(settingStartDate) {
                startDateButton.setText(date);
            }
            else {
                endDateButton.setText(date);
            }

        }
    }
}
