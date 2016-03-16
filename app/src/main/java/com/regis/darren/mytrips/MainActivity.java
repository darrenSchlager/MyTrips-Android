package com.regis.darren.mytrips;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.regis.darren.mytrips.domain.Trip;
import com.regis.darren.mytrips.service.ITripSvc;
import com.regis.darren.mytrips.service.TripSvcSIOImpl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ITripSvc tripSvc;

    private Context context = null;
    private ListView listView = null;
    private List<Trip> trips = new ArrayList<Trip>();
    private ArrayAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            tripSvc = TripSvcSIOImpl.getInstance(this);
            trips = tripSvc.retrieveAll();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        initWithTrips();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void initWithTrips() {
        context = this;
        listView = (ListView) findViewById(R.id.tripListView);

        adapter = new ArrayAdapter<Trip>(context, android.R.layout.simple_list_item_1, trips);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, TripActivity.class);
                intent.putExtra("tripIndex", position);
                startActivity(intent);
            }
        });
    }

    public void newTrip(View view) {
        Intent intent = new Intent(this, TripActivity.class);
        startActivity(intent);
    }
}
