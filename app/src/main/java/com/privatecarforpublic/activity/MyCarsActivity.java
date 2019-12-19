package com.privatecarforpublic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.CarAdapter;
import com.privatecarforpublic.model.Car;
import com.privatecarforpublic.util.CommonUtil;

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
