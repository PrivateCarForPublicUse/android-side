package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.EmployeeAdapter;
import com.privatecarforpublic.adapter.MonitorAdapter;
import com.privatecarforpublic.model.Car;
import com.privatecarforpublic.model.CarModel;
import com.privatecarforpublic.model.Monitor;
import com.privatecarforpublic.model.User;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MonitorListActivity extends Activity {
    private static final String TAG = "MonitorListActivity";

    @BindView(R.id.monitor_list)
    ListView monitor_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    private List<Monitor> monitorList;
    private MonitorAdapter monitorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor_list);
        ButterKnife.bind(this);
        title.setText("实时监控");
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(MonitorListActivity.this, 25);
    }

    private void init() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson=new Gson();
                    String token=SharePreferenceUtil.getString(MonitorListActivity.this, "token", "");
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, token, Constants.SERVICE_ROOT + "Master/monitor", null);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(MonitorListActivity.this, responseResult.getMessage());
                    } else{
                        monitorList=new ArrayList<>();
                        List<CarModel> carModels=gson.fromJson(responseResult.getData(), new TypeToken<List<CarModel>>() {
                        }.getType());
                        for(CarModel carModel:carModels){
                            Monitor monitor=new Monitor(carModel.getCar(),carModel.getUser(),1);
                            monitorList.add(monitor);
                        }
                    }
                } catch (Exception e) {
                    CommonUtil.showMessage(MonitorListActivity.this, "实时监控查询失败");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        monitorAdapter=new MonitorAdapter(this,monitorList);
        monitor_list.setAdapter(monitorAdapter);
    }

    @OnItemClick(R.id.monitor_list)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(MonitorListActivity.this,MonitorActivity.class);
        intent.putExtra("user",monitorList.get(i).getUser());
        startActivity(intent);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }
}
