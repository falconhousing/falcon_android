package com.locon.withu.android.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alexbbb.uploadservice.MultipartUploadRequest;
import com.locon.withu.Constants;
import com.locon.withu.R;
import com.locon.withu.android.ui.android.adapters.MyPagerAdapter;
import com.locon.withu.location.LocationProvider;
import com.locon.withu.uploader.Uploader;
import com.locon.withu.utils.Logger;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements LocationProvider.LocationProviderListener {

    @InjectView(R.id.pager_main)
    ViewPager mViewPager;

    @InjectView(R.id.tab_layout)
    TabLayout mTabLayout;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivityForResult(new Intent(MainActivity.this, AudioRecordActivity.class), Constants.REQUEST_CODE_RECORD);
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_CODE_RECORD:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    String fileName = extras.getString(Constants.KEY_RECORDED_FILE_URI);
                    Toast.makeText(this, "filename is " + fileName, Toast.LENGTH_LONG).show();
                    mFilePath = fileName;
                    Logger.logd("", fileName);
                    mLocationProvider = new LocationProvider(this, this);
                    mLocationProvider.requestLocation();
                }
        }
    }

    private void makeFileUploadRequest(Location location) {
        MultipartUploadRequest request = Uploader.createRequest(this, Constants.AUDIO_UPLOAD_URL, "2138991");
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
}
