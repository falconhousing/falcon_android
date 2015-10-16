package com.locon.withu.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.locon.withu.Constants;
import com.locon.withu.OkHttpCallback;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NetworkUtils {
    private static final String TAG = "locon.com.Utils.NetworkUtils";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    public static final boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return null != info && info.isConnected();
    }

    public static boolean isDisconnectedFromInternet(Activity target) {
        boolean notWorking = Constants.INTERNET_NOT_CONNECTED_MSG.equals(
                NetworkUtils.isConnectedToInternet(target));
        if (notWorking)
            Toast.makeText(target, Constants.INTERNET_NOT_CONNECTED_MSG,
                    Toast.LENGTH_SHORT)
                    .show();
        return notWorking;
    }

    /*
        Method to check if the device is connected to the internet or not...
    */
    public static String isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected())
            return ni.getTypeName();
        else
            return Constants.INTERNET_NOT_CONNECTED_MSG;
    }

    /*
        Method to check if WiFi connection is available...
     */
    public static boolean isConnectedWifi(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() &&
                info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /*
        Method that makes a post call using OkHttpLibrary
     */
    public static void doPostCall(String url, String jsonRequestObject,
                                  OkHttpCallback cb) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(15, TimeUnit.SECONDS);
        RequestBody body = RequestBody.create(JSON, jsonRequestObject);
        Request request = new Request.Builder().url(url)
                .header(Constants.CONTENT_TYPE_TAG, Constants.HTTP_HEADER_STR)
                .post(body).build();
        Call call = client.newCall(request);
        call.enqueue(cb);
    }

    /*
        Method that makes a get call using OkHttpLibrary
     */
    public static void doGetCall(String url, OkHttpCallback cb) {
        Logger.logd(NetworkUtils.class, "Url : " + url);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(15, TimeUnit.SECONDS);
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(cb);
    }

    /*
        Method for multipart upload
     */

    public static Response initMultipartUpload(String url, String filepath,
                                               String imageName, String imageType, String imagehash,
                                               Context context) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(60, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(60, TimeUnit.SECONDS);
//        String policy = PreferenceUtils.getPreferenceValue(context, "policy", null);
//        String signature = PreferenceUtils.getPreferenceValue(context, "signature", null);
//        String access_id = PreferenceUtils.getPreferenceValue(context, "access_id", null);
//        String acl = PreferenceUtils.getPreferenceValue(context, "acl", null);
//        String prefix = PreferenceUtils.getPreferenceValue(context, "prefix", null);
//        String s3_url = PreferenceUtils.getPreferenceValue(context, "s3_url", null);
//        String s3_key = prefix + "/" + imagehash + "/" + imageType + ".jpg";
//        Logger.logd(NetworkUtils.class, s3_key + " code");
        MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
//        multipartBuilder.addFormDataPart("policy", policy);
//        multipartBuilder.addFormDataPart("signature", signature);
//        multipartBuilder.addFormDataPart("AWSAccessKeyId", access_id);
//        multipartBuilder.addFormDataPart("Content-Type", "image/jpeg");
//        multipartBuilder.addFormDataPart("secure", "true");
//        multipartBuilder.addFormDataPart("acl", acl);
//        multipartBuilder.addFormDataPart("success_action_status", "201");
//        multipartBuilder.addFormDataPart("key", s3_key);
        multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\""),
                RequestBody.create(MEDIA_TYPE_JPG, new File(filepath)));
        RequestBody reqBody = multipartBuilder.build();
        Request request =
                new Request.Builder().url(url).post(reqBody).build();
        Call call = client.newCall(request);
        Response response = null;

        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }

    /*
        Do get call sync...
     */

    public static Response doGetCall(String url) {
        Logger.logd(NetworkUtils.class, "Url : " + url);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(15, TimeUnit.SECONDS);
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();

        } catch (IOException e) {
            Utils.logCustomException(e, "NetUtils doGetCall");
        }
        return response;
    }

    /*
        Do post call sync...
     */

    public static Response doPostCall(String url, String jsonRequestObject) {
        Logger.logd(NetworkUtils.class, "Url : " + url);
        Logger.logd(NetworkUtils.class, "jSon : " + jsonRequestObject);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(20, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(20, TimeUnit.SECONDS);
        RequestBody body = RequestBody.create(JSON, jsonRequestObject);
        Request request = new Request.Builder().url(url)
                .header(Constants.CONTENT_TYPE_TAG, Constants.HTTP_HEADER_STR)
                .post(body).build();
        Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Utils.logCustomException(e, "NetUtils doPostCall");
        }
        return response;
    }


    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWifi = null, networkInfoMobi = null;
        if (connectivityManager != null) {
            networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            networkInfoMobi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfoMobi.isConnected() || networkInfoWifi.isConnected())
                return true;
        }
        if (networkInfoMobi.isConnected() || networkInfoWifi.isConnected()) return true;
        else
            return false;
    }

}
