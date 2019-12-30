package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.ApplyAdapter;
import com.privatecarforpublic.model.Apply;
import com.privatecarforpublic.model.RouteModel;
import com.privatecarforpublic.model.SecRoute;
import com.privatecarforpublic.model.SecRouteModel;
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
import butterknife.OnItemClick;

public class ApplyListActivity extends Activity {
    private static final String TAG = "ApplyListActivity";
    public final static int TO_REVIEW_APPLY = 104;

    @BindView(R.id.apply_list)
    ListView apply_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    private ApplyAdapter applyAdapter;
    private List<Apply> applyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_list);
        ButterKnife.bind(this);
        title.setText("申请审核");
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(ApplyListActivity.this, 25);
    }

    private void init() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson=new Gson();
                    Long masterId=Long.parseLong(SharePreferenceUtil.getString(ApplyListActivity.this, "masterId", ""));
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, "", Constants.SERVICE_ROOT + "/Route/status?status=0&masterId="+masterId, null);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(ApplyListActivity.this, "查询申请列表失败");
                        applyList=new ArrayList<>();
                    } else{
                        applyList=new ArrayList<>();
                        List<RouteModel> list=gson.fromJson(responseResult.getData(), new TypeToken<List<RouteModel>>() {
                        }.getType());
                        for(RouteModel routeModel:list){
                            List<SecRoute> secRouteList=new ArrayList<>();
                            for(SecRouteModel secRouteModel:routeModel.getSecRoutes()){
                                secRouteList.add(secRouteModel.getSecRoute());
                            }
                            Apply apply=new Apply(routeModel.getUser(),routeModel.getCar()
                                    ,routeModel.getRoute().getId(),routeModel.getRoute().getApplyStartTime()
                                    ,routeModel.getRoute().getApplyEndTime(),routeModel.getRoute().getStatus()
                                    ,routeModel.getRoute().getReason(),secRouteList);
                            applyList.add(apply);
                        }
                    }
                } catch (Exception e) {
                    CommonUtil.showMessage(ApplyListActivity.this, "查询申请列表出错");
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

        applyAdapter = new ApplyAdapter(this, applyList);
        apply_list.setAdapter(applyAdapter);
    }

    @OnItemClick(R.id.apply_list)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(ApplyListActivity.this, ReviewApplyActivity.class);
        intent.putExtra("apply", applyList.get(i));
        intent.putExtra("index",i);
        startActivityForResult(intent, TO_REVIEW_APPLY);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_REVIEW_APPLY && resultCode == Activity.RESULT_OK) {
            int index=data.getIntExtra("index",-1);
            if(index!=-1){
                applyList.remove(index);
                applyAdapter.updateView(applyList);
            }
        }
    }
}
