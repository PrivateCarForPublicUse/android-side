package com.privatecarforpublic.activity;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.MyTravelsAdapter;
import com.privatecarforpublic.model.MyTravels;
import com.privatecarforpublic.util.CommonUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @description 我的行程列表界面
 */
public class MyTravelsActivity extends Activity {
    private List<MyTravels> myTravelsList = new ArrayList<>();

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
        MyTravelsAdapter adapter = new MyTravelsAdapter(MyTravelsActivity.this, R.layout.my_travel_item, myTravelsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(((parent, view, position, id) -> {
            MyTravels myTravels = myTravelsList.get(position);
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
        for (int i = 0; i < 24; ++i) {
            MyTravels myTravels = new MyTravels(new Date(), "始   " + "浙江大学宁波软院",
                    "终" + "浙江大学宁波软院", false, false);
            myTravelsList.add(myTravels);
        }
    }
}
