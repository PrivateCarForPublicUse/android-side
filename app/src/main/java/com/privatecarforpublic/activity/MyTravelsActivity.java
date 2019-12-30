package com.privatecarforpublic.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.MyTravelsAdapter;
import com.privatecarforpublic.model.MyTravels;
import com.privatecarforpublic.model.MyTravelsUtil;
import com.privatecarforpublic.model.RouteModel;
import com.privatecarforpublic.model.SecRoute;
import com.privatecarforpublic.model.SecRouteModel;
import com.privatecarforpublic.model.User;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Builder;

/**
 * @description 我的行程列表界面
 */
public class MyTravelsActivity extends Activity {
    private List<RouteModel> myTravelsList = new ArrayList<>();
    private List<MyTravelsUtil> list = new ArrayList<>();
    private String userId = null;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.my_travels_list)
    ListView listView;

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @BindView(R.id.side)
    TextView side;
   /* @BindView(R.id.title_bar)
    RelativeLayout layout;*/

    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_travels);
        ButterKnife.bind(this);
        title.setText("我的行程");
        side.setText("报销");

        init();
        MyTravelsAdapter adapter = new MyTravelsAdapter(MyTravelsActivity.this, R.layout.my_travel_item, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(((parent, view, position, id) -> {
            MyTravelsUtil myTravels = list.get(position);
            CommonUtil.showMessage(MyTravelsActivity.this, "您点击的是第" + (position + 1) + "个");
        }));

        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        //TODO   设置了样例并没有改变
//        refreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            refreshLayout1.finishLoadMore(2000);//传入false表示加载失败
        });

    }

    private void init() {
        /*for (int i = 0; i < 24; ++i) {
            MyTravels myTravels = new MyTravels().builder().carStartTime(new Date().toString()).origin("始   " + "浙江大学宁波软院")
                    .destination("终   " + "浙江大学宁波软院").status(1).isReimburse(0).build();
            myTravelsList.add(myTravels);
        }*/
        Thread thread = new Thread(()->{
            try{
                Gson gson = new Gson();
                userId = SharePreferenceUtil.getString(MyTravelsActivity.this,"userId","");
                System.out.println(userId);
                /*ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, SharePreferenceUtil
                        .getString(MyTravelsActivity.this, "token", ""), Constants.SERVICE_ROOT + "/Route/userId?userId=" + userId, null);*/
                ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, SharePreferenceUtil
                        .getString(MyTravelsActivity.this, "token", ""), Constants.SERVICE_ROOT + "/Route/fd", null);
                if(responseResult.getCode() != 200){
                    CommonUtil.showMessage(MyTravelsActivity.this,"无相应的出行路程！");
                    myTravelsList.clear();
                }else{
                    myTravelsList = gson.fromJson(responseResult.getData(),new TypeToken<List<RouteModel>>(){}.getType());
                    System.out.println(myTravelsList);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         *  TODO   这里的getSecRoutesModel  为null
         *  TODO  而且速度也很慢
         */
        for(RouteModel routeModel : myTravelsList){
            if(null == routeModel || null ==routeModel.getSecRoutesModel())
                continue;
            for(SecRouteModel secRouteModel : routeModel.getSecRoutesModel()){
                if(null == secRouteModel)
                    break;
                list.add(new MyTravelsUtil().builder().carStartTime(null == secRouteModel.getSettlement() ? "默认出发时间" : secRouteModel.getSettlement().getCarStartTime())
                        .origin(null == secRouteModel.getSecRoute() ? "默认出发地点" :secRouteModel.getSecRoute().getOrigin())
                        .destination(null == secRouteModel.getSecRoute() ? "默认终止地点" :secRouteModel.getSecRoute().getDestination())
                        .isReimburse(null == routeModel.getRoute() ?  0 : routeModel.getRoute().getIsReimburse())
                        .status(null == routeModel.getRoute() ? 0 : routeModel.getRoute().getStatus()).build());
            }
        }
    }
}
