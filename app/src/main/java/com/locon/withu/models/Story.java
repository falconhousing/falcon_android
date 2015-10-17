package com.locon.withu.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yogesh on 16/10/15.
 */
public class Story {

    public int id;
    public String location;
    public String poi;
    public int user_id;
    public boolean isFollowingUser;
    public String audio_url;
    public int views;
    public String user_picture;
    public String created_at;
    public String name;


    public String getStoryTitle() {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String prefix = "";
        try {
            Date date = simpleFormat.parse(this.created_at);
            SimpleDateFormat finalFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(System.currentTimeMillis());
            int todayDay = today.get(Calendar.DAY_OF_MONTH);
            int todayMonth = today.get(Calendar.MONTH);
            int todayYear = today.get(Calendar.YEAR);

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            if (todayYear == year && todayMonth == month) {
                if (day == todayDay) {
                    prefix = "Created today, ";
                } else if (day + 1 == todayDay) {
                    prefix = "Created yesterday, ";
                } else {
                    prefix = finalFormat.format(date);
                    prefix = "Created on " + prefix;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return prefix;
    }
}
