package com.regis.darren.mytrips;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.regis.darren.mytrips.domain.Trip;
import com.regis.darren.mytrips.service.ITripSvc;
//import com.regis.darren.mytrips.service.TripSvcSIOImpl;
import com.regis.darren.mytrips.service.TripSvcSQLiteImpl;

import java.util.ArrayList;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity {

    private ITripSvc tripSvc;

    private int tripIndex;
    private List<Trip> trips = new ArrayList<Trip>();
    static Trip trip;
    private Context context = null;
    private ExpandableListView expandableListView = null;
    private ExpandableListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        try {
            //tripSvc = TripSvcSIOImpl.getInstance(this);
            tripSvc = TripSvcSQLiteImpl.getInstance(this);
            trips = tripSvc.retrieveAll();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Intent intent = getIntent();
        tripIndex = intent.getIntExtra("tripIndex", -1);
        trip = trips.get(tripIndex);

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
