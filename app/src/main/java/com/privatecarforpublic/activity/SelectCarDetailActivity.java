package com.privatecarforpublic.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    private String start;
    private String end;
    private String reason;
    private List<PointLatDTO> pointList;
    private List<String> nameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_detail);
        ButterKnife.bind(this);
        title.setText("车辆详情");
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(SelectCarDetailActivity.this, 25);
    }

    private void init(){
        reason = getIntent().getStringExtra("reason");
        pointList = (List<PointLatDTO>) getIntent().getSerializableExtra("pointList");
        nameList = (List<String>) getIntent().getSerializableExtra("nameList");
        start = getIntent().getStringExtra("startTime");
        end = getIntent().getStringExtra("endTime");
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ApplyCarDTO applyCarDTO=new ApplyCarDTO(start,end,reason,nameList,pointList,car.getId());
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, SharePreferenceUtil.getString(SelectCarDetailActivity.this, "token", ""), Constants.SERVICE_ROOT + "Route/applyCar", applyCarDTO);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(SelectCarDetailActivity.this, "申请失败");
                        return;
                    } else{
                        CommonUtil.showMessage(SelectCarDetailActivity.this, "申请发送成功");
                        Gson gson = new Gson();
                        Route route = gson.fromJson(responseResult.getData(), Route.class);
                        Intent intent = new Intent();
                        intent.putExtra("routeId",route.getId());
                        intent.putExtra("firstPoint",pointList.get(0));
                        intent.putExtra("secondPoint",pointList.get(1));
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                } catch (Exception e) {
                    CommonUtil.showMessage(SelectCarDetailActivity.this, "申请出错");
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
