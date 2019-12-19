package com.privatecarforpublic.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.privatecarforpublic.MainActivity;
import com.privatecarforpublic.R;
import com.privatecarforpublic.application.MyApplication;
import com.privatecarforpublic.util.CommonUtil;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

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
        MyApplication.addDestroyActivity(this,TAG);
    }

    @OnClick(R.id.login)
    void login() {
        ld.show();
        CommonUtil.processLoading(ld,1);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        //this.finish();
    }

    @OnClick(R.id.register)
    void register() {
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
