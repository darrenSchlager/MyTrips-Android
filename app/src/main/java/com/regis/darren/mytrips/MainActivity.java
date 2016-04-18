package com.regis.darren.mytrips;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.regis.darren.mytrips.domain.Trip;
import com.regis.darren.mytrips.service.ITripSvc;
import com.regis.darren.mytrips.service.TripSvcSIOImpl;
import com.regis.darren.mytrips.service.TripSvcSQLiteImpl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ITripSvc tripSIOSvc, tripSQLite;

    private Context context = null;
    private ListView listView = null;
    private List<Trip> trips = new ArrayList<Trip>();
    private ArrayAdapter arrayAdapter = null;
    private CursorAdapter cursorAdapter = null;

    private boolean usingSQLite = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        listView = (ListView) findViewById(R.id.tripListView);

        if(usingSQLite) initSQLite();
        else initSIO();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(cursorAdapter!=null) {
            Cursor cursor = null;
            try {
                cursor = tripSQLite.getCursor();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if(cursor!=null) {
                cursorAdapter.changeCursor(cursor);
                cursorAdapter.notifyDataSetChanged();
            }
        } else {
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void initSQLite() {
        Cursor cursor = null;
        try {
            tripSQLite = TripSvcSQLiteImpl.getInstance(this);
            cursor = tripSQLite.getCursor();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(cursor!=null) {
            String [] columNames = {"name", "start_date_str", "end_date_str"};
            int [] textFields = {R.id.activity_title, R.id.left_date, R.id.right_date};
            cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_entry_trip, cursor, columNames, textFields, 0);
            listView.setAdapter(cursorAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, TripActivity.class);
                    intent.putExtra("tripIndex", position);
                    startActivity(intent);
                }
            });
        }
    }

    private void initSIO() {
        try {
            tripSIOSvc = TripSvcSIOImpl.getInstance(this);
            trips = tripSIOSvc.retrieveAll();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        arrayAdapter = new ArrayAdapter<Trip>(context, android.R.layout.simple_list_item_1, trips);
        listView.setAdapter(arrayAdapter);

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
