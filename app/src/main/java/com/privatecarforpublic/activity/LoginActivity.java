package com.privatecarforpublic.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.MainActivity;
import com.privatecarforpublic.R;
import com.privatecarforpublic.application.MyApplication;
import com.privatecarforpublic.model.Account;
import com.privatecarforpublic.model.User;
import com.privatecarforpublic.response.ResponseResult;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.Constants;
import com.privatecarforpublic.util.HttpRequestMethod;
import com.privatecarforpublic.util.JsonUtil;
import com.privatecarforpublic.util.SharePreferenceUtil;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;

    private LoadingDialog ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);
        init();
        //状态栏颜色设置
        StatusBarUtil.setColor(LoginActivity.this, 25);
    }

    @OnClick(R.id.login)
    void login() {
        //ld.show();
        CommonUtil.processLoading(ld,1);
        /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);*/
        //this.finish();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    Map<String, Object> param=new HashMap<>();
                    param.put("phoneNumber",account.getText().toString());
                    param.put("password",password.getText().toString());
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, null, Constants.SERVICE_ROOT+"authorize/login/phone", param);
                    if(responseResult.getCode()!=200){
                        CommonUtil.showMessage(LoginActivity.this,responseResult.getMessage());
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(responseResult.getData());
                    User user = gson.fromJson(jsonObject.getString("userOrMaster"), User.class);
                    Account account = gson.fromJson(jsonObject.getString("account"), Account.class);
                    SharePreferenceUtil.setString(LoginActivity.this, "token", account.getToken());
                    SharePreferenceUtil.setString(LoginActivity.this, "userId", user.getId() + "");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } catch (Exception e) {
                    CommonUtil.showMessage(LoginActivity.this,"登录出错");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick(R.id.register)
    void register() {
        MyApplication.addDestroyActivity(this,TAG);
        Intent intent = new Intent(LoginActivity.this, Register1Activity.class);
        startActivity(intent);
    }

    private void init() {
        ButterKnife.bind(this);
        ld = new LoadingDialog(this)
                .setLoadingText("加载中")
                .setInterceptBack(false);
    }

}
