package com.privatecarforpublic;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.privatecarforpublic.model.MyTravels;
import com.privatecarforpublic.model.RouteModel;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.privatecarforpublic", appContext.getPackageName());
    }

    @Test
    public void testRequest() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String userId;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<RouteModel> myTravelsList = new ArrayList<>();
                    Gson gson = new Gson();
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, SharePreferenceUtil
                            .getString(appContext, "token", ""), Constants.SERVICE_ROOT + "/Route/fd", null);
                    if (responseResult.getCode() != 200) {
                        //CommonUtil.showMessage(, "无相应的出行路程！");
                        myTravelsList.clear();
                    } else {
                        myTravelsList = gson.fromJson(responseResult.getData(), new TypeToken<List<RouteModel>>() {}.getType());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (
                InterruptedException e) {
            e.printStackTrace();
        }
    }
}
