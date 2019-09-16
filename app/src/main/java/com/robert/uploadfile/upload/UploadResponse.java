package com.robert.uploadfile.upload;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.robert.uploadfile.base.response.ResponseBody;


/**
 * Created by Robert on 2017 Aug 17.
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

    @SerializedName("desc")
    public String desc;

    public UploadResponse() {
    }

    protected UploadResponse(Parcel in) {
        super(in);
        this.code = in.readInt();
        this.desc = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(code);
        dest.writeString(desc);
    }
}