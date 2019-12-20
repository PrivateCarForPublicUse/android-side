package com.privatecarforpublic.util;

import android.util.Log;

import com.google.gson.Gson;
import com.privatecarforpublic.response.ResponseResult;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JsonUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static Gson gson = new Gson();
    /**
     * TAG
     */
    private static final String TAG = "PrivateCarForPublic_NET";

    public static ResponseResult sendRequest(HttpRequestMethod requestMethod, String token, String url, Object entity) {
        Request request = null;
        RequestBody body;
        String json = gson.toJson(entity);
        switch (requestMethod) {
            case HttpGet:
                request = new Request.Builder().url(url).addHeader("token",token).get().build();
                break;
            case HttpPost:
                body = RequestBody.create(JSON, json);
                if(token==null)
                    request = new Request.Builder().url(url).post(body).build();
                else
                    request = new Request.Builder().url(url).addHeader("token",token).post(body).build();
                break;
            case HttpPut:
                body = RequestBody.create(JSON, json);
                request = new Request.Builder().url(url).addHeader("token",token).put(body).build();
                break;
            case HttpDelete:
                request = new Request.Builder().url(url).addHeader("token",token).delete().build();
                break;
        }
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            String result = response.body().string();
            return gson.fromJson(result,ResponseResult.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}
