package com.locon.withu.android.ui.activities;

import android.location.Location;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alexbbb.uploadservice.MultipartUploadRequest;
import com.locon.withu.Constants;
import com.locon.withu.R;
import com.locon.withu.android.ui.android.adapters.MyPagerAdapter;
import com.locon.withu.location.LocationProvider;
import com.locon.withu.uploader.Uploader;
import com.locon.withu.utils.Logger;
import com.locon.withu.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LocationProvider.LocationProviderListener {

    @InjectView(R.id.pager_main)
    ViewPager mViewPager;

    @InjectView(R.id.tab_layout)
    TabLayout mTabLayout;


    @InjectView(R.id.fab)
    FloatingActionButton recordButton;


    private static final String LOG_TAG = "MainActivity";
    private LocationProvider mLocationProvider;
    private ArrayList<String> mFragmentNames;
    private String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        mLocationProvider = new LocationProvider(this, this);
        mLocationProvider.requestLocation();
        bindViews();
    }

    private void bindViews() {
        mFragmentNames = new ArrayList<String>(0);
        mFragmentNames.add(Constants.FragmentNames.STORIES_FRAGMENT);
        mFragmentNames.add(Constants.FragmentNames.CHANNELS_FRAGMENT);
        FragmentPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragmentNames);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void makeFileUploadRequest(Location location) {
        String url = NetworkUtils.appendAuthToken(Constants.AUDIO_UPLOAD_URL);
        MultipartUploadRequest request = Uploader.createRequest(this, url, "2138991");
        Uploader.uploadMultipart(request, mFilePath, location);
        //NetworkUtils.initMultipartUpload(Constants.AUDIO_UPLOAD_URL, mFilePath, location);
    }

    @Override
    public void onLocationFetched(Location location) {
        Logger.logd(LOG_TAG, "Got new location: latitude: " + location.getLatitude() + " longitude: " + location.getLongitude() + " altitude: " + location.getAltitude());
        if (location != null) {
            makeFileUploadRequest(location);
        }
    }

    @Override
    public void onRequestFailed() {

    }


    private boolean isRecording = false;

    private MediaRecorder mRecorder = null;

    private String fileName;

    @OnClick(R.id.fab)
    public void onRecordButtonClick(View view) {
        if (isRecording) {
            isRecording = false;
            stopRecording();
            recordButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_btn_speak_now));
        } else {
            isRecording = true;
            startRecording();
            recordButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
        }
    }

    private void startRecording() {
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName += "/" + System.currentTimeMillis() + ".3gp";
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("audio recorder", "prepare() failed");
        }
        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        saveRecording();
    }

    private void saveRecording() {
        Toast.makeText(this, "filename is " + fileName, Toast.LENGTH_LONG).show();
        mFilePath = fileName;
        mLocationProvider = new LocationProvider(this, this);
        mLocationProvider.requestLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }
}
