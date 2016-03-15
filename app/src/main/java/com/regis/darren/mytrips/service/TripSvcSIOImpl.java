package com.regis.darren.mytrips.service;

import com.regis.darren.mytrips.domain.ActivityItem;
import com.regis.darren.mytrips.domain.Location;
import com.regis.darren.mytrips.domain.Trip;

import android.content.Context;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import android.util.Log;

/**
 * Created by Darren on 2/22/16.
 */

//Serialized IO
public class TripSvcSIOImpl implements ITripSvc {

    private static TripSvcSIOImpl instance = null; //Singleton Pattern: holds the object (at most one object can be created)
    private static final String FILE_NAME = "trips.sio";
    private static final String TAG = "TripSvcSIOImpl";
    private List<Trip> cache = new ArrayList();
    private Context context = null;

    public static TripSvcSIOImpl getInstance(Context context) throws Exception{ //Singleton Pattern: static method used to create the single object
        if(instance == null) {
            instance = new TripSvcSIOImpl(context);
        }
        return instance;

    }

    private TripSvcSIOImpl(Context context) throws Exception{ //Singleton Pattern: private constructor
        this.context = context;
        File file = new File(context.getFilesDir(), FILE_NAME);
        if(!file.exists()){
            writeFile();
        }
        readFile();
    }

    @Override
    public Trip create(Trip trip) throws Exception {

        cache.add(trip);
        writeFile();
        return trip;
    }

    @Override
    public List<Trip> retrieveAll() {
        return cache;
    }

    @Override
    public Trip update(Trip trip, int tripIndex) throws Exception {
        if(tripIndex >= 0 && tripIndex<cache.size()) {
            cache.set(tripIndex, trip);
            writeFile();
            return trip;
        }
        return null;
    }

    @Override
    public Trip delete(Trip trip, int tripIndex, int locationIndex, int activityItemIndex) {
        if(tripIndex >=0 && tripIndex<cache.size()) {
            Trip deletedTrip = new Trip(trip.getName(), trip.getStartDate(), trip.getEndDate());
            Trip t = cache.get(tripIndex);
            if(locationIndex>=0 && locationIndex<cache.get(tripIndex).getLocations().size()) {
                Location l = t.getLocations().get(locationIndex);
                List<Location> locations= new ArrayList();
                locations.add(new Location(l.getCity(), l.getStateCountry(), l.getArrive(), l.getDepart()));
                deletedTrip.setLocations(locations);
                if(activityItemIndex>=0 && activityItemIndex<=l.getActivityItems().size()) {
                    ActivityItem a = l.getActivityItems().get(activityItemIndex);
                    List<ActivityItem> activityItems = new ArrayList<>();
                    activityItems.add(new ActivityItem(a.getActivityName(), a.getDate(), a.getTime(), a.getDescription()));
                    deletedTrip.getLocations().get(locationIndex).setActivityItems(activityItems);
                    l.getActivityItems().remove(activityItemIndex);
                    return deletedTrip;
                }
                else {
                    t.getLocations().remove(locationIndex);
                    return deletedTrip;
                }
            }
            else {
                cache.remove(tripIndex);
                return deletedTrip;
            }
        }
        else {
            return null;
        }
    }

    private void readFile() throws Exception {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            cache = (List<Trip>)ois.readObject(); //serialize contents of the file into cache
            ois.close();
            fis.close();
        } catch(Exception e) {
            Log.e(TAG, "EXCEPTION: "+e.getMessage()); //log error
            throw e;
        }
    }

    private void writeFile() throws Exception {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cache); //serialize contents of cache into the file (List<Trip> cache and Trip both implements Serializable
            oos.close();
            fos.close();
        } catch(Exception e) {
            Log.e(TAG, "EXCEPTION: "+e.getMessage()); //log error
            throw e;
        }
    }

}
