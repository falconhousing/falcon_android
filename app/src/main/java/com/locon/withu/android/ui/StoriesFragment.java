package com.locon.withu.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.locon.withu.R;
import com.locon.withu.android.ui.android.adapters.StoriesFetcherTask;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class StoriesFragment extends Fragment implements StoriesFetcherTask.StoriesFetcherListener {

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
}
