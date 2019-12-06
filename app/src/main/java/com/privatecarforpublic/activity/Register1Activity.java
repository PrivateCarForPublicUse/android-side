package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.privatecarforpublic.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user1);
        ButterKnife.bind(this);
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
}
