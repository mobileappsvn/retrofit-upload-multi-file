package com.robert.uploadfile;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by robert on 8/17/17.
 */

public abstract class CustomSubscriber<T extends ResponseBody> extends Subscriber<T>  implements Callback<T> {
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
    public void onCompleted() {
        Log.i(TAG, "CustomSubscriber  --------------------> onCompleted");
        onFinish();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.i(TAG, "CustomSubscriber  --------------------> onResponse");
        if (response.isSuccessful()) {

            Log.d(TAG, response.code() + ":" + response.raw().request().url());

            onSuccess(response.body());

        } else {

            try {
                Log.e(TAG, response.code() + ":" + response.raw().request().url() + " - " + response.errorBody().string());
            } catch (IOException e) {
                Log.e(TAG, response.code() + ":" + response.raw().request().url());
                e.printStackTrace();
            }
            onError(response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure(t);
    }

    public abstract void onFinish();

    public abstract void onSuccess(T response);

    public abstract void onError(Response<T> response);

    public abstract void onFailure(Throwable e);
}
