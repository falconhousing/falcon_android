package com.locon.withu.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChannelWrapper {
    @SerializedName("user_audios")
    public ArrayList<Channel> channels;

}
