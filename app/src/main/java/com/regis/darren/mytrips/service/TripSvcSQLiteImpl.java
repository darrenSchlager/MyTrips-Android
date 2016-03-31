package com.regis.darren.mytrips.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.regis.darren.mytrips.domain.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 3/21/16.
 */
public class TripSvcSQLiteImpl extends SvcSQLiteAbs implements ITripSvc {

    private static TripSvcSQLiteImpl instance = null;

    private TripSvcSQLiteImpl(Context context) {
        super(context);
    }

    public static TripSvcSQLiteImpl getInstance(Context context) {
        if(instance==null) {
            instance = new TripSvcSQLiteImpl(context);
        }
        return instance;
    }

    @Override
    public Trip create(Trip trip) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        String[] match;
        String startDate, endDate;
        match = trip.getStartDate().split("-");
        startDate = match[2]+"-"+match[0]+"-"+match[1];
        match = trip.getEndDate().split("-");
        endDate = match[2]+"-"+match[0]+"-"+match[1];

        ContentValues values = new ContentValues();
        values.put("name", trip.getName());
        values.put("start_date", startDate);
        values.put("end_date", endDate);
        db.insert("trip", null, values);
        db.close();
        return trip;
    }

    @Override
    public List<Trip> retrieveAll() throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        List<Trip> trips = new ArrayList();
        Cursor cursor = db.query("trip", new String[]{"trip_id", "name", "start_date", "end_date"}, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Trip trip = getTrip(cursor);
            trips.add(trip);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return trips;
    }

    private Trip getTrip(Cursor cursor) {
        String[] match;
        String startDate, endDate;
        match = cursor.getString(2).split("-");
        startDate = match[1]+"-"+match[2]+"-"+match[0];
        match = cursor.getString(3).split("-");
        endDate = match[1]+"-"+match[2]+"-"+match[0];

        Trip trip = new Trip(cursor.getInt(0), cursor.getString(1), startDate, endDate);
        return trip;
    }

    @Override
    public Trip update(Trip trip) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        String[] match;
        String startDate, endDate;
        match = trip.getStartDate().split("-");
        startDate = match[2]+"-"+match[0]+"-"+match[1];
        match = trip.getEndDate().split("-");
        endDate = match[2]+"-"+match[0]+"-"+match[1];

        ContentValues values = new ContentValues();
        values.put("name", trip.getName());
        values.put("start_date", startDate);
        values.put("end_date", endDate);
        db.update("trip", values, "trip_id=" + String.valueOf(trip.getTripId()), null);
        db.close();
        return trip;
    }

    @Override
    public Trip delete(Trip trip) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("trip", "trip_id="+trip.getTripId(), null);
        db.close();
        return trip;
    }

}
