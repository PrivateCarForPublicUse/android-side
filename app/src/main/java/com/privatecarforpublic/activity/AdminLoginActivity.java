package com.privatecarforpublic.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.MainActivity;
import com.privatecarforpublic.R;
import com.privatecarforpublic.application.MyApplication;
import com.privatecarforpublic.util.CommonUtil;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

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
        ld.show();
        CommonUtil.processLoading(ld,1);
        Intent intent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        this.finish();
        MyApplication.destroyAll();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> map=new HashMap<>();
                    map.put("id",0);
                    map.put("password","string");
                    map.put("token","");
                    String  param= JSON.toJSONString(map);
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, "", Constants.SERVICE_ROOT+"user/", param);
                    if(responseResult.getCode()!=200){
                        CommonUtil.showMessage(LoginActivity.this,"登录出错");
                        return;
                    }
                    Gson gson = new Gson();
                    User user = gson.fromJson(responseResult.getData(), User.class);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } catch (Exception e) {
                    CommonUtil.showMessage(LoginActivity.this,"登录出错");
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    private void init() {
        ButterKnife.bind(this);
        ld = new LoadingDialog(this)
                .setLoadingText("加载中")
                .setInterceptBack(false);
    }

}
