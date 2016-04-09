package com.regis.darren.mytrips.domain;

import java.io.Serializable;

/**
 * Created by Darren on 3/6/16.
 */
public class ActivityItem implements Serializable {

    private int activityItemId;
    private String activityName;
    private String date;
    private String time;
    private String description;
    private int locationId;

    public ActivityItem() {
        activityItemId = -1;
        activityName = "";
        date = "";
        time = "";
        description = "";
        locationId = -1;
    }

    public ActivityItem(String activityName, String date, String time, String description) {
        activityItemId = -1;
        this.activityName = activityName;
        this.date = date;
        this.time = time;
        this.description = description;
        locationId = -1;
    }

    public ActivityItem(int activityItemId, String activityName, String date, String time, String description, int locationId) {
        this.activityItemId = activityItemId;
        this.activityName = activityName;
        this.date = date;
        this.time = time;
        this.description = description;
        this.locationId = locationId;
    }

    public boolean equals(ActivityItem activityItem) {
        return activityName.compareTo(activityItem.getActivityName())==0 && date.compareTo(activityItem.getDate())==0 && time.compareTo(activityItem.getTime())==0 && description.compareTo(activityItem.getDescription())==0;
    }

    public int getActivityItemId() {
        return activityItemId;
    }

    public void setActivityItemId(int activityItemId) {
        this.activityItemId = activityItemId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return activityName+"\n"+date+" at "+time;
    }
}
