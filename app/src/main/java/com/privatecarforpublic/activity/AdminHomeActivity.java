package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.FunctionAdapter;
import com.privatecarforpublic.model.Function;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class AdminHomeActivity extends Activity {
    private static final String TAG = "AdminHomeActivity";

    @BindView(R.id.function_grid_view)
    GridView function_grid_view;
    @BindView(R.id.loop_image)
    ImageView loop_image;

    private List<Function> functionList;
    private FunctionAdapter functionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        ButterKnife.bind(this);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(AdminHomeActivity.this, 25);
    }

    private void init() {
        functionList = new ArrayList<>();
        functionList.add(new Function(R.drawable.star, "员工审核"));
        functionList.add(new Function(R.drawable.star, "用车审核"));
        functionList.add(new Function(R.drawable.star, "车辆审核"));
        functionList.add(new Function(R.drawable.star, "报销审核"));
        functionList.add(new Function(R.drawable.star, "用车记录"));
        functionList.add(new Function(R.drawable.star, "员工管理"));
        functionList.add(new Function(R.drawable.star, "管理员管理"));
        functionList.add(new Function(R.drawable.star, "实时监控"));
        functionList.add(new Function(R.drawable.star, "车辆信息"));
        functionAdapter = new FunctionAdapter(this, functionList);
        function_grid_view.setAdapter(functionAdapter);
    }

    @OnItemClick(R.id.function_grid_view)
    public void onPrivateItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case (0):
                Intent intent = new Intent(AdminHomeActivity.this, ReviewEmployeeActivity.class);
                startActivity(intent);
                break;
            case (1):
                Intent intent1 = new Intent(AdminHomeActivity.this, ApplyListActivity.class);
                startActivity(intent1);
                break;
            case (7):
                Intent intent7 = new Intent(AdminHomeActivity.this, MonitorListActivity.class);
                startActivity(intent7);
                break;
            default:
                break;
        }
    }
}
