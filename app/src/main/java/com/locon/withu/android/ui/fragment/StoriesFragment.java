package com.locon.withu.android.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.locon.withu.R;
import com.locon.withu.android.ui.StoriesFetcherTask;
import com.locon.withu.android.ui.android.adapters.StoriesAdapter;
import com.locon.withu.downloader.DefaultRetryPolicy;
import com.locon.withu.downloader.DownloadRequest;
import com.locon.withu.downloader.DownloadStatusListener;
import com.locon.withu.downloader.ThinDownloadManager;
import com.locon.withu.models.Story;
import com.locon.withu.utils.Logger;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class StoriesFragment extends Fragment implements StoriesFetcherTask.StoriesFetcherListener, StoriesAdapter.DownloadRequestListener {

    @InjectView(R.id.lvStories)
    ListView lvStories;

    private StoriesAdapter adapter;


    public static StoriesFragment newInstance() {
        StoriesFragment fragment = new StoriesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stories, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        fetchStories();
    }

    private void initViews() {
        adapter = new StoriesAdapter(getContext(), null);
        adapter.setDownloadRequestListener(this);
        lvStories.setAdapter(adapter);
    }

    private void fetchStories() {
        StoriesFetcherTask task = new StoriesFetcherTask(this);
        task.execute();
    }

    @Override
    public void onStoriesFetched(ArrayList<Story> stories) {
        adapter.updateContent(stories);
    }


    MyDownloadStatusListener downloadStatusListener =
            new MyDownloadStatusListener();

    private ThinDownloadManager manager;

    @Override
    public void onDownloadRequest(Story story) {
        if (manager == null)
            manager = new ThinDownloadManager();
        manager.cancelAll();
        Uri downloadUri = Uri.parse(story.audio_url);
        Uri destinationUri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + story.id + ".3gp");
        DownloadRequest mRequest = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.LOW)
                .setRetryPolicy(new DefaultRetryPolicy()).setDownloadId(story.id + "")
                .setDownloadListener(downloadStatusListener);
        manager.add(mRequest);
    }

    class MyDownloadStatusListener implements DownloadStatusListener {

        @Override
        public void onDownloadComplete(String id) {
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + id + ".3gp");
            File file = new File(uri.getPath());
            if (file.exists()) {
                Intent i = new Intent();
                i.setAction(android.content.Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(file), "video/*");
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();

                }
            }
        }

        @Override
        public void onDownloadFailed(String id, int errorCode, String errorMessage) {

        }

        @Override
        public void onProgress(String id, long totalBytes, long downloadedBytes, int progress) {
            Logger.logd("DSD", "FS");
        }

    }

}
