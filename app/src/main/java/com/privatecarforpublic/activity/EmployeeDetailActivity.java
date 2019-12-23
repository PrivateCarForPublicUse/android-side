package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class EmployeeDetailActivity extends Activity {
    private static final String TAG = "EmployeeDetailActivity";
    public static final int TO_SHOW_DRIVER_LICENSE=110;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_detail);
        ButterKnife.bind(this);
        title.setText("员工详情");
        side.setVisibility(View.INVISIBLE);
        //状态栏颜色设置
        StatusBarUtil.setColor(EmployeeDetailActivity.this, 25);
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_SHOW_DRIVER_LICENSE && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();   //finish应该写到这个地方
        }
    }
}
