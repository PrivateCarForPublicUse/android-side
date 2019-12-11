package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.application.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register1Activity extends Activity {
    private static final String TAG = "Register1Activity";
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.rePassword)
    EditText rePassword;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user1);
        ButterKnife.bind(this);
        title.setText("用户注册");
        side.setVisibility(View.INVISIBLE);
        //状态栏颜色设置
        StatusBarUtil.setColor(Register1Activity.this, 25);
        MyApplication.addDestroyActivity(this,TAG);
    }

    @OnClick(R.id.next)
    void next() {
        if (password.getText().toString().equals(rePassword.getText().toString())) {
            Intent intent = new Intent(Register1Activity.this, Register2Activity.class);
            intent.putExtra("account",account.getText().toString());
            intent.putExtra("password",password.getText().toString());
            startActivity(intent);
        }
    }
    @OnClick(R.id.back)
    void back() {
        finish();
    }
}
