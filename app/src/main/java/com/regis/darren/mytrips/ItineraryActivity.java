package com.regis.darren.mytrips;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.regis.darren.mytrips.domain.Location;
import com.regis.darren.mytrips.domain.Trip;

public class ItineraryActivity extends AppCompatActivity {

    static Trip trip;
    private Context context = null;
    private ExpandableListView expandableListView = null;
    private ExpandableListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        Intent intent = getIntent();
        trip = (Trip) intent.getSerializableExtra("trip");

        TextView tripName = (TextView) findViewById(R.id.itineraryTripName);
        TextView startDate = (TextView) findViewById(R.id.itineraryStartDate);
        TextView endDate = (TextView) findViewById(R.id.itineraryEndDate);

        tripName.setText(trip.getName());
        startDate.setText(trip.getStartDate());
        endDate.setText(trip.getEndDate());

        initWithLocationsAndActivityItems();
    }

    private void initWithLocationsAndActivityItems() {
        context = this;
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        adapter = new ExpandableListViewAdapter(context, trip.getLocations());
        expandableListView.setAdapter(adapter);

        int groupCount = expandableListView.getExpandableListAdapter().getGroupCount();
        for(int i=0; i<groupCount; i++) {
            expandableListView.expandGroup(i);
        }


    }
}
