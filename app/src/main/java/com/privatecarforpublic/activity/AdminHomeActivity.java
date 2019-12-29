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
                Intent intent = new Intent(AdminHomeActivity.this, AdminEmployeeListActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                break;
            case (1):
                Intent intent1 = new Intent(AdminHomeActivity.this, ApplyListActivity.class);
                startActivity(intent1);
                break;
            case (2):
                Intent intent2 = new Intent(AdminHomeActivity.this, AdminCarListActivity.class);
                intent2.putExtra("type",1);
                startActivity(intent2);
                break;
            case (5):
                Intent intent5 = new Intent(AdminHomeActivity.this, AdminEmployeeListActivity.class);
                intent5.putExtra("type",0);
                startActivity(intent5);
                break;
            case (6):
                Intent intent6 = new Intent(AdminHomeActivity.this, AdminListActivity.class);
                startActivity(intent6);
                break;
            case (7):
                Intent intent7 = new Intent(AdminHomeActivity.this, MonitorListActivity.class);
                startActivity(intent7);
                break;
            case (8):
                Intent intent8 = new Intent(AdminHomeActivity.this, AdminCarListActivity.class);
                intent8.putExtra("type",0);
                startActivity(intent8);
                break;
            default:
                break;
        }
    }
}
