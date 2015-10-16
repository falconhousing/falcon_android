package com.locon.withu.uploader;

import android.content.Context;
import android.util.Log;

import com.alexbbb.uploadservice.MultipartUploadRequest;

/**
 * Created by yogesh on 16/10/15.
 */
public class Uploader {

    public static MultipartUploadRequest createRequest(Context context, String url, String custom_upload_id) {
        return new MultipartUploadRequest(context, custom_upload_id, url);
    }

    public static void uploadMultipart(MultipartUploadRequest request) {

        request.addFileToUpload("/absolute/path/to/your/file",
                "parameter-name",
                "custom-file-name.extension",
                "content-type");

        request.setNotificationConfig(android.R.drawable.ic_menu_upload,
                "notification title",
                "upload in progress text",
                "upload completed successfully text",
                "upload error text",
                false);

        // set a custom user agent string for the upload request
        // if you comment the following line, the system default user-agent will be used
        request.setCustomUserAgent("UploadServiceDemo/1.0");

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

