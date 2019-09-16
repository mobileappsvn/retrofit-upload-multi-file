package com.robert.uploadfile.base;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.robert.uploadfile.base.networking.NetworkPresenter;
import com.robert.uploadfile.base.response.ResponseBody;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * The BaseFragment use to Fragment base for all Fragment view class in App
 * Created by robert on 2017 Aug 20.
 */
public class BaseFragment extends Fragment implements NetworkPresenter {

    private final String TAG = BaseFragment.class.getSimpleName();

    public BaseActivity     mBaseActivity;
    public Context          mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.mBaseActivity = (BaseActivity) this.getActivity();
            this.mContext = mBaseActivity.getApplicationContext();
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {

        this.mBaseActivity = (BaseActivity) context;
        this.mContext = context;

        super.onAttach(context);
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Listen all action private in application send to all fragment
     * @param className the String class name of class extend component
     * @param data the Intent data
     */
    public void onUpdateByClass(String className, Intent data) {

    }

    /**
     * Listen all action private in application send to all fragment
     * @param fragment the Fragment view class
     * @param data the Intent data
     */
    public void onUpdate(BaseFragment fragment, Intent data) {

    }

    /**
     * Listen all action private in application send to all fragment
     * @param actionKey the String action keyword if want handle
     * @param data the Intent data
     */
    public void onUpdate(String actionKey, Intent data) {

    }

    /**
     * Listen all action private in application send to all fragment
     * @param mTab the String name of The TabBar
     * @param actionKey the String action keyword if want handle
     * @param data the Intent data
     */
    public void onUpdate(String mTab, String actionKey, Intent data) {

    }

    @Override
    public void onSuccess(int code, ResponseBody response, int requestCode) {

    }

    @Override
    public void onFailure(int code, Throwable e) {

    }

    @Override
    public void onFinish(int requestCode) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)/*ThreadMode.BACKGROUND*/
    public void onAccountBlock(ResponseBody response) {

        Log.d(TAG, "--->onAccountBlockEvent.uploadResponse=" + (response != null ? response.code : "NULL"));
        if (mBaseActivity != null) {
            Toast.makeText(mContext, "Your account has been blocked", Toast.LENGTH_SHORT).show();
        }

        //Your can delete app cache or token or account cache here
        //Your can start another activity here
    }
}