package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.application.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register2Activity extends Activity {
    private static final String TAG = "Register2Activity";
    @BindView(R.id.city)
    Spinner city;
    @BindView(R.id.company)
    Spinner company;
    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user2);
        ButterKnife.bind(this);
        title.setText("用户注册");
        side.setVisibility(View.INVISIBLE);
        //状态栏颜色设置
        StatusBarUtil.setColor(Register2Activity.this, 25);
        MyApplication.addDestroyActivity(this,TAG);
    }

    @OnClick(R.id.next)
    void next() {
        Intent intent = new Intent(Register2Activity.this, IdCardActivity.class);
        intent.putExtra("name", name.getText().toString());
        startActivity(intent);

    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }
}
