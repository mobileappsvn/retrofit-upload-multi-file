package com.robert.uploadfile;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.robert.uploadfile.base.api.DfeApiListener;
import com.robert.uploadfile.base.response.ResponseBody;
import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
//import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Robert on 2017 Aug 20.
 */
public class UploadApp extends Application implements DfeApiListener {

    private String TAG = UploadApp.class.getSimpleName();
    private NotificationManager mNotificationManager;

    public UploadApp() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        EventBus.getDefault().register(this);

        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/HELVETICANEUE.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );*/
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onTerminate() {
        EventBus.getDefault().unregister(this);
        super.onTerminate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)/*ThreadMode.BACKGROUND*/
    public void onAccountBlock(ResponseBody response) {

        Log.d(TAG, "--->onAccountBlockEvent.uploadResponse=" + (response != null ? response.code : "NULL"));
        Toast.makeText(this, "Your account has been blocked", Toast.LENGTH_SHORT).show();

        //Your can delete app cache or token or account cache here
        //Your can start another activity here
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    private void newTokenRequired(String message) {

        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }

    }

    @Override
    public void onSuccess(int code, ResponseBody response, int requestCode) {
        Log.d(TAG, "--->onSuccess.ResponseBody=" + (response != null ? response.code : "NULL"));
    }

    @Override
    public void onFailure(int code, Throwable e) {
        Log.d(TAG, "--->onFailure.code=" + code);
    }

    @Override
    public void onFinish(int requestCode) {

    }
}
