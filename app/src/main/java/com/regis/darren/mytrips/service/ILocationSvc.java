package com.regis.darren.mytrips.service;

import com.regis.darren.mytrips.domain.ActivityItem;
import com.regis.darren.mytrips.domain.Location;
import com.regis.darren.mytrips.domain.Trip;

import java.util.List;

/**
 * Created by Darren on 2/22/16.
 */
public interface ILocationSvc {
    public Location create(Location location) throws Exception;
    public List<Location> retrieve(int tripId) throws Exception;
    public Location update(Location location) throws Exception;
    public Location delete(Location location) throws Exception;
}
