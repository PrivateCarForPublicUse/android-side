package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.privatecarforpublic.R;
import com.privatecarforpublic.model.User;
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
import butterknife.OnItemClick;

public class EmployeeDetailActivity extends Activity {
    private static final String TAG = "EmployeeDetailActivity";
    public static final int TO_SHOW_DRIVER_LICENSE = 110;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.side)
    TextView side;
    @BindView(R.id.picture)
    ImageView picture;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.starLevel)
    TextView starLevel;
    @BindView(R.id.workNumber)
    TextView workNumber;
    @BindView(R.id.phoneNumber)
    TextView phoneNumber;
    @BindView(R.id.idCardNumber)
    TextView idCardNumber;
    @BindView(R.id.idCardDueDate)
    TextView idCardDueDate;
    @BindView(R.id.agree)
    Button agree;
    @BindView(R.id.reject)
    Button reject;

    private User user;
    private int type;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_detail);
        init();
        if (type == 0) {
            title.setText("员工信息");
            agree.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
        } else {
            title.setText("员工审核");
        }
        side.setVisibility(View.INVISIBLE);
        //状态栏颜色设置
        StatusBarUtil.setColor(EmployeeDetailActivity.this, 25);
    }

    private void init() {
        ButterKnife.bind(this);
        index = getIntent().getIntExtra("index", -1);
        type = getIntent().getIntExtra("type", -1);
        user = (User) getIntent().getSerializableExtra("user");
        username.setText(user.getUserName());
        //Picasso.get().load(user.getPicture()).into(picture);
        name.setText(user.getName());
        starLevel.setText(user.getStarLevel() + "");
        workNumber.setText(user.getWorkNumber() + "");
        phoneNumber.setText(user.getPhoneNumber());
        idCardDueDate.setText(user.getIdCardDueDate());
        idCardNumber.setText(user.getIdCardNumber());
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @OnClick(R.id.agree)
    void agree() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> params = new HashMap<>();
                    params.put("isAccept", 1);
                    params.put("userId", user.getId());
                    String token = SharePreferenceUtil.getString(EmployeeDetailActivity.this, "token", "");
                    ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, token, Constants.SERVICE_ROOT + "Master/audit-user-info", params);
                    if (responseResult.getCode() != 200) {
                        CommonUtil.showMessage(EmployeeDetailActivity.this, "审核失败");
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("index", index);
                    setResult(Activity.RESULT_OK, intent);
                    finish();   //finish应该写到这个地方
                } catch (Exception e) {
                    CommonUtil.showMessage(EmployeeDetailActivity.this, "审核出错");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick(R.id.reject)
    void reject() {
        final EditText inputServer = new EditText(EmployeeDetailActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeDetailActivity.this);
        builder.setTitle("拒绝理由").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String reason = inputServer.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Map<String, Object> params = new HashMap<>();
                            params.put("isAccept", -1);
                            params.put("userId", user.getId());
                            params.put("reason", reason);
                            String token = SharePreferenceUtil.getString(EmployeeDetailActivity.this, "token", "");
                            ResponseResult responseResult = JsonUtil.sendRequest(HttpRequestMethod.HttpPost, token, Constants.SERVICE_ROOT + "Master/audit-user-info", params);
                            if (responseResult.getCode() != 200) {
                                CommonUtil.showMessage(EmployeeDetailActivity.this, "审核失败");
                                return;
                            }
                            Intent intent = new Intent();
                            intent.putExtra("index", index);
                            setResult(Activity.RESULT_OK, intent);
                            finish();   //finish应该写到这个地方
                        } catch (Exception e) {
                            CommonUtil.showMessage(EmployeeDetailActivity.this, "审核出错");
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        builder.show();
    }
}
