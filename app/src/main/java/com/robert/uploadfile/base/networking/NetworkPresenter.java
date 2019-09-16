package com.robert.uploadfile.base.networking;


import com.robert.uploadfile.base.response.ResponseBody;

/**
 * Created by robert on 8/16/17.
 */

public interface NetworkPresenter<T extends ResponseBody> {

    void onSuccess(int code, T response, int requestCode);

    void onFailure(int code, Throwable e);

    void onFinish(int requestCode);


}
