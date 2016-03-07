package com.regis.darren.mytrips;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import com.regis.darren.mytrips.domain.ActivityItem;
import com.regis.darren.mytrips.domain.Location;
import com.regis.darren.mytrips.domain.Trip;

import java.util.Calendar;

public class ActivityItemActivity extends AppCompatActivity {

    static ActivityItem activityItem;
    static String locationArrive;
    static String locationDepart;
    private Context context = null;
    private ListView listView = null;
    private ListAdapter adapter = null;
    static Button dateButton;
    static Button timeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_item);

        Intent intent = getIntent();
        EditText activityItemNameField = (EditText) findViewById(R.id.activityName);
        dateButton = (Button) findViewById(R.id.date);
        timeButton = (Button) findViewById(R.id.time);
        EditText descriptionField = (EditText) findViewById(R.id.description);

        Location location = (Location) intent.getSerializableExtra("location");
        locationArrive = location.getArrive();
        locationDepart = location.getDepart();
        int activityItemIndex = intent.getIntExtra("activityItemIndex", -1);

        if(activityItemIndex != -1) {
            activityItem = location.getActivityItems().get(activityItemIndex);
            activityItemNameField.setText(activityItem.getActivityName());
            dateButton.setText(activityItem.getDate());
            timeButton.setText(activityItem.getTime());
            descriptionField.setText(activityItem.getDescription());
        }
        else
        {
            activityItem = new ActivityItem();
        }
    }

    public void selectDate(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String[] match;
            int month, day, year;
            long cLocationArriveTimeInMillis, cLocationDepartTimeInMillis;
            final Calendar c = Calendar.getInstance();
            final Calendar cLocationArrive = Calendar.getInstance();
            final Calendar cLocationDepart = Calendar.getInstance();

            String[] matchLocationArrive = locationArrive.split("-");
            cLocationArrive.set(Integer.parseInt(matchLocationArrive[2]), Integer.parseInt(matchLocationArrive[0]) - 1, Integer.parseInt(matchLocationArrive[1]));
            cLocationArriveTimeInMillis = cLocationArrive.getTimeInMillis();

            String[] matchLocationDepart = locationDepart.split("-");
            cLocationDepart.set(Integer.parseInt(matchLocationDepart[2]), Integer.parseInt(matchLocationDepart[0]) - 1, Integer.parseInt(matchLocationDepart[1]));
            cLocationDepartTimeInMillis = cLocationDepart.getTimeInMillis();

            match = activityItem.getDate().split("-");

            if (match.length == 3) {
                month = Integer.parseInt(match[0]) - 1;
                day = Integer.parseInt(match[1]);
                year = Integer.parseInt(match[2]);
            } else {
                month = Integer.parseInt(matchLocationArrive[0]) - 1;
                day = Integer.parseInt(matchLocationArrive[1]);
                year = Integer.parseInt(matchLocationArrive[2]);
            }

            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), android.R.style.Theme_Material_Dialog, this, year, month, day);
            datePicker.getDatePicker().setMinDate(cLocationArriveTimeInMillis);
            datePicker.getDatePicker().setMaxDate(cLocationDepartTimeInMillis);
            return datePicker;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date = (month + 1) + "-" + day + "-" + year;
            activityItem.setDate(date);
            dateButton.setText(date);
        }
    }

    public void selectTime(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String[] match;
            int hour, minute;
            final Calendar c = Calendar.getInstance();

            match = activityItem.getTime().split(" |:");

            if (match.length == 3) {
                hour = Integer.parseInt(match[0]);
                minute = Integer.parseInt(match[1]);
                String ampm = match[2].toUpperCase();
                if(ampm.compareTo("AM")==0 && hour==12) {
                    hour=0;
                }
                else if(ampm.compareTo("PM")==0 && hour!=12) {
                    hour+=12;
                }
            }
            else {
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
            }

            return new TimePickerDialog(getActivity(), android.R.style.Theme_Material_Dialog, this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String minuteString;
            String time;

            if(minute<10) {
                minuteString = "0"+minute;
            }
            else {
                minuteString = ""+minute;
            }

            if(hourOfDay==0) {
                time = "12:" + minuteString + " AM";
            }
            else if(hourOfDay<12) {
                time = hourOfDay + ":" + minuteString + " AM";
            }
            else if(hourOfDay==12) {
                time = hourOfDay + ":" + minuteString + " PM";
            }
            else {
                time = "" + (hourOfDay-12) + ":" + minuteString + " PM";
            }
            activityItem.setTime(time);
            timeButton.setText(time);
        }
    }

}
