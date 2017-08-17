package com.robert.uploadfile;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by robert on 8/17/17.
 */

public abstract class CustomCallback<T extends ResponseBody> implements Callback<T> {
    private static final String TAG = Subscriber.class.getSimpleName();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.i(TAG, "CustomCallback  --------------------> onResponse");
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
