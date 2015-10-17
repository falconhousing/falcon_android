package com.locon.withu.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
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
    private static final MediaType MEDIA_TYPE_MPEG = MediaType.parse("audio/mpeg");

    public static boolean isNetworkConnected(Context context) {
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

    public static void doPostCall(String url, String jsonRequestObject,
                                  OkHttpCallback cb) {
        url = appendAuthToken(url);
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

    public static void doGetCall(String url, OkHttpCallback cb) {
        Logger.logd(NetworkUtils.class, "Url : " + url);
        url = appendAuthToken(url);
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

    public static Response initMultipartUpload(String url, String filepath, Location location) {
        url = appendAuthToken(url);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(60, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(60, TimeUnit.SECONDS);
        MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
        multipartBuilder.addFormDataPart("acl", "public");
        multipartBuilder.addFormDataPart("Content-Type", "audio/mp3");
        multipartBuilder.addFormDataPart("latitude", location.getLatitude() + "");
        multipartBuilder.addFormDataPart("longitude", location.getLongitude() + "");
        multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\""),
                RequestBody.create(MEDIA_TYPE_MPEG, new File(filepath)));
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
        url = appendAuthToken(url);
        Logger.logd(NetworkUtils.class, "Saraj : " + url);
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
        url = appendAuthToken(url);
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

    public static String appendAuthToken(String url) {
        String authToken = PrefsHelper.getString(Constants.PREF_KEY_AUTH_TOKEN, "");
        if (TextUtils.isEmpty(authToken)) {
            return url;
        } else {
            return Uri.parse(url).buildUpon().appendQueryParameter("login_auth_token", authToken).build().toString();
        }
    }
}
