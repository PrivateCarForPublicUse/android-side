package com.privatecarforpublic.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.model.Master;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAdminActivity extends Activity {
    private static final String TAG = "AddAdminActivity";
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.alter)
    Button alter;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    private int type;
    private Master master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_info);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", -1);
        if (type == 0) {
            title.setText("管理员信息");
        } else {
            title.setText("新增管理员");
        }
        side.setVisibility(View.INVISIBLE);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(AddAdminActivity.this, 25);
    }

    private void init(){
        if(type==0){
            master = (Master) getIntent().getSerializableExtra("admin");
            name.setText(master.getMasterName());
            password.setVisibility(View.INVISIBLE);
            alter.setVisibility(View.INVISIBLE);
        }
    }
    @OnClick(R.id.alter)
    void alter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    Map<String, Object> param=new HashMap<>();
                    param.put("name",name.getText().toString());
                    param.put("password",password.getText().toString());
                    String token=SharePreferenceUtil.getString(AddAdminActivity.this, "token", "");
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, token, Constants.SERVICE_ROOT+"register/addMaster", param);
                    if(responseResult.getCode()!=200){
                        CommonUtil.showMessage(AddAdminActivity.this,responseResult.getMessage());
                        return;
                    }
                    Master master = gson.fromJson(responseResult.getData(), Master.class);
                    Intent intent = new Intent(AddAdminActivity.this, AdminListActivity.class);
                    intent.putExtra("master",master);
                    startActivity(intent);
                    AddAdminActivity.this.finish();
                } catch (Exception e) {
                    CommonUtil.showMessage(AddAdminActivity.this,"新增管理员出错");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
