package com.robert.uploadfile.base.networking;

import android.util.Log;

import com.robert.uploadfile.base.response.ResponseBody;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import retrofit2.Response;

/**
 * Created by robert on 8/17/17.
 */

public abstract class CustomSubscriber<T extends ResponseBody> implements Subscriber<T> {
    private static final String TAG = Subscriber.class.getSimpleName();


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.e(TAG, "onError:" + e.getMessage());

        onFailure(e);
    }

    @Override
    public void onNext(T response) {
        Log.i(TAG, "CustomSubscriber --------------------> onNext.response.code=" + response.code + "|" + response.getClass().getSimpleName());
        if (response.code == 81) {
            // send broadcast
            return;
        }
        onSuccess(response);
    }

    @Override
    public void onComplete() {
        Log.i(TAG, "CustomSubscriber  --------------------> onCompleted");
        onFinish();
    }

    @Override
    public void onSubscribe(Subscription s) {

    }

    public abstract void onFinish();

    public abstract void onSuccess(T response);

    public abstract void onFailure(Response<T> response);

    public abstract void onFailure(Throwable e);
}
