package com.locon.withu.wakeful;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.locon.withu.MainApplication;
import com.locon.withu.R;
import com.locon.withu.android.ui.StoriesFetcherTask;
import com.locon.withu.android.ui.activities.MainActivity;
import com.locon.withu.location.LocationProvider;
import com.locon.withu.models.Story;

import java.util.ArrayList;

/**
 * Created by saraj on 24/09/15.
 */
public class AlarmResponseService extends WakefulIntentService implements LocationProvider.LocationProviderListener, StoriesFetcherTask.StoriesFetcherListener {

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
        Toast.makeText(this, "In service", Toast.LENGTH_SHORT).show();
        LocationProvider provider = new LocationProvider(this, this);
        provider.requestLocation();
        makeFeedCall();
    }

    @Override
    public void onLocationFetched(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    @Override
    public void onRequestFailed() {

    }

    public void makeFeedCall() {
        StoriesFetcherTask task = new StoriesFetcherTask(this, mLatitude, mLongitude);
        task.execute();
    }

    @Override
    public void onStoriesFetched(ArrayList<Story> stories) {
        if (stories == null || stories.isEmpty()) {
            return;
        }
        notifyIfRequired();
    }

    public void notifyIfRequired() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        createNotification(pendingIntent);
    }

    private void createNotification(PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("New stories found for you");
        mBuilder.setContentText("Hey there! I'm Shadow");
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText("Hey there! I'm shadow"));
        mBuilder.setSmallIcon(R.drawable.com_facebook_button_icon);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(soundUri);
        mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        // Using subscriptionCase as notification ID
        notificationManager
                .notify(1, mBuilder.build());
    }
}
