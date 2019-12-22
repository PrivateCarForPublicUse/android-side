package com.privatecarforpublic.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.application.MyApplication;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseTypeActivity extends Activity {
    private static final String TAG = "ChooseTypeActivity";
    @BindView(R.id.user)
    Button user;
    @BindView(R.id.admin)
    Button admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_type);
        ButterKnife.bind(this);
        //状态栏颜色设置
        StatusBarUtil.setColor(ChooseTypeActivity.this, 25);
    }

    @OnClick(R.id.user)
    void chooseUser() {
        MyApplication.addDestroyActivity(this,TAG);
        Intent intent = new Intent(ChooseTypeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.admin)
    void chooseAdmin() {
        MyApplication.addDestroyActivity(this,TAG);
        Intent intent = new Intent(ChooseTypeActivity.this, AdminLoginActivity.class);
        startActivity(intent);
    }

}
