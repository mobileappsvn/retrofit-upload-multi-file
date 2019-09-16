package com.robert.uploadfile.base.networking;

import android.util.Log;

import com.robert.uploadfile.base.response.ResponseBody;
import com.robert.uploadfile.UploadApp;
import com.robert.uploadfile.base.BaseActivity;
import com.robert.uploadfile.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * The CustomObserver use to Observer base for all response model
 * @author Created by Robert on 2017 Aug 17.
 */
public abstract class CustomObserver<T extends ResponseBody> implements Observer<T> {
    private static final String TAG = CustomObserver.class.getSimpleName();
    private NetworkPresenter    mNetworkPresenter;
    private UploadApp           mApp;
    private BaseActivity        mBaseActivity;
    private BaseFragment        mBaseFragment;

    public CustomObserver() {
    }

    public CustomObserver(UploadApp mApp) {
        this.mApp = mApp;
    }

    public CustomObserver(BaseActivity mBaseActivity) {
        this.mBaseActivity = mBaseActivity;
    }

    public CustomObserver(BaseFragment mBaseFragment) {
        this.mBaseFragment = mBaseFragment;
    }

    public CustomObserver(NetworkPresenter mNetworkPresenter) {
        this.mNetworkPresenter = mNetworkPresenter;
    }

    public CustomObserver(UploadApp mApp, BaseActivity mBaseActivity) {
        this(mApp);
        this.mBaseActivity = mBaseActivity;
    }

    public CustomObserver(UploadApp mApp, BaseFragment mBaseFragment) {
        this(mApp);
        this.mBaseFragment = mBaseFragment;
    }

    public CustomObserver(BaseActivity mBaseActivity, BaseFragment mBaseFragment) {
        this.mBaseActivity = mBaseActivity;
        this.mBaseFragment = mBaseFragment;
    }

    public CustomObserver(UploadApp mApp, NetworkPresenter mNetworkPresenter) {
        this(mApp);
        this.mNetworkPresenter = mNetworkPresenter;
    }

    public CustomObserver(BaseActivity mBaseActivity, NetworkPresenter mNetworkPresenter) {
        this.mBaseActivity = mBaseActivity;
        this.mNetworkPresenter = mNetworkPresenter;
    }

    public CustomObserver(BaseFragment mBaseFragment, NetworkPresenter mNetworkPresenter) {
        this.mBaseFragment = mBaseFragment;
        this.mNetworkPresenter = mNetworkPresenter;
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.e(TAG, "-------------------->onError:" + e.getMessage());

        onFailure(e);
    }

    @Override
    public void onNext(T response) {
        Log.i(TAG, "--------------------> onNext.response.code=" + (response != null ? response.code + "|" + response.getClass().getSimpleName() : "NULL") );
        if (response.code == 81) {

            Log.e(TAG, "--------------------> onNext.response.code=81 and call to onErrorIncorrectPassword" );

            //Choose one of the 5ways:

            //1. TODO: Way 1
            if (mApp != null) mApp.onAccountBlock(response);
            //2. TODO: Way 2
            if (mBaseActivity != null) mBaseActivity.onAccountBlock(response);
            //3. TODO: Way 3
            if (mBaseFragment != null) mBaseFragment.onAccountBlock(response);
            //4. TODO: Way 4
            if (mNetworkPresenter != null) mNetworkPresenter.onFailure(81, null);
            //5. TODO: Way 5
            // Send broadcast
            if (mApp == null && mBaseActivity == null && mBaseFragment == null && mNetworkPresenter == null) {
                onIncorrectPassword(response);
            }

            return;
        }
        onSuccess(response);
    }

    @Override
    public void onComplete() {
        Log.i(TAG, "--------------------> onCompleted");
        onFinish();
    }

    @Override
    public void onSubscribe(Disposable s) {

    }

    private void onIncorrectPassword(T response) {
        Log.i(TAG, "--------------------> onIncorrectPassword");

        //TODO 1: Use LocalBroadcastManager to send Broadcast to target want to listening
        /*
        Intent intent = new Intent(MainActivity.ACTION_INVALID_PASSWORD);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        */

        //TODO 2: Use EventBus to send Event to target want to listening
        EventBus.getDefault().post(response);
    }

    public abstract void onFinish();

    public abstract void onSuccess(T response);

    public abstract void onFailure(Response<T> response);

    public abstract void onFailure(Throwable e);
}
