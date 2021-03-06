package com.locon.withu;

public class Constants {
    public static final String INTERNET_NOT_CONNECTED_MSG = "You are Not Connected to the internet";
    public static final String CONTENT_TYPE_TAG = "Content-Type";
    public static final String HTTP_HEADER_STR = "application/json";
    public static final String KEY_RECORDED_FILE_URI = "recorded_audio_file";
    public static final int REQUEST_CODE_RECORD = 23213;
    public static final String PREF_KEY_AUTH_TOKEN = "auth_token";

    public static class FragmentNames {
        public static final String STORIES_FRAGMENT = "STORIES";
        public static final String CHANNELS_FRAGMENT = "CHANNELS";
        public static final String LOCATIONS_FRAGMENT = "LOCATIONS";
    }

    public static final String AUDIO_UPLOAD_URL = "http://dharmendrav.housing.com:4000/audios.json";
    public static final String GET_STORIES_URL = "http://dharmendrav.housing.com:4000/get_feed/";
    public static final String GET_CHANNELS_URL = "http://dharmendrav.housing.com:4000/get_feed_grouped/";

}
