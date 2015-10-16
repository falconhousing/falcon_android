package com.locon.withu.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Reader;

public class Utils {
    public static <T> T parse(Response response, Class<T> tClass) {

        Gson gson = new Gson();
        if (Logger.DEBUG_FLAG) {
            StringBuilder builder = new StringBuilder();
            Reader reader = null;
            try {
                reader = response.body().charStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int read;
            try {
                while ((read = reader.read()) != -1) {
                    builder.append((char) read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Logger.logd(Utils.class, "response is : " + builder);
            try {
                return gson.fromJson(builder.toString(), tClass);
            } catch (JsonSyntaxException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                return (gson.fromJson(response.body().charStream(), tClass));
            } catch (JsonSyntaxException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void logCustomException(Exception e, String s) {
        Log.e(s, "Exception occurred: " + e.getMessage());
    }
}
