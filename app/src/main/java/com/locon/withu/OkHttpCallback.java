package com.locon.withu;

import android.os.Handler;
import android.os.Message;

import com.locon.withu.utils.Logger;
import com.locon.withu.utils.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public abstract class OkHttpCallback<Wrapper> extends Handler implements Callback {

    private static final int FAILURE = 0;
    private static final int RESPONSE = 1;
    private static final int SESSION_EXPIRED = 2;

    public static final int SESSION_EXPIRE_CODE = 401;

    private final Class<Wrapper> mWrapperClass;

    protected OkHttpCallback(Class<Wrapper> wrapperClass) {
        this.mWrapperClass = wrapperClass;
    }

    @Override
    public final void onFailure(Request request, IOException e) {
        FailureParams params = new FailureParams();
        params.mRequest = request;
        params.mException = e;
        obtainMessage(FAILURE, params).sendToTarget();
    }

    @Override
    public final void onResponse(Response response) throws IOException {
        Wrapper wrapperObj = null;
        if (null != response) {
            if (response.isSuccessful() && !mWrapperClass.equals(Void.class)) {
                wrapperObj = Utils.parse(response, mWrapperClass);
            }
            if (SESSION_EXPIRE_CODE == response.code()) {
                obtainMessage(SESSION_EXPIRED).sendToTarget();
            }
        }
        ResponseParams<Wrapper> params = new ResponseParams();
        params.mResponse = response;
        params.mWrapperObj = wrapperObj;
        obtainMessage(RESPONSE, params).sendToTarget();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case FAILURE: {
                FailureParams params = (FailureParams) msg.obj;
                Logger.logd(this, "exception : " + params.mException);
                setFailure(params.mRequest, params.mException);
                break;
            }
            case RESPONSE: {
                ResponseParams<Wrapper> params = (ResponseParams) msg.obj;
                Logger.logd(this, "response : " + params.mResponse);
                Logger.logd(this, "wrapper : " + params.mWrapperObj);
                try {
                    setResponse(params.mResponse, params.mWrapperObj);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case SESSION_EXPIRED: {
                sessionExpired();
                break;
            }
        }
    }

    public abstract void setFailure(Request request, IOException e);

    public abstract void setResponse(Response response, Wrapper wrapper) throws IOException;

    public abstract void sessionExpired();

    private static class ResponseParams<Wrapper> {
        Response mResponse;
        Wrapper mWrapperObj;
    }

    private static class FailureParams {
        Request mRequest;
        IOException mException;
    }
}