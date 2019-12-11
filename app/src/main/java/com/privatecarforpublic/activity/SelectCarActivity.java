package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.CarAdapter;
import com.privatecarforpublic.model.Car;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class SelectCarActivity extends Activity {

    @BindView(R.id.private_car_list)
    ListView private_car_list;
    @BindView(R.id.public_car_list)
    ListView public_car_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    private List<Car> privateCarList;
    private List<Car> publicCarList;
    private CarAdapter privateCarAdapter;
    private CarAdapter publicCarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_car);
        ButterKnife.bind(this);
        title.setText("车辆选择");
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(SelectCarActivity.this, 25);
    }

    private void init(){
        privateCarList=new ArrayList<>();
        privateCarList.add(new Car());
        publicCarList=new ArrayList<>();
        publicCarList.add(new Car());
        publicCarList.add(new Car());
        publicCarList.add(new Car());
        privateCarAdapter=new CarAdapter(this,privateCarList);
        publicCarAdapter=new CarAdapter(this,publicCarList);
        private_car_list.setAdapter(privateCarAdapter);
        public_car_list.setAdapter(publicCarAdapter);
    }

    @OnItemClick(R.id.private_car_list)
    public void onPrivateItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @OnItemClick(R.id.public_car_list)
    public void onPublicItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }
}
