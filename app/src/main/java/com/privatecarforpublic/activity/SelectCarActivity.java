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
import com.privatecarforpublic.adapter.CarAdapter;
import com.privatecarforpublic.model.Car;
import com.privatecarforpublic.model.CarModel;
import com.privatecarforpublic.model.PointLatDTO;
import com.privatecarforpublic.model.User;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class SelectCarActivity extends Activity {
    private static final String TAG = "SelectCarActivity";
    public final static int TO_SELECT_CAR = 104;

    @BindView(R.id.private_car_list)
    ListView private_car_list;
    @BindView(R.id.public_car_list)
    ListView public_car_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    private List<Car> privateCarList;
    private List<Car> publicCarList;
    private List<User> privateUserList;
    private List<User> publicUserList;
    private CarAdapter privateCarAdapter;
    private CarAdapter publicCarAdapter;

    private Date start;
    private Date end;
    private String reason;
    private List<PointLatDTO> pointList;
    private List<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_car);
        ButterKnife.bind(this);
        title.setText("车辆选择");
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(SelectCarActivity.this, 25);
    }

    private void init() {
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        start = (Date) getIntent().getSerializableExtra("startTime");
        end = (Date) getIntent().getSerializableExtra("endTime");
        reason=getIntent().getStringExtra("reason");
        pointList=(List<PointLatDTO>) getIntent().getSerializableExtra("pointList");
        nameList=(List<String>) getIntent().getSerializableExtra("nameList");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //公有车列表
                    Gson gson = new Gson();
                    publicCarList=new ArrayList<>();
                    publicUserList=new ArrayList<>();
                    Map<String, Object> param = new HashMap<>();
                    param.put("startTime", sdf.format(start));
                    param.put("endTime", sdf.format(end));
                    param.put("isMine", 0);
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, SharePreferenceUtil.getString(SelectCarActivity.this, "token", ""), Constants.SERVICE_ROOT + "car/getCarByTime", param);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(SelectCarActivity.this, "无可用公车");
                    } else{
                        List<CarModel> carModels=gson.fromJson(responseResult.getData(), new TypeToken<List<CarModel>>() {
                        }.getType());
                        for(CarModel carModel:carModels){
                            publicCarList.add(carModel.getCar());
                            publicUserList.add(carModel.getUser());
                        }
                    }

                    privateCarList = new ArrayList<>();
                    privateUserList=new ArrayList<>();
                    //私有车列表
                    param.put("isMine", 1);
                    responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, SharePreferenceUtil.getString(SelectCarActivity.this, "token", ""), Constants.SERVICE_ROOT + "car/getCarByTime", param);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(SelectCarActivity.this, "无可用私车");
                    }else{
                        List<CarModel> carModels=gson.fromJson(responseResult.getData(), new TypeToken<List<CarModel>>() {
                        }.getType());
                        for(CarModel carModel:carModels){
                            privateCarList.add(carModel.getCar());
                            privateUserList.add(carModel.getUser());
                        }
                    }
                } catch (Exception e) {
                    CommonUtil.showMessage(SelectCarActivity.this, "查询可用车辆失败");
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
        publicCarAdapter = new CarAdapter(this, publicCarList, 1);
        public_car_list.setAdapter(publicCarAdapter);

        privateCarAdapter = new CarAdapter(this, privateCarList, 1);
        private_car_list.setAdapter(privateCarAdapter);
    }

    @OnItemClick(R.id.private_car_list)
    public void onPrivateItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        Intent intent = new Intent(SelectCarActivity.this, SelectCarDetailActivity.class);
        intent.putExtra("car", privateCarList.get(i));
        intent.putExtra("user",privateUserList.get(i));
        intent.putExtra("startTime",sdf.format(start));
        intent.putExtra("endTime",sdf.format(end));
        intent.putExtra("reason",reason);
        intent.putExtra("pointList",(Serializable)pointList);
        intent.putExtra("nameList",(Serializable)nameList);
        startActivityForResult(intent, TO_SELECT_CAR);
    }

    @OnItemClick(R.id.public_car_list)
    public void onPublicItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        Intent intent = new Intent(SelectCarActivity.this, SelectCarDetailActivity.class);
        intent.putExtra("car", publicCarList.get(i));
        intent.putExtra("user",publicUserList.get(i));
        intent.putExtra("startTime",sdf.format(start));
        intent.putExtra("endTime",sdf.format(end));
        intent.putExtra("reason",reason);
        intent.putExtra("pointList",(Serializable)pointList);
        intent.putExtra("nameList",(Serializable)nameList);
        startActivityForResult(intent, TO_SELECT_CAR);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_SELECT_CAR && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra("routeId",data.getLongExtra("routeId",-1));
            intent.putExtra("firstPoint",data.getSerializableExtra("firstPoint"));
            intent.putExtra("secondPoint",data.getSerializableExtra("secondPoint"));
            setResult(Activity.RESULT_OK, intent);
            finish();   //finish应该写到这个地方
        }
    }
}
