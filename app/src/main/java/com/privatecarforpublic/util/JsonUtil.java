package com.privatecarforpublic.util;

import android.util.Log;

import com.google.gson.Gson;
import com.privatecarforpublic.response.ResponseResult;

import org.json.JSONException;
import org.json.JSONObject;

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
        String cookie="token=\""+token+"\"";
        switch (requestMethod) {
            case HttpGet:
                request = new Request.Builder().url(url).addHeader("Cookie",cookie).get().build();
                break;
            case HttpPost:
                body = RequestBody.create(JSON, json);
                Log.e(TAG, json);
                if(token==null)
                    request = new Request.Builder().url(url).post(body).build();
                else
                    request = new Request.Builder().url(url).addHeader("Cookie",cookie).post(body).build();
                break;
            case HttpPut:
                body = RequestBody.create(JSON, json);
                request = new Request.Builder().url(url).addHeader("Cookie",cookie).put(body).build();
                break;
            case HttpDelete:
                request = new Request.Builder().url(url).addHeader("Cookie",cookie).delete().build();
                break;
        }
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            String result = response.body().string();
            Log.e(TAG, result);
            return convert(result);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    public static ResponseResult convert(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(jsonObject.getInt("code"));
        responseResult.setMessage(jsonObject.getString("message"));
        responseResult.setData(jsonObject.getString("data"));
        return responseResult;
    }
}
