package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.SecRouteAdapter;
import com.privatecarforpublic.model.Apply;
import com.privatecarforpublic.model.Car;
import com.privatecarforpublic.model.SecRoute;
import com.privatecarforpublic.model.User;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewApplyActivity extends Activity {
    private static final String TAG = "ReviewApplyActivity";

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;
    @BindView(R.id.license)
    TextView license;
    @BindView(R.id.brand_type)
    TextView brand_type;
    @BindView(R.id.star_car)
    TextView star_car;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.workNumber)
    TextView workNumber;
    @BindView(R.id.star_user)
    TextView star_user;
    @BindView(R.id.start_time)
    TextView start_time;
    @BindView(R.id.end_time)
    TextView end_time;
    @BindView(R.id.reason)
    TextView reason;
    @BindView(R.id.sec_route_list)
    ListView sec_route_list;

    private Apply apply;
    private int index;

    private SecRouteAdapter secRouteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_detail);
        init();
        title.setText("用车审核");
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(ReviewApplyActivity.this, 25);
    }

    private void init() {
        index=getIntent().getIntExtra("index",-1);
        apply=(Apply) getIntent().getSerializableExtra("apply");
        User user=apply.getUser();
        Car car=apply.getCar();
        ButterKnife.bind(this);
        license.setText(apply.getCar().getLicense());
        brand_type.setText(car.getBrand()+"·"+car.getType());
        star_car.setText(car.getStarOfCar()+"");
        name.setText(user.getName());
        workNumber.setText(user.getWorkNumber()+"");
        star_user.setText(user.getStarLevel()+"");
        start_time.setText(apply.getApplyStartTime());
        end_time.setText(apply.getApplyEndTime());
        reason.setText(apply.getReason());
        secRouteAdapter=new SecRouteAdapter(this,apply.getSecRouteList());
        sec_route_list.setAdapter(secRouteAdapter);
    }

    @OnClick(R.id.agree)
    void agree() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> param = new HashMap<>();
                    param.put("routeId", apply.getId());
                    param.put("status", 1);
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, null, Constants.SERVICE_ROOT + "Master/reviewUseCar", param);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(ReviewApplyActivity.this, "审核失败");
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("index",index);
                    setResult(Activity.RESULT_OK, intent);
                    finish();   //finish应该写到这个地方
                } catch (Exception e) {
                    CommonUtil.showMessage(ReviewApplyActivity.this, "审核出错");
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
                    Map<String, Object> param = new HashMap<>();
                    param.put("routeId", apply.getId());
                    param.put("status", -1);
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, null, Constants.SERVICE_ROOT + "Master/reviewUseCar", param);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(ReviewApplyActivity.this, "审核失败");
                        return;
                    }
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();   //finish应该写到这个地方
                } catch (Exception e) {
                    CommonUtil.showMessage(ReviewApplyActivity.this, "审核出错");
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
