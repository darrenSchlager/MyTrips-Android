package com.regis.darren.mytrips.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 3/4/16.
 */
public class Location implements Serializable {

    private int locationId;
    private String city;
    private String stateCountry;
    private String arrive;
    private String depart;
    private int tripId;
    private List<ActivityItem> activityItems = new ArrayList();

    public Location() {
        locationId = -1;
        city = "";
        stateCountry = "";
        arrive = "";
        depart = "";
        tripId = -1;
    }

    public Location(String city, String stateCountry, String arrive, String depart) {
        locationId = -1;
        this.city = city;
        this.stateCountry = stateCountry;
        this.arrive = arrive;
        this.depart = depart;
        tripId = -1;
    }

    public boolean equals(Location location) {
        return city.compareTo(location.getCity())==0 && stateCountry.compareTo(location.getStateCountry())==0 && arrive.compareTo(location.getArrive())==0 && depart.compareTo(location.getDepart())==0;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
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
