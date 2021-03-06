package com.privatecarforpublic.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.ReimItemAdapter;
import com.privatecarforpublic.model.DataModel;
import com.privatecarforpublic.model.Reimburse;
import com.privatecarforpublic.model.RouteModel;
import com.privatecarforpublic.model.SecModel;
import com.privatecarforpublic.model.SecRouteModel;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReimbursementActivity extends Activity {
    private List<Reimburse> reimburseList = new ArrayList<>();
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.reim_list)
    ListView listView;
    @BindView(R.id.side)
    TextView side;

    @OnClick(R.id.back)
    void back(){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimbursement);
        ButterKnife.bind(this);
        title.setText("申请报销");
        side.setVisibility(View.INVISIBLE);

        init();
        ReimItemAdapter adapter = new ReimItemAdapter(ReimbursementActivity.this,R.layout.reim_item, reimburseList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reimburse reimburse = reimburseList.get(position);
                CommonUtil.showMessage(ReimbursementActivity.this, "您点击的是第" + (position+ 1) + "个");
            }
        });

       final RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        //TODO   设置了样例并没有改变
//        refreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

    }

    private void init(){
       /* for(int i = 0; i < 24; ++i){
            Reimburse reimburse = new Reimburse(R.drawable.head_image, "12-05  15:18", i +"始   浙江大学软件学院西门",
                    "终   宁波站", "未报销",50.0);
            reimburseList.add(reimburse);
        }*/
       Thread thread = new Thread(()->{
           try{
               Gson gson = new Gson();
               ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, SharePreferenceUtil.getString(ReimbursementActivity.this,"token",""),  Constants.SERVICE_ROOT +"/Route/total" ,
                       null);
               if(responseResult.getCode() != 200){
                   CommonUtil.showMessage(ReimbursementActivity.this,"无相应的出行路程！");
                   reimburseList.clear();
               }else{
                   reimburseList = gson.fromJson(responseResult.getData(),new TypeToken<List<Reimburse>>(){}.getType());
                   //这里处理一下数据
                   int len = reimburseList.size();
                   for(int i = 0; i < len; ++i){
                       List<SecModel> secModelList = reimburseList.get(i).getSecModels();
                       if(!secModelList.isEmpty()){
                           reimburseList.get(i).setStart(secModelList.get(0).getOrigin());
                           reimburseList.get(i).setEnd(secModelList.get(secModelList.size() - 1).getDestination());
                       }
                   }
                   System.out.println(reimburseList);
               }

           }catch (Exception e){
               e.printStackTrace();
           }
       });
       thread.start();
       try{
           thread.join();
       }catch (InterruptedException e){
           e.printStackTrace();
       }
    }
}
