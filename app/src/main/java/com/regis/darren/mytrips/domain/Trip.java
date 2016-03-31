package com.regis.darren.mytrips.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 2/8/16.
 */
public class Trip implements Serializable { //Serializable allows objects of this class to be passed through intent.putExtra
    private int tripId;
    private String name;
    private String startDate;
    private String endDate;
    private List<Location> locations = new ArrayList();

    public Trip() {
        tripId = -1;
        name = "";
        startDate = "";
        endDate = "";
    }

    public Trip(String name, String startDate, String endDate) {
        tripId = -1;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Trip(int tripId, String name, String startDate, String endDate) {
        this.tripId = tripId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean equals(Trip trip) {
        return name.compareTo(trip.getName())==0 && startDate.compareTo(trip.getStartDate())==0 && endDate.compareTo(trip.getEndDate())==0;
    }


    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Location> getLocations() { return locations; }

    public void setLocations(List<Location> locations) { this.locations = locations; }

    @Override
    public String toString() {
        return name+"\n"+startDate+" to "+endDate;
    }

}
