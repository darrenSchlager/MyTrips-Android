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

        Trip t = new Trip("Pacific Islands", "7-10-2016", "7-24-2016");
        List<Location> locations = new ArrayList();
        locations.add(new Location("Wailea", "Hawaii", "7-10-2016", "7-17-2016"));
        locations.add(new Location("Papeete", "French Polynesia", "7-17-2016", "7-24-2016"));
        t.setLocations(locations);
        trips.add(t);

        t = new Trip("Alaska", "8-5-2017", "8-19-2017");
        locations = new ArrayList();
        locations.add(new Location("Juneau", "Alaska", "8-5-2016", "8-19-2016"));
        t.setLocations(locations);
        trips.add(t);

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
}
