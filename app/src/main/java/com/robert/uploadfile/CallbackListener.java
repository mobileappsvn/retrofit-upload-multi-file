package com.robert.uploadfile;


/**
 * Created by robert on 8/16/17.
 */

public interface CallbackListener<T extends ResponseBody> {

    void onSuccess(int code, T response, int requestCode);

    void onFailure(int code, Throwable e);

    void onFinish(int requestCode);


}
