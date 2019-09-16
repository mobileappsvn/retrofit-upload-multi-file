package com.robert.uploadfile.base.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * The ResponseBody use to response base for all response model
 * @author Created by Robert on 2017 Aug 20.
 */
public class ResponseBody implements Parcelable, Cloneable {

    @SerializedName("code")
    public int code;

    @SerializedName("status")
    public String status;

    public ResponseBody() {
    }

    protected ResponseBody(Parcel in) {
        this.code = in.readInt();
        this.status = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Static field used to regenerate object, individually or as arrays
     */
    public static final Creator<ResponseBody> CREATOR = new Creator<ResponseBody>() {
        public ResponseBody createFromParcel(Parcel pc) {
            return new ResponseBody(pc);
        }

        public ResponseBody[] newArray(int size) {
            return new ResponseBody[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.status);
    }

}
