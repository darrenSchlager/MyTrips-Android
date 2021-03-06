package com.regis.darren.mytrips.service;

import android.database.Cursor;

import com.regis.darren.mytrips.domain.ActivityItem;
import com.regis.darren.mytrips.domain.Location;
import com.regis.darren.mytrips.domain.Trip;

import java.util.List;

/**
 * Created by Darren on 2/22/16.
 */
public interface ITripSvc {
    public Trip create(Trip trip) throws Exception;
    public List<Trip> retrieveAll() throws Exception;
    public Cursor getCursor() throws Exception;
    public Trip update(Trip trip) throws Exception;
    public Trip delete(Trip trip) throws Exception;
}
