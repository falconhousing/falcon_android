package com.locon.withu.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

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
        setItemDetails(position, holder);
        return convertView;
    }

    private void setItemDetails(int position, StoryViewHolder holder) {
        Story story = stories.get(position);
        holder.tvViews.setText(story.views + "");
        holder.tvLocation.setText(story.location + "");
    }

    public void updateContent(ArrayList<Story> stories) {
        this.stories = stories;
         notifyDataSetChanged();
    }

    public static class StoryViewHolder {

        public TextView tvViews;
        public ImageButton ibPlay;
        public ImageButton ibFollow;
        public TextView tvLocation;

        public StoryViewHolder(View view) {
            tvViews = (TextView) view.findViewById(R.id.tvViews);
            tvLocation = (TextView) view.findViewById(R.id.tvLocation);
            ibPlay = (ImageButton) view.findViewById(R.id.ibPlay);
            ibFollow = (ImageButton) view.findViewById(R.id.ibFollow);

        }
    }
}