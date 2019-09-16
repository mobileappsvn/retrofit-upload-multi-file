package com.robert.uploadfile.base.networking;

import android.provider.Settings;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.robert.uploadfile.base.response.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava2.HttpException;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * The handle network error when communication with server
 *
 * @author Created by Robert on 2017 Aug 21
 */
public class NetworkError extends Throwable {
    public static final String DEFAULT_ERROR_MESSAGE = "Something went wrong! Please try again.";
    public static final String NETWORK_ERROR_MESSAGE = "No Internet Connection!";

    private static final String ERROR_MESSAGE_HEADER = "Error-Message";
    private final Throwable error;

    public NetworkError(Throwable e) {
        super(e);
        this.error = e;
        Settings.ACTION_BATTERY_SAVER_SETTINGS;
    }

    public String getMessage() {
        return error.getMessage();
    }

    public boolean isAuthFailure() {
        return error instanceof HttpException &&
                ((HttpException) error).code() == HTTP_UNAUTHORIZED;
    }

    public boolean isResponseNull() {
        return error instanceof HttpException && ((HttpException) error).response() == null;
    }

    public String getAppErrorMessage() {
        if (this.error instanceof IOException) return NETWORK_ERROR_MESSAGE;
        if (!(this.error instanceof HttpException)) return DEFAULT_ERROR_MESSAGE;
        retrofit2.Response<?> response = ((HttpException) this.error).response();
        if (response != null) {
            String status = getJsonStringFromResponse(response);
            if (!TextUtils.isEmpty(status)) return status;

            Map<String, List<String>> headers = response.headers().toMultimap();
            if (headers.containsKey(ERROR_MESSAGE_HEADER))
                return headers.get(ERROR_MESSAGE_HEADER).get(0);
        }

        return DEFAULT_ERROR_MESSAGE;
    }

    protected String getJsonStringFromResponse(final retrofit2.Response<?> response) {
        try {
            String jsonString = response.errorBody().string();
            ResponseBody errorResponse = new Gson().fromJson(jsonString, ResponseBody.class);
            return errorResponse.status;
        } catch (Exception e) {
            return null;
        }
    }

    public Throwable getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworkError that = (NetworkError) o;

        return error != null ? error.equals(that.error) : that.error == null;

    }

    @Override
    public int hashCode() {
        return error != null ? error.hashCode() : 0;
    }
}
