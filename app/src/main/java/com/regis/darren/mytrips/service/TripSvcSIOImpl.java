package com.regis.darren.mytrips.service;

import com.regis.darren.mytrips.domain.Trip;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
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

    public static TripSvcSIOImpl getInstance(Context context) { //Singleton Pattern: static method used to create the single object
        if(instance == null) {
            instance = new TripSvcSIOImpl(context);
        }
        return instance;

    }

    private TripSvcSIOImpl(Context context) { //Singleton Pattern: private constructor
        this.context = context;
        readFile();
    }

    @Override
    public Trip create(Trip trip) throws Exception {

        return trip;
    }

    @Override
    public List<Trip> retrieveAll() throws Exception {
        List<Trip> list = new ArrayList();

        return list;
    }

    @Override
    public Trip update(Trip trip) throws Exception {

        return trip;
    }

    @Override
    public Trip delete(Trip trip) throws Exception {

        return trip;
    }

    private void readFile() {

    }

    private void writeFile() {

    }

}
