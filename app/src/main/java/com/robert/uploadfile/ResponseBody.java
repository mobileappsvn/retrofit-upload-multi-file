package com.robert.uploadfile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class ResponseBody implements Parcelable, Cloneable {

    @SerializedName("code")
    public int code;

    public ResponseBody() {
    }

    protected ResponseBody(Parcel in) {
        this.code = in.readInt();
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
    }

}
