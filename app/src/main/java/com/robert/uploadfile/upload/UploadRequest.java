package com.robert.uploadfile.upload;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robert on 2017 Aug 18.
 */

public class UploadRequest implements Parcelable {

    @SerializedName("file_name")
    public String fileName;
    @SerializedName("sum")
    public String hashSum;


    public UploadRequest(Parcel in) {
        super();
        this.fileName = in.readString();
        this.hashSum = in.readString();

    }

    public UploadRequest(String fileName, String hashSum) {
        super();
        this.fileName = fileName;
        this.hashSum = hashSum;

    }

    public static final Parcelable.Creator<UploadRequest> CREATOR = new Parcelable.Creator<UploadRequest>() {
        @Override
        public UploadRequest createFromParcel(Parcel source) {
            return new UploadRequest(source);
        }

        @Override
        public UploadRequest[] newArray(int size) {
            return new UploadRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(fileName);
        dest.writeString(hashSum);

    }
}
