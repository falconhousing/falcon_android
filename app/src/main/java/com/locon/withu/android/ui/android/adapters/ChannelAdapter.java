package com.locon.withu.android.ui.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.locon.withu.R;
import com.locon.withu.models.Channel;

import java.util.ArrayList;

/**
 * Created by yogesh on 16/10/15.
 */
public class ChannelAdapter extends BaseAdapter {

    private ArrayList<Channel> channels;

    private Context context;

    private final LayoutInflater inflater;

    public ChannelAdapter(Context context, ArrayList<Channel> channels) {
        if (channels == null)
            this.channels = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return channels.size();
    }

    @Override
    public Object getItem(int position) {
        return channels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChannelViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_channel, parent, false);
            holder = new ChannelViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChannelViewHolder) convertView.getTag();
        }
        setItemDetails();
        return convertView;
    }

    private void setItemDetails() {

    }

    public void updateContent(ArrayList<Channel> channels) {
        this.channels = channels;
        notifyDataSetChanged();
    }

    public static class ChannelViewHolder {

        public ChannelViewHolder(View view) {

        }
    }
}