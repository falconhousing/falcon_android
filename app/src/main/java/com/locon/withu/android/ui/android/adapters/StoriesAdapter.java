package com.locon.withu.android.ui.android.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.locon.withu.R;
import com.locon.withu.models.Story;

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
        setItemDetails(convertView, position, holder);
        return convertView;
    }

    private void setItemDetails(View convertView, int position, StoryViewHolder holder) {
        final Story story = stories.get(position);
        Uri uri = Uri.parse(story.user_picture);
        holder.mAvatarView.setImageURI(uri);
        holder.mPersonTextView.setText(story.name);
        holder.mStoryTitleTextView.setText(story.getStoryTitle());
        holder.mViewCountTextView.setText(story.views + "");
        holder.mLocationTextView.setText(story.location + "");
        holder.mPlayButton.setTag(story.audio_url);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadRequestListener != null)
                    downloadRequestListener.onDownloadRequest(story);
            }
        });
    }

    public interface DownloadRequestListener {

        public void onDownloadRequest(Story story);
    }

    private DownloadRequestListener downloadRequestListener;

    public void setDownloadRequestListener(DownloadRequestListener listener) {
        this.downloadRequestListener = listener;
    }


    public void updateContent(ArrayList<Story> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    public static class StoryViewHolder {
        public TextView mLocationTextView;
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
            mStoryTitleTextView = (TextView) view.findViewById(R.id.tvTrackName);
            mLocationTextView = (TextView) view.findViewById(R.id.tvLocation);
            mViewCountTextView = (TextView) view.findViewById(R.id.views_count);
            mLikeButton = (ImageView) view.findViewById(R.id.image_like);
            mPlayButton = (Button) view.findViewById(R.id.btn_play);
        }
    }
}