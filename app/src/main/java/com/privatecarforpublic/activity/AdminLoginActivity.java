package com.privatecarforpublic.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.MainActivity;
import com.privatecarforpublic.R;
import com.privatecarforpublic.application.MyApplication;
import com.privatecarforpublic.model.Master;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminLoginActivity extends Activity {
    private static final String TAG = "AdminLoginActivity";
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;

    private LoadingDialog ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_admin);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(AdminLoginActivity.this, 25);
    }

    @OnClick(R.id.login)
    void login() {
        //ld.show();
        //CommonUtil.processLoading(ld,1);
        MyApplication.destroyAll();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    Map<String, Object> param=new HashMap<>();
                    param.put("masterName",account.getText().toString());
                    param.put("password",password.getText().toString());
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, null, Constants.SERVICE_ROOT+"Master/login", param);
                    if(responseResult.getCode()!=200){
                        CommonUtil.showMessage(AdminLoginActivity.this,"账号或密码错误");
                        return;
                    }
                    Master master = gson.fromJson(responseResult.getData(), Master.class);
                    //SharePreferenceUtil.setString(AdminLoginActivity.this, "token", account.getToken());
                    SharePreferenceUtil.setString(AdminLoginActivity.this, "masterId", master.getId() + "");
                    Intent intent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
                    intent.putExtra("master",master);
                    startActivity(intent);
                    AdminLoginActivity.this.finish();
                } catch (Exception e) {
                    CommonUtil.showMessage(AdminLoginActivity.this,"登录出错");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void init() {
        ButterKnife.bind(this);
        ld = new LoadingDialog(this)
                .setLoadingText("加载中")
                .setInterceptBack(false);
    }

}
