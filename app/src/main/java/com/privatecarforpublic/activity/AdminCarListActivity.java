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

public class AdminCarListActivity extends Activity {
    private static final String TAG = "AdminCarListActivity";
    public final static int TO_REVIEW_CAR = 104;

    @BindView(R.id.car_list)
    ListView car_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    private int type;
    private CarAdapter carAdapter;
    private List<Car> carList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_car_list);
        ButterKnife.bind(this);
        type=getIntent().getIntExtra("type",-1);
        if(type==0){
            title.setText("车辆管理");
        }else{
            title.setText("车辆审核");
        }
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(AdminCarListActivity.this, 25);
    }

    private void init() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    carList=new ArrayList<>();
                    Gson gson=new Gson();
                    Long masterId=Long.parseLong(SharePreferenceUtil.getString(AdminCarListActivity.this, "masterId", ""));
                    String url;
                    if(type==0){
                        url=Constants.SERVICE_ROOT + "car/fd?masterId="+masterId;
                    }else{
                        url=Constants.SERVICE_ROOT + "car/reviewAddCar?masterId="+masterId;
                    }
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, "", url, null);
                    if (responseResult.getCode() !=200) {
                        CommonUtil.showMessage(AdminCarListActivity.this, responseResult.getMessage());
                    } else{
                        carList=gson.fromJson(responseResult.getData(), new TypeToken<List<Car>>() {
                        }.getType());
                    }
                } catch (Exception e) {
                    CommonUtil.showMessage(AdminCarListActivity.this, "查询新车列表出错");
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

        carAdapter = new CarAdapter(this, carList,1);
        car_list.setAdapter(carAdapter);
    }

    @OnItemClick(R.id.car_list)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(AdminCarListActivity.this, ReviewCarActivity.class);
        intent.putExtra("car", carList.get(i));
        intent.putExtra("type", type);
        intent.putExtra("index",i);
        startActivityForResult(intent, TO_REVIEW_CAR);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_REVIEW_CAR && resultCode == Activity.RESULT_OK) {
            int index=data.getIntExtra("index",-1);
            if(index!=-1){
                carList.remove(index);
                carAdapter.updateView(carList);
            }
        }
    }
}
