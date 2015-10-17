package com.locon.withu.wakeful;

import android.content.Intent;

import com.locon.withu.MainApplication;

/**
 * Created by saraj on 24/09/15.
 */
public class AlarmResponseService extends WakefulIntentService {
    public static final String NAME = "AlarmResponseService";
    private double mLatitude;
    private double mLongitude;

    public AlarmResponseService() {
        super(NAME);
    }

    public AlarmResponseService(String name) {
        super(name);
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        if (!MainApplication.isLoggedIn()) {
            return;
        }
        Manager.getInstance().startProcess();
    }

}

