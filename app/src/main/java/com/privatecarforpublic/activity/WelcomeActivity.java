package com.privatecarforpublic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.privatecarforpublic.R;
import com.privatecarforpublic.util.CommonUtil;
import com.privatecarforpublic.util.NetUtil;

public class WelcomeActivity extends Activity {

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        open();
    }

    private void open() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!NetUtil.isConnected(WelcomeActivity.this))
                    CommonUtil.showMessage(WelcomeActivity.this,"网络连接失败");
                else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
            }
        }).start();
    }
}
