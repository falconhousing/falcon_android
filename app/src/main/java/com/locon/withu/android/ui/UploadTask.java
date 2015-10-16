package com.locon.withu.android.ui;

import android.location.Location;
import android.os.AsyncTask;

import com.locon.withu.Constants;
import com.locon.withu.utils.NetworkUtils;

/**
 * Created by yogesh on 16/10/15.
 */

public class UploadTask extends AsyncTask<Void, Void, Void> {

    private String url;
    private String filePath;
    private Location location;

    public UploadTask(String url, String filePath, Location location) {
        this.filePath = filePath;
    }

    @Override
    protected Void doInBackground(Void... params) {
        NetworkUtils.initMultipartUpload(Constants.AUDIO_UPLOAD_URL, filePath, location);
        return null;
    }

}

