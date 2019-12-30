package com.privatecarforpublic;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.privatecarforpublic.model.MyTravels;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * @date:2019/12/26
 * @author:zhongcz
 */
@RunWith(AndroidJUnit4.class)
public class HttpRequestTest {
    Context appContext = InstrumentationRegistry.getTargetContext();
    private List<MyTravels> myTravelsList = new ArrayList<>();
    private String userId;

    @Test
    public void testRequest() {
        Thread thread = new Thread(() -> {
            try {
                Gson gson = new Gson();
                userId = "2";
                ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, SharePreferenceUtil
                        .getString(appContext, "token", ""), Constants.SERVICE_ROOT + "car/Route/userId?userId=" + userId, null);
                if (responseResult.getCode() != 200) {
                    //CommonUtil.showMessage(, "无相应的出行路程！");
                    myTravelsList.clear();
                } else {
                    myTravelsList = gson.fromJson(responseResult.getData(), new TypeToken<List<MyTravels>>() {
                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
