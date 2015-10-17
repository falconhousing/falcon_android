package com.locon.withu.android.ui;

import android.net.Uri;
import android.os.AsyncTask;

import com.locon.withu.Constants;
import com.locon.withu.models.Story;
import com.locon.withu.models.StoryWrapper;
import com.locon.withu.utils.NetworkUtils;
import com.locon.withu.utils.Utils;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

/**
 * Created by yogesh on 17/10/15.
 */
public class StoriesFetcherTask extends AsyncTask<Void, Void, ArrayList<Story>> {

    private final double mLatitude;
    private final double mLongitude;
    private StoriesFetcherListener listener;

    public StoriesFetcherTask(StoriesFetcherListener listener) {
        this.listener = listener;
        mLatitude = 19.12;
        mLongitude = 72.91;
    }

    public StoriesFetcherTask(StoriesFetcherListener listener, double latitude, double longitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    @Override
    protected ArrayList<Story> doInBackground(Void... params) {
        String url = Constants.GET_STORIES_URL;
        Uri uri = Uri.parse(url).buildUpon().appendQueryParameter("latitude",mLatitude + "").appendQueryParameter("longitude",mLongitude + "").build();
        Response response = NetworkUtils.doGetCall(uri.toString());
        ArrayList<Story> stories = Utils.parse(response, StoryWrapper.class).stories;
        System.out.print(stories.size());
        return stories;
    }

    @Override
    protected void onPostExecute(ArrayList<Story> stories) {
        if (stories != null)
            listener.onStoriesFetched(stories);
    }

    public interface StoriesFetcherListener {

        void onStoriesFetched(ArrayList<Story> stories);
    }
}
