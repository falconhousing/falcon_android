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

import com.locon.withu.R;
import com.locon.withu.android.ui.StoriesFetcherTask;
import com.locon.withu.android.ui.activities.MainActivity;
import com.locon.withu.location.LocationProvider;
import com.locon.withu.models.Story;

import java.util.ArrayList;

public final class Manager implements LocationProvider.LocationProviderListener, StoriesFetcherTask.StoriesFetcherListener {
    private double mLatitude;
    private double mLongitude;
    private Context mContext;

    private Manager() {

    }

    private static class InstanceHolder {
        public static final Manager sInstance = new Manager();
    }

    public static Manager getInstance() {
        return InstanceHolder.sInstance;
    }

    public void init(Context context) {
        mContext = context;
    }

    public void startProcess() {
        LocationProvider provider = new LocationProvider(mContext, this);
        provider.requestLocation();
    }

    @Override
    public void onLocationFetched(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        makeFeedCall();
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
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        createNotification(pendingIntent);
    }

    private void createNotification(PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setContentTitle("New stories found for you");
        mBuilder.setContentText("Hey there! I'm shadow");
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
