package com.regis.darren.mytrips;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.regis.darren.mytrips.domain.ActivityItem;
import com.regis.darren.mytrips.domain.Location;
import com.regis.darren.mytrips.domain.Trip;
import com.regis.darren.mytrips.service.ActivitySvcSQLiteImpl;
import com.regis.darren.mytrips.service.IActivityItemSvc;
import com.regis.darren.mytrips.service.ILocationSvc;
import com.regis.darren.mytrips.service.ITripSvc;
import com.regis.darren.mytrips.service.TripSvcSIOImpl;
import com.regis.darren.mytrips.service.TripSvcSQLiteImpl;
import com.regis.darren.mytrips.service.LocationSvcSQLiteImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private ITripSvc tripSIOSvc, tripSQLiteSvc;
    private ILocationSvc locationSvc;
    private IActivityItemSvc activityItemSvc;

    private int tripIndex;
    private int locationIndex;
    private List<Trip> trips = new ArrayList<Trip>();
    private static Trip trip;
    private static Location location;
    private static String tripStartDate;
    private static String tripEndDate;
    private Context context = null;
    private ListView listView = null;
    private ArrayAdapter arrayAdapter = null;
    private SimpleCursorAdapter cursorAdapter = null;

    private Boolean addingNew = false;
    private boolean readyToDelete = false;

    private EditText cityField;
    private EditText stateCountryField;
    private static boolean settingArrive;
    private static Button arriveButton;
    private static Button departButton;

    private Button dynamicButton1;
    private Button dynamicButton2;

    private boolean usingSQLite = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        try {
            if(usingSQLite) {
                activityItemSvc = ActivitySvcSQLiteImpl.getInstance(this);
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
        cityField = (EditText) findViewById(R.id.city);
        stateCountryField = (EditText) findViewById(R.id.stateCountry);
        arriveButton = (Button) findViewById(R.id.arrive);
        departButton = (Button) findViewById(R.id.depart);
        dynamicButton1 = (Button) findViewById(R.id.locationDynamicButton1);
        dynamicButton2 = (Button) findViewById(R.id.locationDynamicButton2);

        context = this;
        listView = (ListView) findViewById(R.id.activitiesListView);

        tripIndex = intent.getIntExtra("tripIndex", -1);
        trip = trips.get(tripIndex);
        tripStartDate = trip.getStartDate();
        tripEndDate = trip.getEndDate();
        locationIndex = intent.getIntExtra("locationIndex", -1);

        if(locationIndex != -1) {
            location = trip.getLocations().get(locationIndex);
            cityField.setText(location.getCity());
            stateCountryField.setText(location.getStateCountry());
            arriveButton.setText(location.getArrive());
            departButton.setText(location.getDepart());
            dynamicButton1.setText("Save");
            dynamicButton2.setText("Delete");
        }
        else
        {
            location = new Location();
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
                cursor = activityItemSvc.getCursor(location.getLocationId());
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (cursor != null) {
                cursorAdapter.changeCursor(cursor);
            }
            cursorAdapter.notifyDataSetChanged();
        }
        else {
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
                if(location.equals(new Location(cityField.getText().toString(), stateCountryField.getText().toString(), arriveButton.getText().toString(), departButton.getText().toString()))) {
                    Intent intent = new Intent(this, ItineraryActivity.class);
                    intent.putExtra("tripIndex", tripIndex);
                    startActivity(intent);
                    return true;
                }
                else {
                    Toast.makeText(this, "Please SAVE the location first", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Please ADD the location first", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initSQLite() {
        Cursor cursor = null;
        try {
            cursor = activityItemSvc.getCursor(location.getLocationId());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(cursor!=null) {
            String [] columNames = {"activity_name", "activity_date_str", "activity_time"};
            int [] textFields = {R.id.activity_title, R.id.left_date, R.id.right_time};
            cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_entry_activity, cursor, columNames, textFields, 0);
        }
        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(location.equals(new Location(cityField.getText().toString(), stateCountryField.getText().toString(), arriveButton.getText().toString(), departButton.getText().toString()))) {
                    Intent intent = new Intent(context, ActivityItemActivity.class);
                    intent.putExtra("tripIndex", tripIndex);
                    intent.putExtra("locationIndex", locationIndex);
                    intent.putExtra("activityItemIndex", position);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context, "Please SAVE the location first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initSIO() {
        arrayAdapter = new ArrayAdapter<ActivityItem>(context, android.R.layout.simple_list_item_1, location.getActivityItems());
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(location.equals(new Location(cityField.getText().toString(), stateCountryField.getText().toString(), arriveButton.getText().toString(), departButton.getText().toString()))) {
                    Intent intent = new Intent(context, ActivityItemActivity.class);
                    intent.putExtra("tripIndex", tripIndex);
                    intent.putExtra("locationIndex", locationIndex);
                    intent.putExtra("activityItemIndex", position);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context, "Please SAVE the location first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addActivity(View view) {
        if(!addingNew) {
            if(location.equals(new Location(cityField.getText().toString(), stateCountryField.getText().toString(), arriveButton.getText().toString(), departButton.getText().toString()))) {
                Intent intent = new Intent(this, ActivityItemActivity.class);
                intent.putExtra("tripIndex", tripIndex);
                intent.putExtra("locationIndex", locationIndex);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Please SAVE the location first", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Please ADD the location first", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLocationDynamic1(View view) {
        String city = cityField.getText().toString();
        String stateCountry = stateCountryField.getText().toString();
        String arrive = arriveButton.getText().toString();
        String depart = departButton.getText().toString();

        if(addingNew) {
            if(city.compareTo("") == 0) {
                Toast.makeText(this, "Please provide a City", Toast.LENGTH_SHORT).show();
            }
            else if(stateCountry.compareTo("") == 0) {
                Toast.makeText(this, "Please provide a State/Country", Toast.LENGTH_SHORT).show();
            }
            else if(arrive.compareTo("") == 0) {
                Toast.makeText(this, "Please provide an Arrival date", Toast.LENGTH_SHORT).show();
            }
            else if(depart.compareTo("") == 0) {
                Toast.makeText(this, "Please provide a Departure date", Toast.LENGTH_SHORT).show();
            }
            else {
                location.setCity(city);
                location.setStateCountry(stateCountry);
                location.setArrive(arrive);
                location.setDepart(depart);
                trip.getLocations().add(location);
                try {
                    if(usingSQLite) {
                        location.setTripId(trip.getTripId());
                        locationSvc.create(location);
                    } else {
                        trip.setTripId(tripIndex);
                        tripSIOSvc.update(trip);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
        else {
            location.setCity(city);
            location.setStateCountry(stateCountry);
            location.setArrive(arrive);
            location.setDepart(depart);
            try {
                if(usingSQLite) {
                    locationSvc.update(location);
                } else {
                    trip.setTripId(tripIndex);
                    tripSIOSvc.update(trip);
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    public void onLocationDynamic2(View view) {
        if(addingNew) {
            finish();
        }
        else {
            if(readyToDelete) {
                if(!usingSQLite) trip.getLocations().remove(locationIndex);
                try {
                    if(usingSQLite) {
                        locationSvc.delete(location);
                    } else {
                        trip.setTripId(tripIndex);
                        tripSIOSvc.update(trip);
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

    public void selectArrive(View view) {
        settingArrive = true;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void selectDepart(View view) {
        settingArrive = false;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String[] match, matchOther;
            int month, day, year;
            long cTripStartDateTimeInMillis, cTripEndDateTimeInMillis;
            final Calendar c = Calendar.getInstance();
            final Calendar cTripStartDate = Calendar.getInstance();
            final Calendar cTripEndDate = Calendar.getInstance();

            String[] matchTripStartDate = tripStartDate.split("-");
            cTripStartDate.set(Integer.parseInt(matchTripStartDate[2]), Integer.parseInt(matchTripStartDate[0]) - 1, Integer.parseInt(matchTripStartDate[1]));
            cTripStartDateTimeInMillis = cTripStartDate.getTimeInMillis();

            String[] matchTripEndDate = tripEndDate.split("-");
            cTripEndDate.set(Integer.parseInt(matchTripEndDate[2]), Integer.parseInt(matchTripEndDate[0]) - 1, Integer.parseInt(matchTripEndDate[1]));
            cTripEndDateTimeInMillis = cTripEndDate.getTimeInMillis();

            if (settingArrive) {
                match = arriveButton.getText().toString().split("-");
                matchOther = departButton.getText().toString().split("-");
            } else {
                match = departButton.getText().toString().split("-");
                matchOther = arriveButton.getText().toString().split("-");
            }

            if (match.length == 3) {
                month = Integer.parseInt(match[0]) - 1;
                day = Integer.parseInt(match[1]);
                year = Integer.parseInt(match[2]);
            } else {
                month = Integer.parseInt(matchTripStartDate[0]) - 1;
                day = Integer.parseInt(matchTripStartDate[1]);
                year = Integer.parseInt(matchTripStartDate[2]);
            }

            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), android.R.style.Theme_Material_Dialog, this, year, month, day);
            if (matchOther.length == 3) {
                c.set(Integer.parseInt(matchOther[2]), Integer.parseInt(matchOther[0]) - 1, Integer.parseInt(matchOther[1]));
                long cTimeInMillis = c.getTimeInMillis();
                if (settingArrive) {
                    datePicker.getDatePicker().setMinDate(cTripStartDateTimeInMillis);
                    datePicker.getDatePicker().setMaxDate(cTimeInMillis);

                } else {
                    datePicker.getDatePicker().setMinDate(cTimeInMillis);
                    datePicker.getDatePicker().setMaxDate(cTripEndDateTimeInMillis);
                }
            }
            else {
                datePicker.getDatePicker().setMinDate(cTripStartDateTimeInMillis);
                datePicker.getDatePicker().setMaxDate(cTripEndDateTimeInMillis);
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
            if (settingArrive) {
                arriveButton.setText(date);
            } else {
                departButton.setText(date);
            }

        }
    }

}
