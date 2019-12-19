package com.privatecarforpublic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.CarAdapter;
import com.privatecarforpublic.model.Car;
import com.privatecarforpublic.util.CommonUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCarsActivity extends Activity {
    private List<Car> carList = new ArrayList<>();
    private List<Car> carList1 = new ArrayList<>();
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;
    @BindView(R.id.my_cars_private_car)
    ListView listView;
    @BindView(R.id.my_cars_public_car)
    ListView listView1;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.refreshLayout1)
    RefreshLayout refreshLayout1;

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @OnClick(R.id.side)
    void add() {
        CommonUtil.showMessage(this, "您点击了增加按钮");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cars);
        ButterKnife.bind(this);
        title.setText("车辆信息");
        side.setText("增加");

        init();
        CarAdapter carAdapter = new CarAdapter(MyCarsActivity.this, carList,2);
        listView.setAdapter(carAdapter);

        CarAdapter carAdapter1 = new CarAdapter(MyCarsActivity.this, carList1,2);
        listView1.setAdapter(carAdapter1);

        listView.setOnItemClickListener(((parent, view, position, id) -> {
            Car car = carList.get(position);
            CommonUtil.showMessage(MyCarsActivity.this, "您点击的是第" + (position + 1) + "个");
        }));

        listView1.setOnItemClickListener(((parent, view, position, id) -> {
            Car car = carList1.get(position);
            CommonUtil.showMessage(MyCarsActivity.this, "您点击的是第" + (position + 1) + "个");
        }));

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

        refreshLayout1.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout1.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout1.setOnLoadMoreListener(refreshLayout1 -> {
            refreshLayout1.finishLoadMore(2000);//传入false表示加载失败
        });
    }

    private void init() {
        for (int i = 0; i < 10; ++i) {
            Car car = new Car().builder().brand("保时捷卡宴111" + i)
                    .journey(30).starOfCar(i % 5 + 1).build();
            carList.add(car);
        }

        for (int i = 0; i < 5; ++i) {
            Car car = new Car().builder().brand("保时捷卡宴222")
                    .journey(30).starOfCar(i % 5 + 1).build();
            carList1.add(car);
        }
    }
}
