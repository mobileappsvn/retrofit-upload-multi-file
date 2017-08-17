package com.robert.uploadfile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 * Created by namIT on 11/29/2016.
 */

public class UploadResponse extends ResponseBody {

    public static final Parcelable.Creator<UploadResponse> CREATOR = new Parcelable.Creator<UploadResponse>() {
        @Override
        public UploadResponse createFromParcel(Parcel source) {
            return new UploadResponse(source);
        }

        @Override
        public UploadResponse[] newArray(int size) {
            return new UploadResponse[size];
        }
    };
    @SerializedName("success")
    public String success;

    public UploadResponse() {
    }

    protected UploadResponse(Parcel in) {
        super(in);
        this.success = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(success);
    }
}