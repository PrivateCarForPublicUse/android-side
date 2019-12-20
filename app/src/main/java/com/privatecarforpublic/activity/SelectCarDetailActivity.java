package com.privatecarforpublic.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.MainActivity;
import com.privatecarforpublic.R;
import com.privatecarforpublic.application.MyApplication;
import com.privatecarforpublic.model.Car;
import com.privatecarforpublic.model.User;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.squareup.picasso.Picasso;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectCarDetailActivity extends Activity {
    private static final String TAG = "SelectCarDetailActivity";
    private Car car;
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
    @BindView(R.id.starOfCar)
    TextView starOfCar;
    @BindView(R.id.insuranceCompany)
    TextView insuranceCompany;
    @BindView(R.id.strongInsurancePolicy)
    TextView strongInsurancePolicy;
    @BindView(R.id.commercialInsurancePolicy)
    TextView commercialInsurancePolicy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_detail);
        ButterKnife.bind(this);
        title.setText("车辆详情");
        side.setVisibility(View.INVISIBLE);
        //状态栏颜色设置
        StatusBarUtil.setColor(SelectCarDetailActivity.this, 25);
    }

    private void init(){
        car=(Car)getIntent().getSerializableExtra("car");
        //Picasso.get().load(car.getPicture()).into(picture);
        license.setText(car.getLicense());
        brand_type.setText(car.getBrand()+"·"+car.getType());
        owner.setText(car.getUserId()+"");
        starOfCar.setText(car.getStarOfCar()+"");
        insuranceCompany.setText(car.getInsuranceCompany());
    }
    @OnClick(R.id.determine)
    void determine() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    @OnClick(R.id.back)
    void back() {
        finish();
    }

}
