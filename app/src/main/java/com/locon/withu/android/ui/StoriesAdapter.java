package com.locon.withu.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.locon.withu.R;

import java.util.ArrayList;

/**
 * Created by yogesh on 16/10/15.
 */
public class StoriesAdapter extends BaseAdapter {

    private ArrayList<Story> stories;

    private Context context;

    private final LayoutInflater inflater;

    public StoriesAdapter(Context context, ArrayList<Story> stories) {
        if (stories == null)
            this.stories = new ArrayList<>();
        this.context = context;
        this.stories = stories;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stories.size();
    }

    @Override
    public Object getItem(int position) {
        return stories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StoryViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_story, parent, false);
            holder = new StoryViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (StoryViewHolder) convertView.getTag();
        }
        setItemDetails();
        return convertView;
    }

    private void setItemDetails() {

    }

    public static class StoryViewHolder {

        public StoryViewHolder(View view) {

        }
    }
}