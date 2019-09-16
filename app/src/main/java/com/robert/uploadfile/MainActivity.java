package com.robert.uploadfile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.robert.uploadfile.base.BaseActivity;
import com.robert.uploadfile.base.networking.CustomObserver;
import com.robert.uploadfile.constant.Constants;
import com.robert.uploadfile.login.LoginRequest;
import com.robert.uploadfile.service.AccountService;
import com.robert.uploadfile.service.UploadService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    /*{
        "api": "post_status",
        "token": "6a45a1e3-3869-44e2-9d1f-fa61519132ce",
        "img_cat": 1,
        "comment": "Có ai tâm hự cùng chế hem?",
        "post_mode": 0,
        "friend_id": [
            "59a8b2eae4b054b87e0840b7",
            "59a7deb4e4b054b87e0840a5",
            "59a7dd56e4b054b87e08409d"
         ],
        "file": "Content of file 1",
        "file": "Content of file 2",
        "file": "Content of file 3",
        "sum": {
            "IMG_20170802_111432.jpg": "803158ab918d31f846d4d7f95f92c9c1",
            "587c4178e4b0060e66732576_294204376.jpg": "79d2456c0511127bde34da15f92ae338",
            "594a2ea4e4b0d6df9153028d_265511791.jpg": "b09c8f7ad7233c4c47c3bd4404ee5300"
        }
    }*/
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;

    private Uri                 uri;
    private UploadService       uploadService;
    private AccountService      accountService;
    private ProgressDialog      progressDialog;
    private CompositeDisposable subscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button selectUploadButton = (Button) findViewById(R.id.select_image);

        selectUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);

            }
        });

        Button loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(findViewById(R.id.password));
                login();

            }
        });

        Button uploadMultiFile = (Button) findViewById(R.id.upload_multi_file);
        uploadMultiFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //uploadMultiFile();
                //uploadMultiFileWithObservable();
                uploadMultiFileWithObservable2();

            }
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Change base URL to your upload server URL.
        uploadService = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_PATH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(UploadService.class);

        accountService = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_PATH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(AccountService.class);

        subscription = new CompositeDisposable();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        // Register as a subscriber
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //TODO Execution upload file to server via URI
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        if (uri == null) return;

        //TODO Execution upload file to server via URI

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static String dataFileMD5EncryptedToString(File encTarget) {
        final String TAG = "MD5Encrypted";


        InputStream is = null;
        byte[] buffer = new byte[1024];
        int read;
        try {
            MessageDigest mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.reset();
            is = new FileInputStream(encTarget);
            while ((read = is.read(buffer)) > 0) {
                mdEnc.update(buffer, 0, read);
            }

            byte[] md5sum = mdEnc.digest();
            StringBuilder builder = new StringBuilder();
            final int HEX = 16;
            int md5Size = md5sum.length;
            for (int i = 0; i < md5Size; i++) {
                builder.append(Integer.toString((md5sum[i] & 0xff) + 0x100, HEX).substring(1));
            }

            String output = builder.toString();
            Log.e(TAG, output);
            return output;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Exception while encrypting to md5");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Exception while getting FileInputStream");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Unable to process file for MD5");
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception on closing MD5 input stream");
                }
            }
        }

        return "";
    }

    private void login() {
        progressDialog.show();//("", "", "")

        accountService.login(new LoginRequest(((EditText) findViewById(R.id.phone)).getText().toString(),
                ((EditText) findViewById(R.id.password)).getText().toString(),
                "adr_352238062784640"))//TODO Using for Java Server
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<com.robert.uploadfile.base.response.ResponseBody>(mApp, this) {
                    //Neu goi onNext or day thi CustomObserver se khong dieu huong duoc ve noi minh mong muon
                    /*@Override
                    public void onNext(com.robert.uploadfile.base.response.ResponseBody response) {
                        progressDialog.dismiss();
                        Log.d(TAG, "subscribe().onNext.uploadResponse=" + (response != null ? response.code : "NULL"));
                    }*/

                    @Override
                    public void onFinish() {
                        Log.d(TAG, "--->subscribe().onFinish");
                        progressDialog.dismiss();
                        //Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(com.robert.uploadfile.base.response.ResponseBody response) {

                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "--->subscribe().onSuccess.uploadResponse=" + (response != null ? response.code : "NULL"));

                    }

                    @Override
                    public void onFailure(Response response) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login error", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login failure", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void uploadMultiFileWithObservable() {
        progressDialog.show();

        ArrayList<String> filePaths = new ArrayList<>();
        filePaths.add("storage/emulated/0/DCIM/Camera/IMG_20170802_111432.jpg");
        filePaths.add("storage/emulated/0/Pictures/WeLoveChat/587c4178e4b0060e66732576_294204376.jpg");
        filePaths.add("storage/emulated/0/Pictures/WeLoveChat/594a2ea4e4b0d6df9153028d_265511791.jpg");

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("api", "upl_multi_file");
        builder.addFormDataPart("token", "6a45a1e3-3869-44e2-9d1f-fa61519132ce");
        builder.addFormDataPart("img_cat", "1");
        builder.addFormDataPart("user_name", "Robert");
        builder.addFormDataPart("email", "mobile.apps.pro.vn@gmail.com");

        LinkedHashMap<String, String> hashSumOfFile = new LinkedHashMap<>();
        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images

        JSONObject jSum = new JSONObject();

        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i));

            //hashSumOfFile.put(file.getName(), dataFileMD5EncryptedToString(file));
            try {
                jSum.put("SUM_OF:" + file.getName(), dataFileMD5EncryptedToString(file));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //builder.addFormDataPart("sum_" + file.getName(), dataFileMD5EncryptedToString(file));
            builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }

        //RequestBody reqSum = RequestBody.create(MediaType.parse("multipart/form-data"), jSum.toString());
        //RequestBody reqSum = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jSum.toString());
        RequestBody reqSum = RequestBody.create(null, jSum.toString());
        //builder.addPart(reqSum);
        builder.addFormDataPart("sum[]", jSum.toString());

        MultipartBody requestBody = builder.build();

        //uploadService.uploadMultiFiles(requestBody)//TODO Using for PHP Server
        uploadService.uploadMultipleFile(requestBody)//TODO Using for Java Server
                //uploadService.hashMap(hashSumOfFile)//TODO Using for Java Server
                //uploadService.queryMap(hashSumOfFile)//TODO Using for Java Server
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<com.robert.uploadfile.base.response.ResponseBody>() {

                    @Override
                    public void onFinish() {
                        Log.d(TAG, "subscribe().onFinish");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(com.robert.uploadfile.base.response.ResponseBody response) {
                        progressDialog.dismiss();
                        Log.d(TAG, "subscribe().onSuccess.uploadResponse=" + (response != null ? response.code : "NULL"));
                    }

                    @Override
                    public void onFailure(Response response) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        progressDialog.dismiss();
                    }
                });

    }

    private void uploadMultiFileWithObservable2() {
        progressDialog.show();

        ArrayList<String> filePaths = new ArrayList<>();
        filePaths.add("storage/emulated/0/DCIM/Camera/IMG_20170802_111432.jpg");
        filePaths.add("storage/emulated/0/Pictures/WeLoveChat/587c4178e4b0060e66732576_294204376.jpg");
        filePaths.add("storage/emulated/0/Pictures/WeLoveChat/594a2ea4e4b0d6df9153028d_265511791.jpg");

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("api", "upl_multi_file");
        builder.addFormDataPart("token", "6a45a1e3-3869-44e2-9d1f-fa61519132ce");
        builder.addFormDataPart("img_cat", "1");
        builder.addFormDataPart("user_name", "Robert");
        builder.addFormDataPart("email", "mobile.apps.pro.vn@gmail.com");

        LinkedHashMap<String, String> hashSumOfFile = new LinkedHashMap<>();
        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images

        JSONObject jBody = new JSONObject();
        MultipartBody.Part[] fileParts = new MultipartBody.Part[filePaths.size()];

        Map<String, RequestBody> map = new HashMap<>();

        //map.put("api", RequestBody.create(MediaType.parse("text/plain"), "upl_multi_file"));
        //map.put("token", RequestBody.create(MediaType.parse("text/plain"),"6a45a1e3-3869-44e2-9d1f-fa61519132ce"));
        //map.put("img_cat", RequestBody.create(MediaType.parse("text/plain"),"1"));
        //map.put("user_name", RequestBody.create(MediaType.parse("text/plain"),"Robert"));
        //map.put("email", RequestBody.create(MediaType.parse("text/plain"),"mobile.apps.pro.vn@gmail.com"));
        try {
            jBody.put("email", "mobile.apps.pro.vn@gmail.com");
            jBody.put("api",  "upl_multi_file");
            jBody.put("token", "6a45a1e3-3869-44e2-9d1f-fa61519132ce");
            jBody.put("img_cat", 1);
            jBody.put("user_name", "Robert");


            //JSONArray jSum = new JSONArray();
            JSONObject jSum = new JSONObject();

            for (int i = 0; i < filePaths.size(); i++) {
                File file = new File(filePaths.get(i));

                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                map.put("file\"; filename=\"" + file.getName(), fileReqBody);

                try {
                    //jSum.put("SUM_OF:" + file.getName() + ":" + dataFileMD5EncryptedToString(file));
                    jSum.put(file.getName(), dataFileMD5EncryptedToString(file));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*
                //builder.addFormDataPart("sum_" + file.getName(), dataFileMD5EncryptedToString(file));
                builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                */

                /*
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
                //Setting the file name as an empty string here causes the same issue, which is sending the request successfully without saving the files in the backend, so don't neglect the file name parameter.
                fileParts[i] = MultipartBody.Part.createFormData(String.format(Locale.ENGLISH, "files[%d]", i), file.getName(), fileBody);
                */

            }
            jBody.put("sum", jSum);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("data", RequestBody.create(null, jBody.toString()));


        //TODO Solution 1
        //RequestBody reqSum = RequestBody.create(MediaType.parse("multipart/form-data"), jSum.toString());
        //RequestBody reqSum = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jSum.toString());
        //RequestBody reqSum = RequestBody.create(MediaType.parse("text/plain"), jSum.toString());

        //RequestBody reqSum = RequestBody.create(null, jBody.toString());
        //builder.addPart(reqSum);
        //TODO Soluton 2
        //builder.addFormDataPart("sum[]", jSum.toString());

        MultipartBody requestBody = builder.build();
        String token = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxQ1dlNTIyK3pwQzZpckRPb1drcW9BPT0iLCJwbWQiOiJzS25McXh5OWJRZzVuUzNNd2J6MmJnPT0iLCJkdmkiOiJhZHJfMzUyMjM4MDYyNzg0NjQwIiwiaXNzIjoicUNXZTUyMit6cEM2aXJET29Xa3FvQT09In0.FBXRaewijuZZ14kmt7Q0y7m7Wsbt5g3VDyCFu4fTERfkQ40UHCyYxxR8LMPrNjItUPsg4P6dNDm0IbH8CC63SA";
        uploadService.uploadFileMultiPart(token, map)//TODO Using for Java Server
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new CustomObserver<com.robert.uploadfile.base.response.ResponseBody>() {

                @Override
                public void onFinish() {
                    Log.d(TAG, "subscribe().onFinish");
                    progressDialog.dismiss();
                }

                @Override
                public void onSuccess(com.robert.uploadfile.base.response.ResponseBody response) {
                    progressDialog.dismiss();
                    Log.d(TAG, "subscribe().onSuccess.uploadResponse=" + (response != null ? response.code : "NULL"));
                }

                @Override
                public void onFailure(Response response) {
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Throwable e) {
                    progressDialog.dismiss();
                }
            });

    }

    private void postStatusWithUploadMultiplePart() {
        Log.i(TAG, "postStatusWithUploadMultiplePart():Execution upload file to server via URI");
        if (isValidParams()) {
            //Show loading dialog
            progressDialog.show();
            //Get authentication token
            String token = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxQ1dlNTIyK3pwQzZpckRPb1drcW9BPT0iLCJwbWQiOiJzS25McXh5OWJRZzVuUzNNd2J6MmJnPT0iLCJkdmkiOiJhZHJfMzUyMjM4MDYyNzg0NjQwIiwiaXNzIjoicUNXZTUyMit6cEM2aXJET29Xa3FvQT09In0.FBXRaewijuZZ14kmt7Q0y7m7Wsbt5g3VDyCFu4fTERfkQ40UHCyYxxR8LMPrNjItUPsg4P6dNDm0IbH8CC63SA";
            //Preparing files list
            ArrayList<String> filePaths = new ArrayList<>();
            filePaths.add("storage/emulated/0/DCIM/Camera/IMG_20170802_111432.jpg");
            filePaths.add("storage/emulated/0/Pictures/WeLoveChat/587c4178e4b0060e66732576_294204376.jpg");
            filePaths.add("storage/emulated/0/Pictures/WeLoveChat/594a2ea4e4b0d6df9153028d_265511791.jpg");

            // Map is used to multipart the file using okhttp3.RequestBody
            JSONObject jBody = new JSONObject();
            Map<String, RequestBody> map = new HashMap<>();

            try {
                //Put other params into JSONObject
                jBody.put("email", "mobile.apps.pro.vn@gmail.com");
                jBody.put("api", "upl_multi_file");
                jBody.put("token", "6a45a1e3-3869-44e2-9d1f-fa61519132ce");
                jBody.put("img_cat", 1);
                jBody.put("user_name", "Robert");
                jBody.put("comment", "Có ai tâm hự cùng chế hem?");
                jBody.put("post_mode", 0);

                JSONArray jFriendId = new JSONArray();
                jFriendId.put("59a8b2eae4b054b87e0840b7");
                jFriendId.put("59a7deb4e4b054b87e0840a5");
                jFriendId.put("59a7dd56e4b054b87e08409d");
                jBody.put("friend_id", jFriendId);
                //

                JSONObject jSum = new JSONObject();
                //Put all file to Map
                for (int i = 0; i < filePaths.size(); i++) {
                    File file = new File(filePaths.get(i));

                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                    map.put("file\"; filename=\"" + file.getName(), fileReqBody);

                    try {
                        //Put all SUM file into JSONObject
                        jSum.put(file.getName(), dataFileMD5EncryptedToString(file));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                jBody.put("sum", jSum);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put("data", RequestBody.create(null, jBody.toString()));

            //getPresenter().uploadFileMultiPart(token, map);

        }
    }

    private void postStatusWithUploadMultiplePart3() {
        Log.i(TAG, "postStatusWithUploadMultiplePart():Execution upload file to server via URI");
        if (isValidParams()) {
            //Show loading dialog
            progressDialog.show();
            //Get authentication token
            String token = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxQ1dlNTIyK3pwQzZpckRPb1drcW9BPT0iLCJwbWQiOiJzS25McXh5OWJRZzVuUzNNd2J6MmJnPT0iLCJkdmkiOiJhZHJfMzUyMjM4MDYyNzg0NjQwIiwiaXNzIjoicUNXZTUyMit6cEM2aXJET29Xa3FvQT09In0.FBXRaewijuZZ14kmt7Q0y7m7Wsbt5g3VDyCFu4fTERfkQ40UHCyYxxR8LMPrNjItUPsg4P6dNDm0IbH8CC63SA";
            //Preparing files list
            ArrayList<String> filePaths = new ArrayList<>();
            filePaths.add("storage/emulated/0/DCIM/Camera/IMG_20170802_111432.jpg");
            filePaths.add("storage/emulated/0/Pictures/WeLoveChat/587c4178e4b0060e66732576_294204376.jpg");
            filePaths.add("storage/emulated/0/Pictures/WeLoveChat/594a2ea4e4b0d6df9153028d_265511791.jpg");

            // Map is used to multipart the file using okhttp3.RequestBody
            //JSONObject jBody = new JSONObject();
            Map<String, RequestBody> map = new HashMap<>();

            map.put("api", RequestBody.create(MediaType.parse("text/plain"), "post_status"));
            map.put("token", RequestBody.create(MediaType.parse("text/plain"), "6a45a1e3-3869-44e2-9d1f-fa61519132ce"));
            map.put("img_cat", RequestBody.create(MediaType.parse("text/plain"), "1"));
            map.put("comment", RequestBody.create(MediaType.parse("text/plain"), "Có ai tâm hự cùng chế hem?"));
            map.put("post_mode", RequestBody.create(MediaType.parse("text/plain"), "0"));
            try {

                JSONArray jFriendId = new JSONArray();
                jFriendId.put("59a8b2eae4b054b87e0840b7");
                jFriendId.put("59a7deb4e4b054b87e0840a5");
                jFriendId.put("59a7dd56e4b054b87e08409d");

                JSONObject jSum = new JSONObject();
                //Put all file to Map
                for (int i = 0; i < filePaths.size(); i++) {
                    File file = new File(filePaths.get(i));

                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                    map.put("file\"; filename=\"" + file.getName(), fileReqBody);

                    try {
                        //Put all SUM file into JSONObject
                        jSum.put(file.getName(), dataFileMD5EncryptedToString(file));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                map.put("sum", RequestBody.create(null, jSum.toString()));
            } catch (/*JSON*/Exception e) {
                e.printStackTrace();
            }

            //getPresenter().uploadFileMultiPart(token, map);

        }
    }

    private void postStatusWithUploadMultiplePart5() {
        Log.i(TAG, "postStatusWithUploadMultiplePart():Execution upload file to server via URI");
        if (isValidParams()) {
            //Show loading dialog
            progressDialog.show();
            //Get authentication token
            String token = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxQ1dlNTIyK3pwQzZpckRPb1drcW9BPT0iLCJwbWQiOiJzS25McXh5OWJRZzVuUzNNd2J6MmJnPT0iLCJkdmkiOiJhZHJfMzUyMjM4MDYyNzg0NjQwIiwiaXNzIjoicUNXZTUyMit6cEM2aXJET29Xa3FvQT09In0.FBXRaewijuZZ14kmt7Q0y7m7Wsbt5g3VDyCFu4fTERfkQ40UHCyYxxR8LMPrNjItUPsg4P6dNDm0IbH8CC63SA";
            //Preparing files list
            ArrayList<String> filePaths = new ArrayList<>();
            filePaths.add("storage/emulated/0/DCIM/Camera/IMG_20170802_111432.jpg");
            filePaths.add("storage/emulated/0/Pictures/WeLoveChat/587c4178e4b0060e66732576_294204376.jpg");
            filePaths.add("storage/emulated/0/Pictures/WeLoveChat/594a2ea4e4b0d6df9153028d_265511791.jpg");

            // Map is used to multipart the file using okhttp3.RequestBody
            //JSONObject jBody = new JSONObject();
            Map<String, RequestBody> files = new HashMap<>();
            Map<String, RequestBody> mapSum = new HashMap<>();

            /*map.put("api", RequestBody.create(MediaType.parse("text/plain"), "post_status"));
            map.put("token", RequestBody.create(MediaType.parse("text/plain"), "6a45a1e3-3869-44e2-9d1f-fa61519132ce"));
            map.put("img_cat", RequestBody.create(MediaType.parse("text/plain"), "1"));
            map.put("comment", RequestBody.create(MediaType.parse("text/plain"), "Có ai tâm hự cùng chế hem?"));
            map.put("post_mode", RequestBody.create(MediaType.parse("text/plain"), "0"));*/
            try {
                //Put other params into JSONObject
                /*jBody.put("email", "mobile.apps.pro.vn@gmail.com");
                jBody.put("api", "upl_multi_file");
                jBody.put("token", "6a45a1e3-3869-44e2-9d1f-fa61519132ce");
                jBody.put("img_cat", 1);
                jBody.put("user_name", "Robert");
                jBody.put("comment", "Có ai tâm hự cùng chế hem?");
                jBody.put("post_mode", 0);*/

                JSONArray jFriendId = new JSONArray();
                jFriendId.put("59a8b2eae4b054b87e0840b7");
                jFriendId.put("59a7deb4e4b054b87e0840a5");
                jFriendId.put("59a7dd56e4b054b87e08409d");
                //jBody.put("friend_id", jFriendId);

                JSONObject jSum = new JSONObject();
                //Put all file to Map
                for (int i = 0; i < filePaths.size(); i++) {
                    File file = new File(filePaths.get(i));

                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                    files.put("file\"; filename=\"" + file.getName(), fileReqBody);

                    try {
                        //Put all SUM file into JSONObject
                        jSum.put(file.getName(), dataFileMD5EncryptedToString(file));

                        mapSum.put(file.getName(), RequestBody.create(MediaType.parse("text/plain"), FileUtils.dataFileMD5EncryptedToString(file)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //jBody.put("sum", jSum);
                files.put("sum", RequestBody.create(null, jSum.toString()));
                files.put("friend_id", RequestBody.create(null, jFriendId.toString()));
            } catch (/*JSON*/Exception e) {
                e.printStackTrace();
            }

            //map.put("data", RequestBody.create(null, jBody.toString()));

            //getPresenter().uploadFileMultiPart(token, map);


            //getPresenter().uploadFileMultiPart(token, "post_status", "6a45a1e3-3869-44e2-9d1f-fa61519132ce", 1, "Có ai tâm hự cùng chế hem?", 0, mapSum, files);

        }
    }

    private void uploadMultiFile() {
        progressDialog.show();

        ArrayList<String> filePaths = new ArrayList<>();
        filePaths.add("storage/emulated/0/DCIM/Camera/IMG_20170802_111432.jpg");
        filePaths.add("storage/emulated/0/Pictures/WeLoveChat/587c4178e4b0060e66732576_294204376.jpg");
        filePaths.add("storage/emulated/0/Pictures/WeLoveChat/594a2ea4e4b0d6df9153028d_265511791.jpg");

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("user_name", "Robert");
        builder.addFormDataPart("email", "mobile.apps.pro.vn@gmail.com");

        // Map is used to multipart the file using okhttp3.RequestBody Multiple Images
        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i));
            builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }

        MultipartBody requestBody = builder.build();
        Call<ResponseBody> call = uploadService.uploadMultiFile(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Toast.makeText(MainActivity.this, "Success " + response.message(), Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "Success " + response.body().toString(), Toast.LENGTH_LONG).show();

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "Error " + t.getMessage());
            }
        });


    }

    private boolean isValidParams() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountResponse(com.robert.uploadfile.base.response.ResponseBody response) {

        Log.d(TAG, "--->onMessageEvent.uploadResponse=" + (response != null ? response.code : "NULL"));
        Toast.makeText(this, "Invalid phone number or password", Toast.LENGTH_SHORT).show();

    }

    /**
     * Hidden the soft keyboard of view defined when focus
     *
     * @param view
     */
    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Shows the soft keyboard of view defined
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister
        EventBus.getDefault().unregister(this);
    }
}

