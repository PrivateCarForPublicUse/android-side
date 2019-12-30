package com.privatecarforpublic.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.model.ApplyCarDTO;
import com.privatecarforpublic.model.Car;
import com.privatecarforpublic.model.PointLatDTO;
import com.privatecarforpublic.model.Route;
import com.privatecarforpublic.model.User;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewCarActivity extends Activity {
    private static final String TAG = "ReviewCarActivity";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;
    @BindView(R.id.picture)
    ImageView picture;
    @BindView(R.id.license)
    TextView license;
    @BindView(R.id.brand_type)
    TextView brand_type;
    @BindView(R.id.owner)
    TextView owner;
    @BindView(R.id.workNumber)
    TextView workNumber;
    @BindView(R.id.insuranceCompany)
    TextView insuranceCompany;
    @BindView(R.id.strongInsurancePolicy)
    TextView strongInsurancePolicy;
    @BindView(R.id.commercialInsurancePolicy)
    TextView commercialInsurancePolicy;
    @BindView(R.id.agree)
    Button agree;
    @BindView(R.id.reject)
    Button reject;


    private int type;
    private Car car;
    private User user;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_car);
        ButterKnife.bind(this);
        type=getIntent().getIntExtra("type",-1);
        if(type==0){
            title.setText("车辆信息");
            agree.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
        }else{
            title.setText("车辆审核");
        }
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(ReviewCarActivity.this, 25);
    }

    private void init(){
        index=getIntent().getIntExtra("index",-1);
        car = (Car) getIntent().getSerializableExtra("car");
        //Picasso.get().load(car.getPicture()).into(picture);
        license.setText(car.getLicense());
        brand_type.setText(car.getBrand()+"·"+car.getType());
        insuranceCompany.setText(car.getInsuranceCompany());
        strongInsurancePolicy.setText(car.getStrongInsurancePolicy());
        commercialInsurancePolicy.setText(car.getCommercialInsurancePolicy());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson=new Gson();
                    String url=Constants.SERVICE_ROOT + "user/"+car.getUserId();
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, "", url, null);
                    if (responseResult.getCode() !=200 ) {
                        CommonUtil.showMessage(ReviewCarActivity.this, responseResult.getMessage());
                    } else{
                        user=gson.fromJson(responseResult.getData(), User.class);
                    }
                } catch (Exception e) {
                    CommonUtil.showMessage(ReviewCarActivity.this, "查询车主信息失败");
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
        if(user!=null){
            owner.setText(user.getName()+"");
            workNumber.setText(user.getWorkNumber()+"");
        }
    }

    @OnClick(R.id.agree)
    void agree() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, null, Constants.SERVICE_ROOT + "car/passAddCar?carId="+car.getId()+"&isUse=0", null);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(ReviewCarActivity.this, "审核失败");
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("index",index);
                    setResult(Activity.RESULT_OK, intent);
                    finish();   //finish应该写到这个地方
                } catch (Exception e) {
                    CommonUtil.showMessage(ReviewCarActivity.this, "审核出错");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick(R.id.reject)
    void reject() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, null, Constants.SERVICE_ROOT + "car/passAddCar?carId="+car.getId()+"&isUse=-1", null);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(ReviewCarActivity.this, "审核失败");
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("index",index);
                    setResult(Activity.RESULT_OK, intent);
                    finish();   //finish应该写到这个地方
                } catch (Exception e) {
                    CommonUtil.showMessage(ReviewCarActivity.this, "审核出错");
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @OnClick(R.id.back)
    void back() {
        finish();
    }

}
