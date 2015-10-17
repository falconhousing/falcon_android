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
import com.locon.withu.models.Channel;
import com.locon.withu.models.Story;

import java.util.ArrayList;
import java.util.List;

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
        setItemDetails(position, holder);
        return convertView;
    }

    private void setItemDetails(int position, ChannelViewHolder holder) {
        Channel channel = channels.get(position);
        Uri uri = Uri.parse("https://devimages-housing.s3.amazonaws.com/profile_pictures/d85056e6-8cb2-43c4-89f7-3129d713a130/10297708_10204061098076651_794995807567807314_n.jpg");
        holder.mAvatarView.setImageURI(uri);
        holder.mPersonTextView.setText(channel.name);
        List<Story> stories = channel.stories;
        int storySize = stories.size();
        if (storySize < 2) {
            holder.mMoreTracks.setVisibility(View.GONE);
        } else {
            holder.mMoreTracks.setText((storySize - 2) + " more Tracks");
        }

        if (storySize >= 1) {
            holder.mTrackName1.setText(stories.get(0).name);
        }

        if (storySize >= 2) {
            holder.mTrackName2.setText(stories.get(1).name);
        }

    }

    public void updateContent(ArrayList<Channel> channels) {
        this.channels = channels;
        notifyDataSetChanged();
    }

    public static class ChannelViewHolder {
        public SimpleDraweeView mAvatarView;
        public TextView mPersonTextView;
        public TextView mTrackName1;
        public TextView mTrackName2;
        public TextView mMoreTracks;
        public ImageView mArrowButton;
        public Button mPlayButton;
        public Button mFollowButton;

        public ChannelViewHolder(View view) {
            mAvatarView = (SimpleDraweeView) view.findViewById(R.id.drawee_view_avatar);
            mPersonTextView = (TextView) view.findViewById(R.id.tvUsername);
            mTrackName1 = (TextView) view.findViewById(R.id.tvTrackName1);
            mTrackName2 = (TextView) view.findViewById(R.id.tvTrackName2);
            mMoreTracks = (TextView) view.findViewById(R.id.tvMoreTracks);
            mArrowButton = (ImageView) view.findViewById(R.id.image_like);
            mPlayButton = (Button) view.findViewById(R.id.btn_play);
            mFollowButton = (Button) view.findViewById(R.id.btn_follow);
        }
    }
}