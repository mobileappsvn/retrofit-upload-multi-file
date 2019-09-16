package com.robert.uploadfile.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.robert.uploadfile.base.networking.NetworkPresenter;
import com.robert.uploadfile.constant.Constants;
import com.robert.uploadfile.base.response.ResponseBody;
import com.robert.uploadfile.UploadApp;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * The BaseActivity use to AppCompatActivity base for all Activity view class in App
 * @author Created by Robert on 2017 Aug 20.
 */
public class BaseActivity extends AppCompatActivity implements NetworkPresenter {

    public static final String TAG = BaseActivity.class.getSimpleName();
    public UploadApp            mApp;

    private BroadcastReceiver   mAccountReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerAccountBroadcastReceiver();

        this.mApp = (UploadApp) getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterAccountBroadcastReceiver();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //TODO: Using Calligraphy set font for app
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(int code, ResponseBody response, int requestCode) {
        Log.i(TAG, "--->onSuccess()implementation of NetworkPresenter.ResponseBody=" + (response != null ? response.code : "NULL"));
    }

    @Override
    public void onFailure(int code, Throwable e) {
        Log.i(TAG, "--->onFailure()implementation of NetworkPresenter");
        Toast.makeText(this, "Notification from " + TAG + " --> Invalid phone number or password.code=" + code, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish(int requestCode) {
        Log.i(TAG, "--->onFinish()implementation of NetworkPresenter");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)/*ThreadMode.BACKGROUND*/
    public void onAccountBlock(ResponseBody response) {

        Log.d(TAG, "--->onAccountBlockEvent.uploadResponse=" + (response != null ? response.code : "NULL"));
        Toast.makeText(this, "Your account has been blocked", Toast.LENGTH_SHORT).show();

        //Your can delete app cache or token or account cache here
        //Your can start another activity here
    }

    //===============================Registration Broadcast Receiver==============================//

    private void registerAccountBroadcastReceiver() {
        mAccountReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                switch (action) {
                    case Constants.ACTION_INVALID_PASSWORD:
                        break;
                    default:
                        break;
                }

            }
        };
        IntentFilter filter = new IntentFilter(Constants.ACTION_INVALID_PASSWORD);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mAccountReceiver, filter);
    }

    private void unregisterAccountBroadcastReceiver() {
        if (mAccountReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mAccountReceiver);
        }
    }
}
