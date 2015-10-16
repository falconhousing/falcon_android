package com.locon.withu;

import android.app.Application;


public class MainApplication extends Application {

    private static MainApplication instance;

    public MainApplication() {
        instance = this;
    }

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
