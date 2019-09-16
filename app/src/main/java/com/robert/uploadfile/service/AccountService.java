package com.robert.uploadfile.service;

import com.robert.uploadfile.login.LoginRequest;
import com.robert.uploadfile.login.LoginResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;


/**
 * Define all method use for account of user via Observable of RxJava2
 * Created by Robert
 */
public interface AccountService {

    @POST("/ws.apps.mongodb/api/account/login")
    Observable<LoginResponse> login(@Body LoginRequest request);

    @FormUrlEncoded
    @PUT("/api/account/register")
    Call<ResponseBody> subscriber(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("fbID") String fbID,
            @Field("gmailID") String gmailID,
            @Field("twitID") String twitID,
            @Field("gender") String gender,
            @Field("birthDate") String birthDate,
            @Field("location") String location,
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Field("profileImage") String profileImage);
    //@Field parameters can only be used with form encoding. (parameter #2)
}
