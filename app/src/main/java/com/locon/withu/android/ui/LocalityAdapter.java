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
class LocalityAdapter extends BaseAdapter {

    private ArrayList<Locality> localities;

    private Context context;

    private final LayoutInflater inflater;

    public LocalityAdapter(Context context, ArrayList<Locality> localities) {
        if (localities == null)
            this.localities = localities;
        this.context = context;
        this.localities = localities;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return localities.size();
    }

    @Override
    public Object getItem(int position) {
        return localities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LocalityViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_locality, parent, false);
            holder = new LocalityViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (LocalityViewHolder) convertView.getTag();
        }
        setItemDetails();
        return convertView;
    }

    private void setItemDetails() {

    }

    public static class LocalityViewHolder {

        public LocalityViewHolder(View view) {

        }
    }

}