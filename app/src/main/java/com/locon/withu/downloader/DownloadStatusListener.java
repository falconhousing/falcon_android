package com.locon.withu.downloader;

public interface DownloadStatusListener {

    //Callback when download is successfully completed
    void onDownloadComplete(String id);

    //Callback if download is failed. Corresponding error code and error messages are provided
    void onDownloadFailed(String id, int errorCode, String errorMessage);

    //Callback provides download progress
    void onProgress(String id, long totalBytes, long downloadedBytes, int progress);
}
