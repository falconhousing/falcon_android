package com.locon.withu.downloader;

/**
 * Created by maniselvaraj on 15/4/15.
 */
public class RetryError extends Exception {

    public RetryError() {
        super("Maximum retry exceeded");
    }

    public RetryError(Throwable cause) {
        super(cause);
    }
}
