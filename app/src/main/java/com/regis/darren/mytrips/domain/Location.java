package com.regis.darren.mytrips.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 3/4/16.
 */
public class Location implements Serializable {

    private String city;
    private String stateCountry;
    private String arrive;
    private String depart;
    private List<ActivityItem> activityItems = new ArrayList();

    public Location(String city, String stateCountry, String arrive, String depart) {
        this.city = city;
        this.stateCountry = stateCountry;
        this.arrive = arrive;
        this.depart = depart;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCountry() {
        return stateCountry;
    }

    public void setStateCountry(String stateCountry) {
        this.stateCountry = stateCountry;
    }

    public String getArrive() {
        return arrive;
    }

    public void setArrive(String arrive) { this.arrive = arrive; }

    public String getDepart() { return depart; }

    public void setDepart(String depart) { this.depart = depart; }

    public List<ActivityItem> getActivityItems() { return activityItems; }

    public void setActivityItems(List<ActivityItem> activityItems) { this.activityItems = activityItems; }

    @Override
    public String toString() { return city+", "+stateCountry+"\n"+arrive+" to "+depart; }
}
