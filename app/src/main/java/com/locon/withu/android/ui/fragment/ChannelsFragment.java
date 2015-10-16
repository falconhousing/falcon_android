package com.locon.withu.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.locon.withu.R;
import com.locon.withu.android.ui.ChannelFetcherTask;
import com.locon.withu.android.ui.android.adapters.ChannelAdapter;
import com.locon.withu.models.Channel;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ChannelsFragment extends Fragment implements ChannelFetcherTask.ChannelsFetcherListener {

    @InjectView(R.id.lvChannels)
    ListView lvChannels;

    private ChannelAdapter adapter;

    public static ChannelsFragment newInstance() {
        ChannelsFragment fragment = new ChannelsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_channels, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        fetchChannels();
    }

    private void initViews() {
        adapter = new ChannelAdapter(getContext(), null);
        lvChannels.setAdapter(adapter);
    }

    private void fetchChannels() {
        ChannelFetcherTask task = new ChannelFetcherTask(this);
        task.execute();
    }

    @Override
    public void onChannelsFetched(ArrayList<Channel> channels) {
        adapter.updateContent(channels);
    }
}
