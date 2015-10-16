package com.locon.withu.location;

import android.location.Location;

import com.google.android.gms.location.LocationRequest;

/**
 * Created by yogesh on 28/08/15.
 */
public class LocationProviderOptions {

    // default values
    private static final int DEFAULT_BEST_ACCURACY = 100;
    private static final int DEFAULT_INTERVAL = 1000;
    private static final int DEFAULT_FAST_INTERVAL = 500;

    public int getPriority() {
        return mPriority;
    }

    public int getInterval() {
        return mInterval;
    }

    public int getFastInterval() {
        return mFastInterval;
    }

    private int mPriority;
    private int mInterval;
    private int mFastInterval;
    private int mBestAccuracy;


    public LocationProviderOptions() {
        mPriority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
        mInterval = DEFAULT_INTERVAL;
        mFastInterval = DEFAULT_FAST_INTERVAL;
        mBestAccuracy = DEFAULT_BEST_ACCURACY;
    }

    public LocationProviderOptions(int priority, int interval, int fastInterval, int bestAccuracy) {
        mPriority = priority;
        mInterval = interval;
        mFastInterval = fastInterval;
        mBestAccuracy = bestAccuracy;
    }

    public void setPriority(int priority) {
        this.mPriority = priority;
    }

    public void setmnterval(int interval) {
        this.mInterval = interval;
    }

    public void setFastInterval(int fastInterval) {
        this.mFastInterval = fastInterval;
    }


    public boolean isBestApproximation(Location location) {
        return location.getAccuracy() <= mBestAccuracy;
    }
}
