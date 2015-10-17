package com.locon.withu.android.ui;

import android.net.Uri;
import android.os.AsyncTask;

import com.locon.withu.Constants;
import com.locon.withu.models.Channel;
import com.locon.withu.models.ChannelWrapper;
import com.locon.withu.utils.NetworkUtils;
import com.locon.withu.utils.Utils;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

/**
 * Created by yogesh on 17/10/15.
 */
public class ChannelFetcherTask extends AsyncTask<Void, Void, ArrayList<Channel>> {

    private final double mLatitude;
    private final double mLongitude;
    private ChannelsFetcherListener listener;

    public ChannelFetcherTask(ChannelsFetcherListener listener) {
        this.listener = listener;
        mLatitude = 19.12;
        mLongitude = 72.91;
    }

    public ChannelFetcherTask(ChannelsFetcherListener listener, double latitude, double longitude) {
        this.listener = listener;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    @Override
    protected ArrayList<Channel> doInBackground(Void... params) {
        String url = Constants.GET_CHANNELS_URL;
        Uri uri = Uri.parse(url).buildUpon().appendQueryParameter("latitude", mLatitude + "").appendQueryParameter("longitude", mLongitude + "").build();
        Response response = NetworkUtils.doGetCall(uri.toString());
        ArrayList<Channel> Channels = Utils.parse(response, ChannelWrapper.class).channels;
        return Channels;
    }

    @Override
    protected void onPostExecute(ArrayList<Channel> Channels) {
        if (Channels != null)
            listener.onChannelsFetched(Channels);
    }

    public interface ChannelsFetcherListener {

        void onChannelsFetched(ArrayList<Channel> Channels);
    }
}
