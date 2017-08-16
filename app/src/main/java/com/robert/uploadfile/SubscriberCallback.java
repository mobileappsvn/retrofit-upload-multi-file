package com.robert.uploadfile;

/**
 * Created by robert on 8/16/17.
 */


import android.util.Log;

//import retrofit2.adapter.rxjava2.HttpException;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class SubscriberCallback<T extends ResponseBody> extends Subscriber<T> {
    private CallbackListener<T> callbackListener;
    private int requestCode;

    public SubscriberCallback(CallbackListener<T> callbackListener, int requestCode) {
        this.callbackListener = callbackListener;
        this.requestCode = requestCode;
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.e("onError", e.getMessage());

        if (this.callbackListener != null) {
            callbackListener.onFailure(((HttpException)e).code(), e);
        }
    }

    @Override
    public void onNext(T response) {
        Log.i("SubscriberCallback", "SubscriberCallback --------------------> onSuccess: " + response.getClass().getSimpleName());
        if (this.callbackListener != null)
            callbackListener.onSuccess(response.code, response, requestCode);
    }

    @Override
    public void onCompleted() {
        Log.i("SubscriberCallback", "SubscriberCallback  --------------------> onCompleted");
        if (this.callbackListener != null)
            this.callbackListener.onFinish(requestCode);
    }
}