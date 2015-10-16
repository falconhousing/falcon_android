package com.locon.withu.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationProvider extends Handler implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    /*=====================================================================*/

    private static final int PUBLISH_CURRENT_LOCATION = 0;
    private static final int PUBLISH_REQUEST_FAILED = 1;

    private Context mContext;
    private LocationProviderOptions mProviderOptions;
    private GoogleApiClient mGoogleApiClient;
    private LocationProviderListener mProviderListener;

    public LocationProvider(Context context, LocationProviderListener listener) {
        this.mContext = context;
        this.mProviderOptions = new LocationProviderOptions();
        this.mProviderListener = listener;
        initGoogleApiClient();
    }

    public LocationProvider(Context mContext, LocationProviderOptions mOptions, LocationProviderListener listener) {
        this.mContext = mContext;
        this.mProviderOptions = mOptions;
        this.mProviderListener = listener;
        initGoogleApiClient();
    }

    private void initGoogleApiClient() {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(mContext);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        builder.addApi(LocationServices.API);
        mGoogleApiClient = builder.build();
    }

    public void setLocationProviderListener(LocationProviderListener mProviderListener) {
        this.mProviderListener = mProviderListener;
    }

    public void requestLocation() {
        connect();
    }

    /*=============================================================================*/

    @Override
    public void onLocationChanged(Location location) {
        if (mProviderOptions.isBestApproximation(location)) {
            obtainMessage(PUBLISH_CURRENT_LOCATION, location).sendToTarget();
            disconnect();
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locReq = new LocationRequest();
        locReq.setInterval(mProviderOptions.getInterval());
        locReq.setFastestInterval(mProviderOptions.getFastInterval());
        locReq.setPriority(mProviderOptions.getPriority());
        return locReq;
    }

    /*================================================================================*/

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation == null ||
                !mProviderOptions.isBestApproximation(lastLocation)) {
            LocationRequest request = createLocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);
        } else {
            obtainMessage(PUBLISH_CURRENT_LOCATION, lastLocation).sendToTarget();
            disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        obtainMessage(PUBLISH_REQUEST_FAILED).sendToTarget();
    }

    private void connect() {
        synchronized (mGoogleApiClient) {
            mGoogleApiClient.connect();
        }
    }

    private void disconnect() {
        synchronized (mGoogleApiClient) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
    }

    /*========================================================================================*/


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PUBLISH_CURRENT_LOCATION:
                if (msg.obj instanceof Location && mProviderListener != null)
                    mProviderListener.onLocationFetched((Location) msg.obj);
                break;
            case PUBLISH_REQUEST_FAILED:
                if (mProviderListener != null)
                    mProviderListener.onRequestFailed();
                break;
        }
    }


    public interface LocationProviderListener {

        void onLocationFetched(Location location);

        void onRequestFailed();

    }


    /*===========================================================================================*/
}
