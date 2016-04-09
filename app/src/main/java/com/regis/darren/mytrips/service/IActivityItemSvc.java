package com.regis.darren.mytrips.service;

import android.database.Cursor;

import com.regis.darren.mytrips.domain.ActivityItem;

import java.util.List;

/**
 * Created by Darren on 3/30/16.
 */
public interface IActivityItemSvc {
    public ActivityItem create(ActivityItem activityItem) throws Exception;
    public List<ActivityItem> retrieveAll(int locationId) throws Exception;
    public Cursor getCursor(int locationId) throws Exception;
    public ActivityItem update(ActivityItem activityItem) throws Exception;
    public ActivityItem delete(ActivityItem activityItem) throws Exception;
}
