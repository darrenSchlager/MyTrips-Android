package com.regis.darren.mytrips.service;

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
    public Trip update(Trip trip, int tripIndex) throws Exception;
    public Trip delete(Trip trip, int tripIndex, int locationIndex, int activityItemIndex);
}
