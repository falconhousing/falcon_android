package com.locon.withu.android.ui;

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

    private ChannelsFetcherListener listener;

    public ChannelFetcherTask(ChannelsFetcherListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Channel> doInBackground(Void... params) {
        String url = Constants.GET_CHANNELS_URL;
        Response response = NetworkUtils.doGetCall(url);
        ArrayList<Channel> Channels = Utils.parse(response, ChannelWrapper.class).stories;
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
