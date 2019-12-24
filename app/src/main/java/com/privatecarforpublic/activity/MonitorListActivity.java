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
import com.privatecarforpublic.adapter.EmployeeAdapter;
import com.privatecarforpublic.adapter.MonitorAdapter;
import com.privatecarforpublic.model.Car;
import com.privatecarforpublic.model.Monitor;
import com.privatecarforpublic.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MonitorListActivity extends Activity {
    private static final String TAG = "MonitorListActivity";

    @BindView(R.id.monitor_list)
    ListView monitor_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    private List<Monitor> monitorList;
    private MonitorAdapter monitorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor_list);
        ButterKnife.bind(this);
        title.setText("实时监控");
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(MonitorListActivity.this, 25);
    }

    private void init() {
        monitorList = new ArrayList<>();
        monitorList.add(new Monitor(new Car(),new User(),1));
        monitorList.add(new Monitor(new Car(),new User(),1));
        monitorAdapter=new MonitorAdapter(this,monitorList);
        monitor_list.setAdapter(monitorAdapter);
    }

    @OnItemClick(R.id.monitor_list)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(MonitorListActivity.this,MonitorActivity.class);
        intent.putExtra("user",monitorList.get(i).getUser());
        startActivity(intent);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }
}
