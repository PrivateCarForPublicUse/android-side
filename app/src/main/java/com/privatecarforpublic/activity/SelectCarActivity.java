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
    private CarAdapter privateCarAdapter;
    private CarAdapter publicCarAdapter;

    private User user;
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //公有车列表
                    Gson gson = new Gson();
                    Map<String, Object> param = new HashMap<>();
                    param.put("startTime", sdf.format(start));
                    param.put("endTime", sdf.format(end));
                    param.put("isMine", 0);
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, SharePreferenceUtil.getString(SelectCarActivity.this, "token", ""), Constants.SERVICE_ROOT + "car/getCarByTime", param);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(SelectCarActivity.this, "无可用公车");
                        publicCarList=new ArrayList<>();
                    } else{
                        publicCarList = gson.fromJson(responseResult.getData(), new TypeToken<List<Car>>() {
                        }.getType());
                    }

                    //私有车列表
                    param.put("isMine", 1);
                    responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, SharePreferenceUtil.getString(SelectCarActivity.this, "token", ""), Constants.SERVICE_ROOT + "car/getCarByTime", param);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(SelectCarActivity.this, "无可用私车");
                        privateCarList = new ArrayList<>();
                    }else{
                        privateCarList = gson.fromJson(responseResult.getData(), new TypeToken<List<Car>>() {
                        }.getType());
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
        intent.putExtra("startTime",sdf.format((Date) getIntent().getSerializableExtra("startTime")));
        intent.putExtra("endTime",sdf.format((Date) getIntent().getSerializableExtra("endTime")));
        intent.putExtra("user",getIntent().getSerializableExtra("user"));
        intent.putExtra("reason",getIntent().getStringExtra("reason"));
        intent.putExtra("pointList",getIntent().getSerializableExtra("pointList"));
        intent.putExtra("nameList",getIntent().getSerializableExtra("nameList"));
        startActivityForResult(intent, TO_SELECT_CAR);
    }

    @OnItemClick(R.id.public_car_list)
    public void onPublicItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(SelectCarActivity.this, SelectCarDetailActivity.class);
        intent.putExtra("car", publicCarList.get(i));
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
            setResult(Activity.RESULT_OK, intent);
            finish();   //finish应该写到这个地方
        }
    }
}
