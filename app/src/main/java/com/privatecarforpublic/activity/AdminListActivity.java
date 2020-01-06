package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.adapter.AdminAdapter;
import com.privatecarforpublic.adapter.CarAdapter;
import com.privatecarforpublic.model.Car;
import com.privatecarforpublic.model.Master;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class AdminListActivity extends Activity {
    private static final String TAG = "AdminListActivity";
    public final static int TO_ADD_ADMIN = 104;
    public final static int TO_ALTER_ADMIN = 105;

    @BindView(R.id.admin_list)
    ListView admin_list;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    private AdminAdapter adminAdapter;
    private List<Master> adminList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_list);
        ButterKnife.bind(this);
        title.setText("管理员管理");
        side.setText("添加");
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(AdminListActivity.this, 25);
    }

    private void init() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    adminList=new ArrayList<>();
                    Gson gson=new Gson();
                    String token=SharePreferenceUtil.getString(AdminListActivity.this, "token", "");
                    String url=Constants.SERVICE_ROOT + "Master/masterList";
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpGet, token, url, null);
                    if (responseResult.getCode() == 506) {
                        CommonUtil.showMessage(AdminListActivity.this, responseResult.getMessage());
                    } else{
                        adminList=gson.fromJson(responseResult.getData(), new TypeToken<List<Master>>() {
                        }.getType());
                    }
                } catch (Exception e) {
                    CommonUtil.showMessage(AdminListActivity.this, "查询管理员列表出错");
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

        adminAdapter= new AdminAdapter(this,adminList);
        admin_list.setAdapter(adminAdapter);
    }

    @OnItemClick(R.id.admin_list)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(AdminListActivity.this, AddAdminActivity.class);
        intent.putExtra("type",0);
        intent.putExtra("admin", adminList.get(i));
        intent.putExtra("index",i);
        startActivityForResult(intent, TO_ALTER_ADMIN);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @OnClick(R.id.side)
    void add() {
        Intent intent = new Intent(AdminListActivity.this, AddAdminActivity.class);
        intent.putExtra("type",1);
        startActivityForResult(intent, TO_ADD_ADMIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_ALTER_ADMIN && resultCode == Activity.RESULT_OK) {
            int index=data.getIntExtra("index",-1);
            if(index!=-1){
                adminList.remove(index);
                adminAdapter.updateView(adminList);
            }
        }else if (requestCode == TO_ADD_ADMIN && resultCode == Activity.RESULT_OK) {
            Master master=(Master)getIntent().getSerializableExtra("master");
            adminList.add(master);
            adminAdapter.updateView(adminList);
        }
    }
}
