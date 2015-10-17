package com.locon.withu.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by yogesh on 17/10/15.
 */
public class ChannelWrapper {
    @SerializedName("user_audios")
    public ArrayList<Channel> channels;

}
