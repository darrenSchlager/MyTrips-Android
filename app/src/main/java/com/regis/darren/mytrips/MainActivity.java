package com.regis.darren.mytrips;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.regis.darren.mytrips.domain.ActivityItem;
import com.regis.darren.mytrips.domain.Location;
import com.regis.darren.mytrips.domain.Trip;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context = null;
    private ListView listView = null;
    private List<Trip> trips = new ArrayList<Trip>();
    private ListAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWithTrips();
    }

    private void initWithTrips() {
        context = this;
        listView = (ListView) findViewById(R.id.tripListView);


        Trip t1 = new Trip("Pacific Islands", "7-10-2016", "7-24-2016");
        List<Location> t1Locations = new ArrayList();
        //
        Location l1 = new Location("Wailea", "Hawaii", "7-10-2016", "7-17-2016");
        List<ActivityItem> l1ActivityItems = new ArrayList();
        l1ActivityItems.add(new ActivityItem("Volcano Hike", "7-11-2016", "10:30 AM", "Hike the Haleakala crater"));
        l1.setActivityItems(l1ActivityItems);
        t1Locations.add(l1);
        //
        Location l2 = new Location("Papeete", "French Polynesia", "7-17-2016", "7-24-2016");
        List<ActivityItem> l2ActivityItems = new ArrayList();
        l2ActivityItems.add(new ActivityItem("Helicopter Tour", "7-20-2016", "2:30 PM", "Helicopter tour of the island"));
        l2.setActivityItems(l2ActivityItems);
        t1Locations.add(l2);
        //
        t1.setLocations(t1Locations);
        trips.add(t1);


        Trip t2 = new Trip("Alaska", "8-5-2017", "8-19-2017");
        List<Location> t2Locations = new ArrayList();
        //
        Location l3 = new Location("Juneau", "Alaska", "8-5-2017", "8-19-2017");
        List<ActivityItem> l3ActivityItems = new ArrayList();
        l3ActivityItems.add(new ActivityItem("Coast Tour", "8-7-2017", "10:30 AM", "Alaska coast boat tour"));
        l3.setActivityItems(l3ActivityItems);
        t2Locations.add(l3);
        //
        t2.setLocations(t2Locations);
        trips.add(t2);


        adapter = new ArrayAdapter<Trip>(context, android.R.layout.simple_list_item_1, trips);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trip trip = (Trip) listView.getItemAtPosition(position);
                Intent intent = new Intent(context, TripActivity.class);
                intent.putExtra("trip", trip);
                startActivity(intent);
            }
        });
    }

    public void newTrip(View view) {
        Intent intent = new Intent(context, TripActivity.class);
        startActivity(intent);
    }
}
