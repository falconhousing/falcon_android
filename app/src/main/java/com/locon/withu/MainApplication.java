package com.locon.withu;

import android.app.Application;
import android.text.TextUtils;

import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.locon.withu.utils.PrefsHelper;
import com.locon.withu.wakeful.AlarmListener;
import com.locon.withu.wakeful.AlarmResponseService;
import com.locon.withu.wakeful.Manager;
import com.squareup.okhttp.OkHttpClient;


public class MainApplication extends Application {

    private static MainApplication instance;
    public static final long REPEAT_INTERVAL = 1 * 60 * 1000;

    public MainApplication() {
        instance = this;
    }

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PrefsHelper.init(this);
        FacebookSdk.sdkInitialize(this);
        initFresco();
        Manager.getInstance().init(this);
    }

    private void initFresco() {
        OkHttpClient okHttpClient = new OkHttpClient();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, okHttpClient)
                .build();
        Fresco.initialize(this, config);
        initialiseIntentService();
    }

    public static boolean isLoggedIn() {
        return !TextUtils.isEmpty(PrefsHelper.getString(Constants.PREF_KEY_AUTH_TOKEN, ""));
    }

    private void initialiseIntentService() {
        AlarmResponseService.scheduleAlarms(new AlarmListener(), this);

    }
}