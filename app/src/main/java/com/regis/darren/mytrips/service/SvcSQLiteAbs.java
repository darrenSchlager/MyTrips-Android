package com.regis.darren.mytrips.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.regis.darren.mytrips.domain.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 3/30/16.
 */
public abstract class SvcSQLiteAbs extends SQLiteOpenHelper {

    private final static String DBNAME = "trips.db";
    private final static int DBVERSION = 1;
    private final static String CREATE_TRIP_TABLE = "CREATE TABLE trip (_id integer primary key autoincrement, name text, start_date date, end_date date)";
    private final static String DROP_TRIP_TABLE = "DROP TABLE IF EXISTS trip";
    private final static String CREATE_LOCATION_TABLE = "CREATE TABLE location (_id integer primary key autoincrement, city text, state_country text, arrive date, depart date, trip_id integer, FOREIGN KEY(trip_id) REFERENCES trip(_id))";
    private final static String DROP_LOCATION_TABLE = "DROP TABLE IF EXISTS location";
    private final static String CREATE_ACTIVITY_ITEM_TABLE = "CREATE TABLE activity_item (_id integer primary key autoincrement, activity_name text, activity_date date, activity_time time, description text, location_id integer, FOREIGN KEY(location_id) REFERENCES location(_id))";
    private final static String DROP_ACTIVITY_ITEM_TABLE = "DROP TABLE IF EXISTS activity_item";
    //protected SQLiteDatabase db = null;
    private List<Trip> currentTrips = new ArrayList();

    public SvcSQLiteAbs(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRIP_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_ACTIVITY_ITEM_TABLE);
        //this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TRIP_TABLE);
        db.execSQL(DROP_LOCATION_TABLE);
        db.execSQL(DROP_ACTIVITY_ITEM_TABLE);
        onCreate(db);
    }

}
