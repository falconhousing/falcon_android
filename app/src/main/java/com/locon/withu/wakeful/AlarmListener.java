package com.locon.withu.wakeful;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.locon.withu.MainApplication;

public class AlarmListener
        implements WakefulIntentService.AlarmListener {

    @Override
    public void scheduleAlarms(AlarmManager mgr, PendingIntent pi, Context ctxt) {
        mgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), MainApplication.REPEAT_INTERVAL, pi);
    }

    @Override
    public void sendWakefulWork(Context ctxt) {
        WakefulIntentService.sendWakefulWork(ctxt, AlarmResponseService.class);
    }

    @Override
    public long getMaxAge() {
        return 2 * MainApplication.REPEAT_INTERVAL;
    }
}
