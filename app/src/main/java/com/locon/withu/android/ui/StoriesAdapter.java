package com.locon.withu.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
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
        holder.mViewCountTextView.setText(story.views + "");
        holder.mLocationTextView.setText(story.location + "");
        holder.mPlayButton.setTag(story.audio_url);
        if (story.isFollowingUser) {
            holder.mFollowButton.setText("Following");
        } else {
            holder.mFollowButton.setText("Follow");
        }
        holder.mFollowButton.setTag(story.isFollowingUser);
    }

    public void updateContent(ArrayList<Story> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    public static class StoryViewHolder {
        private final TextView mLocationTextView;
        public SimpleDraweeView mAvatarView;
        public TextView mPersonTextView;
        public TextView mStoryTitleTextView;
        public TextView mViewCountTextView;
        public ImageView mLikeButton;
        public Button mPlayButton;
        public Button mFollowButton;

        public StoryViewHolder(View view) {
            mAvatarView = (SimpleDraweeView) view.findViewById(R.id.drawee_view_avatar);
            mPersonTextView = (TextView) view.findViewById(R.id.tvUsername);
            mStoryTitleTextView = (TextView) view.findViewById(R.id.tvDescription);
            mLocationTextView = (TextView) view.findViewById(R.id.tvLocation);
            mViewCountTextView = (TextView) view.findViewById(R.id.views_count);
            mLikeButton = (ImageView) view.findViewById(R.id.image_like);
            mPlayButton = (Button) view.findViewById(R.id.btn_play);
            mFollowButton = (Button) view.findViewById(R.id.btn_follow);
        }
    }
}