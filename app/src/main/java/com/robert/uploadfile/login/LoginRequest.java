package com.robert.uploadfile.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Robert on 2017 Aug 18
 */
public class LoginRequest implements Parcelable {

    @SerializedName("phone")
    public String phone;
    @SerializedName("password")
    public String password;
    @SerializedName("device_id")
    public String deviceId;


    public LoginRequest(Parcel in) {
        super();
        this.phone = in.readString();
        this.password = in.readString();
        this.deviceId = in.readString();
    }

    public LoginRequest(String phone, String password, String deviceId) {
        super();
        this.phone = phone;
        this.password = password;
        this.deviceId = deviceId;
    }

    public static final Parcelable.Creator<LoginRequest> CREATOR = new Parcelable.Creator<LoginRequest>() {
        @Override
        public LoginRequest createFromParcel(Parcel source) {
            return new LoginRequest(source);
        }

        @Override
        public LoginRequest[] newArray(int size) {
            return new LoginRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(phone);
        dest.writeString(password);
        dest.writeString(deviceId);
    }
}

