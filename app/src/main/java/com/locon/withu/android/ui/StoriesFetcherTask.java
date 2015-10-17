package com.locon.withu.android.ui;

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

    private StoriesFetcherListener listener;

    public StoriesFetcherTask(StoriesFetcherListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Story> doInBackground(Void... params) {
        String url = Constants.GET_STORIES_URL;
        Response response = NetworkUtils.doGetCall(url);
        ArrayList<Story> stories = Utils.parse(response, StoryWrapper.class).stories;
        return stories;
    }

    @Override
    protected void onPostExecute(ArrayList<Story> stories) {
        if (stories != null) {
            if (listener != null) {
                listener.onStoriesFetched(stories);
                listener = null;
            }
        }
    }

    public interface StoriesFetcherListener {

        void onStoriesFetched(ArrayList<Story> stories);
    }
}
