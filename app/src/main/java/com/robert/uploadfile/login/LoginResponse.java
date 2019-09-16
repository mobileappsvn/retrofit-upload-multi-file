package com.robert.uploadfile.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.robert.uploadfile.base.response.ResponseBody;


/**
 * Created by Robert on 2017 Aug 18
 */
public class LoginResponse extends ResponseBody {

    @SerializedName("phone")
    public String phone;
    @SerializedName("device_id")
    public String deviceId;
    @SerializedName("token")
    public String token;

    public LoginResponse(Parcel in) {
        super();
        this.phone = in.readString();
        this.deviceId = in.readString();
        this.token = in.readString();

    }

    public LoginResponse(String phone, String deviceId, String token) {
        super();
        this.phone = phone;
        this.deviceId = deviceId;
        this.token = token;
    }

    public static final Parcelable.Creator<LoginResponse> CREATOR = new Parcelable.Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel source) {
            return new LoginResponse(source);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(phone);
        dest.writeString(deviceId);
        dest.writeString(token);

    }
}