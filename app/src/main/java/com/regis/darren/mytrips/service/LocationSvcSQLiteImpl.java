package com.regis.darren.mytrips.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.regis.darren.mytrips.domain.ActivityItem;
import com.regis.darren.mytrips.domain.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 4/8/16.
 */
public class LocationSvcSQLiteImpl extends SvcSQLiteAbs implements ILocationSvc {

    private static LocationSvcSQLiteImpl instance = null;
    private Context context;

    private LocationSvcSQLiteImpl(Context context) {
        super(context);
        this.context = context;
    }

    public static LocationSvcSQLiteImpl getInstance(Context context) {
        if(instance==null) {
            instance = new LocationSvcSQLiteImpl(context);
        }
        return instance;
    }

    @Override
    public Location create(Location location) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        String[] match;
        String arrive, depart;
        match = location.getArrive().split("-");
        if(match[0].length()==1) match[0] = "0"+match[0];
        if(match[1].length()==1) match[1] = "0"+match[1];
        arrive = match[2]+"-"+match[0]+"-"+match[1];
        match = location.getDepart().split("-");
        if(match[0].length()==1) match[0] = "0"+match[0];
        if(match[1].length()==1) match[1] = "0"+match[1];
        depart = match[2]+"-"+match[0]+"-"+match[1];

        ContentValues values = new ContentValues();
        values.put("city", location.getCity());
        values.put("state_country", location.getStateCountry());
        values.put("arrive", arrive);
        values.put("depart", depart);
        values.put("trip_id", location.getTripId());
        db.insert("location", null, values);
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        cursor.moveToFirst();
        location.setLocationId(cursor.getInt(0));
        cursor.close();
        db.close();
        return location;
    }

    @Override
    public List<Location> retrieveAll(int tripId) throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        List<Location> locations = new ArrayList();
        Cursor cursor = db.query("location", new String[]{"_id", "city", "state_country", "arrive", "depart", "trip_id"}, "trip_id=?", new String[]{tripId+""}, null, null, "arrive");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Location location = getLocation(cursor);
            List<ActivityItem> activityItems = ActivitySvcSQLiteImpl.getInstance(context).retrieveAll(location.getLocationId());
            location.setActivityItems(activityItems);
            locations.add(location);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return locations;
    }

    @Override
    public Cursor getCursor(int tripId) throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, city, state_country, strftime('%m-%d-%Y', arrive) AS arrive, strftime('%m-%d-%Y', depart) AS depart, trip_id FROM location WHERE trip_id="+tripId+" ORDER BY arrive", null);
        cursor.moveToFirst();
        db.close();
        return cursor;
    }

    private Location getLocation(Cursor cursor) {
        String[] match;
        String arrive, depart;
        match = cursor.getString(3).split("-");
        arrive = match[1]+"-"+match[2]+"-"+match[0];
        match = cursor.getString(4).split("-");
        depart = match[1]+"-"+match[2]+"-"+match[0];

        Location location = new Location(cursor.getInt(0), cursor.getString(1), cursor.getString(2), arrive, depart, cursor.getInt(5));
        return location;
    }

    @Override
    public Location update(Location location) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        String[] match;
        String arrive, depart;
        match = location.getArrive().split("-");
        if(match[0].length()==1) match[0] = "0"+match[0];
        if(match[1].length()==1) match[1] = "0"+match[1];
        arrive = match[2]+"-"+match[0]+"-"+match[1];
        match = location.getDepart().split("-");
        if(match[0].length()==1) match[0] = "0"+match[0];
        if(match[1].length()==1) match[1] = "0"+match[1];
        depart = match[2]+"-"+match[0]+"-"+match[1];

        ContentValues values = new ContentValues();
        values.put("city", location.getCity());
        values.put("state_country", location.getStateCountry());
        values.put("arrive", arrive);
        values.put("depart", depart);
        values.put("trip_id", location.getTripId());
        db.update("location", values, "_id=" + String.valueOf(location.getLocationId()), null);
        db.close();
        return location;
    }

    @Override
    public Location delete(Location location) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("location", "_id="+location.getLocationId(), null);
        db.close();
        return location;
    }

}
