package com.regis.darren.mytrips.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.regis.darren.mytrips.domain.Trip;

import java.util.List;

/**
 * Created by Darren on 3/21/16.
 */
public class TripSvcSQLiteImpl extends SQLiteOpenHelper implements ITripSvc {

    private final static String DBNAME = "trips.db";
    private final static int DBVERSION = 1;
    private final static String CREATEDB = "CREATE TABLE trip (trip_id int primary key, name text, start_date date, end_date date)";

    private static TripSvcSQLiteImpl instance = null;
    //private Context context = null;

    private TripSvcSQLiteImpl(Context context) {
        super(context, DBNAME, null, DBVERSION);
        //this.context = context;
    }

    public static TripSvcSQLiteImpl getInstance(Context context) {
        if(instance==null) {
            instance = new TripSvcSQLiteImpl(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATEDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public Trip create(Trip trip) throws Exception {

        return trip;
    }

    @Override
    public List<Trip> retrieveAll() throws Exception {

        return null;
    }

    @Override
    public Trip update(Trip trip, int tripIndex) throws Exception {

        return trip;
    }

    @Override
    public Trip delete(Trip trip, int tripIndex, int locationIndex, int activityItemIndex) throws Exception {

        return trip;
    }

}
