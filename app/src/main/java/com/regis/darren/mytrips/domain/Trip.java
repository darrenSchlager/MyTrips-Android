package com.regis.darren.mytrips.domain;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 2/8/16.
 */
public class Trip implements Serializable { //Serializable allows objects of this class to be passed through intent.putExtra
    private String name;
    private String startDate;
    private String endDate;
    private List<Location> locations = new ArrayList();

    public Trip() {
        name = "";
        startDate = "";
        endDate = "";
    }

    public Trip(String name, String startDate, String endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isComplete() {
        return name.compareTo("")!=0 && startDate.compareTo("")!=0 && endDate.compareTo("")!=0;
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
