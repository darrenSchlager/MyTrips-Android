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

import com.regis.darren.mytrips.domain.Trip;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context = null;
    private ListView listView = null;
    private List<String> list = new ArrayList<String>();
    private List<Trip> trips = new ArrayList<Trip>();
    private ListAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWithTrips();
    }

    private void initWithStrings() {
        context = this;
        listView = (ListView) findViewById(R.id.tripListView);
        list.add("Pacific Islands");
        list.add("Scandinavia");
        list.add("Alaska");
        list.add("Europe");
        list.add("Australia");
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(context, TripActivity.class);
                intent.putExtra("trip name", item);
                startActivity(intent);
            }
        });
    }

    private void initWithTrips() {
        context = this;
        listView = (ListView) findViewById(R.id.tripListView);
        trips.add( new Trip("Pacific Islands", "7-10-2016", "7-24-2016") );
        trips.add( new Trip("Scandanavia", "8-10-2017", "8-24-2017") );
        adapter = new ArrayAdapter<Trip>(context, android.R.layout.simple_list_item_1, trips);
        listView.setAdapter(adapter);


    }
}
