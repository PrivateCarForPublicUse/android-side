package com.privatecarforpublic.mapService;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.privatecarforpublic.response.AddTerminalResponse;
import com.privatecarforpublic.response.AddTraceResponse;
import com.privatecarforpublic.response.Point;
import com.privatecarforpublic.response.SearchTraceResponse;
import com.privatecarforpublic.response.UpPointResponse;
import com.privatecarforpublic.util.Constants;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MapService {
    //注册终端实体
    static public AddTerminalResponse addTerminal(String teminalName){
        OkHttpClient client = new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("key", Constants.SERVICE_KEY)
                .add("sid",Constants.SERVICE_ID)
                .add("name",teminalName)
                .build();
        Request request=new Request.Builder()
                .url(Constants.ADD_TERMINAL)
                .post(body)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return JSONObject.parseObject(response.toString(),AddTerminalResponse.class);
        }catch (IOException e){
            Log.e("add terminal error",e.getMessage());
            return null;
        }
    }

    //创建轨迹
    static public AddTraceResponse addTrace(String tid){
        OkHttpClient client = new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("key",Constants.SERVICE_KEY)
                .add("sid",Constants.SERVICE_ID)
                .add("tid",tid)
                .build();
        Request request=new Request.Builder()
                .url(Constants.ADD_TRACE)
                .post(body)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return JSONObject.parseObject(response.toString(),AddTraceResponse.class);
        }catch (IOException e){
            Log.e("add trace error",e.getMessage());
            return null;
        }
    }

    //上传轨迹点
    static public UpPointResponse upPoint(String tid, String trid, List<Point> points){
        OkHttpClient client = new OkHttpClient();
        String pointsString= JSON.toJSONString(points);
        RequestBody body=new FormBody.Builder()
                .add("key",Constants.SERVICE_KEY)
                .add("sid",Constants.SERVICE_ID)
                .add("tid",tid)
                .add("trid",trid)
                .add("points",pointsString)
                .build();
        Request request=new Request.Builder()
                .url(Constants.UP_POINT)
                .post(body)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return JSONObject.parseObject(response.toString(),UpPointResponse.class);
        }catch (IOException e){
            Log.e("up point error",e.getMessage());
            return null;
        }
    }

    //查询轨迹信息
    static public SearchTraceResponse searchTrace(String tid, String trid){
        OkHttpClient client = new OkHttpClient();
        Request.Builder reBuilder=new Request.Builder();
        HttpUrl.Builder urlBuilder=HttpUrl.parse(Constants.SEARCH_TRACE).newBuilder();
        urlBuilder.addQueryParameter("key",Constants.SERVICE_KEY);
        urlBuilder.addQueryParameter("sid",Constants.SERVICE_ID);
        urlBuilder.addQueryParameter("tid",tid);
        urlBuilder.addQueryParameter("trid",trid);
        Request request=reBuilder.get().build();
        try{
            Response response = client.newCall(request).execute();
            return JSONObject.parseObject(response.toString(),SearchTraceResponse.class);

        }catch (IOException e){
            Log.e("add terminal error",e.getMessage());
            return null;
        }
    }
}
