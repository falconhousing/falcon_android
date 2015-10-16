package com.locon.withu.uploader;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.alexbbb.uploadservice.MultipartUploadRequest;

import java.io.File;

/**
 * Created by yogesh on 16/10/15.
 */
public class Uploader {

    public static MultipartUploadRequest createRequest(Context context, String url, String custom_upload_id) {
        return new MultipartUploadRequest(context, custom_upload_id, url);
    }

    public static void uploadMultipart(MultipartUploadRequest request, String filePath, Location location) {
        File file = new File(filePath);
        request.addFileToUpload(filePath,
                "audio",
                file.getName(),
                "audio/3gpp");

        request.setNotificationConfig(android.R.drawable.ic_menu_upload,
                "notification title",
                "upload in progress text",
                "upload completed successfully text",
                "upload error text",
                false);

        request.addParameter("acl", "public");
        request.addParameter("latitude", location.getLatitude() + "");
        request.addParameter("longitude", location.getLongitude() + "");
        // set the maximum number of automatic upload retries on error
        request.setMaxRetries(2);

        try {
            //Start upload service and display the notification
            request.startUpload();

        } catch (Exception exc) {
            //You will end up here only if you pass an incomplete upload request
            Log.e("AndroidUploadService", exc.getLocalizedMessage(), exc);
        }
    }
}

