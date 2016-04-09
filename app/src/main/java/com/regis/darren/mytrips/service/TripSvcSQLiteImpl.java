package com.regis.darren.mytrips.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.regis.darren.mytrips.domain.Location;
import com.regis.darren.mytrips.domain.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 3/21/16.
 */
public class TripSvcSQLiteImpl extends SvcSQLiteAbs implements ITripSvc {

    private static TripSvcSQLiteImpl instance = null;
    private Context context;

    private TripSvcSQLiteImpl(Context context) {
        super(context);
        this.context = context;
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
        if(match[0].length()==1) match[0] = "0"+match[0];
        if(match[1].length()==1) match[1] = "0"+match[1];
        startDate = match[2]+"-"+match[0]+"-"+match[1];
        match = trip.getEndDate().split("-");
        if(match[0].length()==1) match[0] = "0"+match[0];
        if(match[1].length()==1) match[1] = "0"+match[1];
        endDate = match[2]+"-"+match[0]+"-"+match[1];

        ContentValues values = new ContentValues();
        values.put("name", trip.getName());
        values.put("start_date", startDate);
        values.put("end_date", endDate);
        db.insert("trip", null, values);
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        cursor.moveToFirst();
        trip.setTripId(cursor.getInt(0));
        cursor.close();
        db.close();
        return trip;
    }

    @Override
    public List<Trip> retrieveAll() throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        List<Trip> trips = new ArrayList();
        Cursor cursor = db.query("trip", new String[]{"_id", "name", "start_date", "end_date"}, null, null, null, null, "start_date");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Trip trip = getTrip(cursor);
            List<Location> locations = LocationSvcSQLiteImpl.getInstance(context).retrieveAll(trip.getTripId());
            trip.setLocations(locations);
            trips.add(trip);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return trips;
    }

    @Override
    public Cursor getCursor() throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, name, strftime('%m-%d-%Y', start_date) AS start_date_str, strftime('%m-%d-%Y', end_date) AS end_date_str FROM trip ORDER BY start_date", null);
        cursor.moveToFirst();
        db.close();
        return cursor;
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
        if(match[0].length()==1) match[0] = "0"+match[0];
        if(match[1].length()==1) match[1] = "0"+match[1];
        startDate = match[2]+"-"+match[0]+"-"+match[1];
        match = trip.getEndDate().split("-");
        if(match[0].length()==1) match[0] = "0"+match[0];
        if(match[1].length()==1) match[1] = "0"+match[1];
        endDate = match[2]+"-"+match[0]+"-"+match[1];

        LocationSvcSQLiteImpl.getInstance(context).updateDates(trip.getTripId(), trip.getStartDate(), trip.getEndDate());
        List<Location> locations = LocationSvcSQLiteImpl.getInstance(context).retrieveAll(trip.getTripId());
        for (Location l: locations) {
            ActivitySvcSQLiteImpl.getInstance(context).updateDates(l.getLocationId(), l.getArrive(), l.getDepart());
        }

        ContentValues values = new ContentValues();
        values.put("name", trip.getName());
        values.put("start_date", startDate);
        values.put("end_date", endDate);
        db.update("trip", values, "_id=" + String.valueOf(trip.getTripId()), null);
        db.close();
        return trip;
    }

    @Override
    public Trip delete(Trip trip) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("trip", "_id="+trip.getTripId(), null);
        db.close();
        return trip;
    }

}
