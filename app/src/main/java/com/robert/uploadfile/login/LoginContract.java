package com.robert.uploadfile.login;

import android.util.Log;

import com.robert.uploadfile.UploadApp;
import com.robert.uploadfile.base.BaseActivity;
import com.robert.uploadfile.base.BaseFragment;
import com.robert.uploadfile.base.networking.CustomObserver;
import com.robert.uploadfile.base.networking.NetworkPresenter;
import com.robert.uploadfile.base.response.ResponseBody;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * The String use to Observer base for login response model
 * @author Created by Robert on 2017 Aug 17.
 */
public class LoginContract extends CustomObserver {
    private static final String TAG = LoginContract.class.getSimpleName();

    interface View {
        void showProgress();

        void hideProgress();

        void usernameInvalid();

        void passwordInvalid();

        void onFinish();
    }

    public LoginContract() {
        super();
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onSuccess(ResponseBody response) {

    }

    @Override
    public void onFailure(Response response) {

    }

    @Override
    public void onFailure(Throwable e) {

    }

    interface Presenter {
        void onSuccess(ResponseBody response);
        void onFailure(Response response);
        void onFailure(Throwable e);
    }
}
