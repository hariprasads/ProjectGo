package com.example.android.bluetoothlegatt;

/**
 * Created by hari on 5/11/16.
 */
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.HttpEntity;

public class RestClient {
    private static final String BASE_URL = "http://52.53.196.31:5000/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void put(android.content.Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        //client.addHeader("Content-Type", "application/json");
        client.put(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void post(android.content.Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void delete(android.content.Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        client.delete(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}