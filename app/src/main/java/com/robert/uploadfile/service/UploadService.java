package com.robert.uploadfile.service;

import com.robert.uploadfile.upload.UploadRespond;
import com.robert.uploadfile.upload.UploadResponse;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;


/**
 * Define all method use in application via Observable of RxJava2
 * Created by Robert
 */
public interface UploadService {

    /*@Multipart
    @POST("/upload_multi_files/MultiUpload.php")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);*/
    @Multipart
    @POST("/upload_multi_files/fileUpload.php")
    Call<UploadRespond> uploadSingleFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);

    @Multipart
    //@POST("/upload_multi_files/MultiUpload.php")
    @POST("/upload_multi_files/MultiPartUpload.php")
    Call<UploadRespond> uploadMultiFile(@Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @Part MultipartBody.Part file3);

    //@Multipart
    //@FormUrlEncoded
    //@POST("/upload_multi_files/MultiUpload.php")
    @POST("/upload_multi_files/MultiPartUpload.php")
    Call<ResponseBody> uploadMultiFile(@Body RequestBody requestBody);

    @POST("/upload_multi_files/MultiPartUpload.php")
    Observable<UploadResponse> uploadMultiFiles(@Body RequestBody requestBody);

    //@Multipart
    //@FormUrlEncoded
    //@POST("/WSMongoDBJerseySwagger/api/upload/multiple-file")
    //Observable<UploadResponse> uploadMultipleFile(@Body RequestBody requestBody, @FieldMap LinkedHashMap<String, String> requestBody2);

    //@Multipart
    @POST("/WSMongoDBJerseySwagger/api/upload/multiple-file")
    Observable<UploadResponse> uploadMultipleFile(@Body RequestBody requestBody);
    //Observable<UploadResponse> uploadMultipleFile(@Part RequestBody requestBody, @Part LinkedHashMap<String, String> requestBody2);

    @Multipart
    @POST("/WSMongoDBJerseySwagger/api/upload/multiple-file")
    //Observable<UploadResponse> uploadFileMultiPart(@Header("Authorization") String authHeader, @Part("photo[]") MultipartBody.Part[] files, @Part("sum") RequestBody reqSum);
    Observable<UploadResponse> uploadFileMultiPart(@Header("Authorization") String authHeader, @PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST("/WSMongoDBJerseySwagger/api/upload/hashmap")
    Observable<UploadResponse> hashMap(@FieldMap LinkedHashMap<String, String> requestBody2);

    @POST("/WSMongoDBJerseySwagger/api/upload/hashmap")
    Observable<UploadResponse> queryMap(@QueryMap LinkedHashMap<String, String> requestBody2);

}
