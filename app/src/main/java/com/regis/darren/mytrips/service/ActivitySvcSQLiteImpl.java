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
public class ActivitySvcSQLiteImpl extends SvcSQLiteAbs implements IActivityItemSvc {

    private static ActivitySvcSQLiteImpl instance = null;

    private ActivitySvcSQLiteImpl(Context context) {
        super(context);
    }

    public static ActivitySvcSQLiteImpl getInstance(Context context) {
        if(instance==null) {
            instance = new ActivitySvcSQLiteImpl(context);
        }
        return instance;
    }

    @Override
    public ActivityItem create(ActivityItem activityItem) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        String[] match, timeMatch;
        String date, ampm;
        int time, hour, minute;
        match = activityItem.getDate().split("-");
        if(match[0].length()==1) match[0] = "0"+match[0];
        if(match[1].length()==1) match[1] = "0"+match[1];
        date = match[2]+"-"+match[0]+"-"+match[1];
        match = activityItem.getTime().split(" ");
        ampm = match[1].toUpperCase();
        timeMatch = match[0].split(":");
        hour = Integer.parseInt(timeMatch[0]);
        minute = Integer.parseInt(timeMatch[1]);
        if(ampm.compareTo("PM")==0) {
            if(hour<12) time = (hour+12)*100+minute;
            else time = 1200+minute;
        }
        else {
            if(hour==12) time = minute;
            else time = hour*100+minute;
        }

        ContentValues values = new ContentValues();
        values.put("activity_name", activityItem.getActivityName());
        values.put("activity_date", date);
        values.put("activity_time", activityItem.getTime());
        values.put("activity_time24", time);
        values.put("description", activityItem.getDescription());
        values.put("location_id", activityItem.getLocationId());
        db.insert("activity_item", null, values);
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        cursor.moveToFirst();
        activityItem.setActivityItemId(cursor.getInt(0));
        cursor.close();
        db.close();
        return activityItem;
    }

    @Override
    public List<ActivityItem> retrieveAll(int locationId) throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        List<ActivityItem> activityItems = new ArrayList();
        Cursor cursor = db.query("activity_item", new String[]{"_id", "activity_name", "activity_date", "activity_time", "description", "location_id"}, "location_id=?", new String[]{locationId+""}, null, null, "activity_date, activity_time24");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            ActivityItem activityItem = getActivityItem(cursor);
            activityItems.add(activityItem);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return activityItems;
    }

    @Override
    public Cursor getCursor(int locationId) throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, activity_name, strftime('%m-%d-%Y', activity_date) AS activity_date, activity_time, description, location_id FROM activity_item WHERE location_id="+locationId+" ORDER BY activity_date, activity_time24", null);
        cursor.moveToFirst();
        db.close();
        return cursor;
    }

    private ActivityItem getActivityItem(Cursor cursor) {
        String[] match;
        String date;
        match = cursor.getString(2).split("-");
        date = match[1]+"-"+match[2]+"-"+match[0];

        ActivityItem activityItem = new ActivityItem(cursor.getInt(0), cursor.getString(1), date, cursor.getString(3), cursor.getString(4), cursor.getInt(5));
        return activityItem;
    }

    @Override
    public ActivityItem update(ActivityItem activityItem) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        String[] match, timeMatch;
        String date, ampm;
        int time, hour, minute;
        match = activityItem.getDate().split("-");
        if(match[0].length()==1) match[0] = "0"+match[0];
        if(match[1].length()==1) match[1] = "0"+match[1];
        date = match[2]+"-"+match[0]+"-"+match[1];
        match = activityItem.getTime().split(" ");
        ampm = match[1].toUpperCase();
        timeMatch = match[0].split(":");
        hour = Integer.parseInt(timeMatch[0]);
        minute = Integer.parseInt(timeMatch[1]);
        if(ampm.compareTo("PM")==0) {
            if(hour<12) time = (hour+12)*100+minute;
            else time = 1200+minute;
        }
        else {
            if(hour==12) time = minute;
            else time = hour*100+minute;
        }

        ContentValues values = new ContentValues();
        values.put("activity_name", activityItem.getActivityName());
        values.put("activity_date", date);
        values.put("activity_time", activityItem.getTime());
        values.put("activity_time24", time);
        values.put("description", activityItem.getDescription());
        values.put("location_id", activityItem.getLocationId());
        db.update("activity_item", values, "_id=" + String.valueOf(activityItem.getActivityItemId()), null);
        db.close();
        return activityItem;
    }

    @Override
    public ActivityItem delete(ActivityItem activityItem) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("activity_item", "_id="+activityItem.getActivityItemId(), null);
        db.close();
        return activityItem;
    }

}
