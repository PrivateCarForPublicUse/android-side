package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import com.privatecarforpublic.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user2);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.next)
    void next() {
        Intent intent = new Intent(Register2Activity.this, IdCardActivity.class);
        intent.putExtra("name", name.getText().toString());
        startActivity(intent);

    }
}
