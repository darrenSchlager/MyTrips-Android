package com.regis.darren.mytrips.domain;

import java.io.Serializable;

/**
 * Created by Darren on 3/6/16.
 */
public class ActivityItem implements Serializable {

    private String activityName;
    private String date;
    private String time;
    private String description;

    public ActivityItem() {
        activityName = "";
        date = "";
        time = "";
        description = "";
    }

    public ActivityItem(String activityName, String date, String time, String description) {
        this.activityName = activityName;
        this.date = date;
        this.time = time;
        this.description = description;
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
