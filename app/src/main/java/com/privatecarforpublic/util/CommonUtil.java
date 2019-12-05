package com.privatecarforpublic.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

public class CommonUtil {
    public static final int LOAD_SUCCESS = 1;
    public static final int LOAD_FAILED = 2;
    public static final int LOADING = 3;
    public static final int LOAD_WITHOUT_ANIM_SUCCESS = 4;
    public static final int LOAD_WITHOUT_ANIM_FAILED = 5;
    public static final int SAVE_YOU = 6;

    static public void showMessage(final Activity activity,final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    static public void processLoading(LoadingDialog ld,int what){
        LoadingHandler h=new LoadingHandler(ld);
        //h.sendEmptyMessage(what);
        h.sendEmptyMessageDelayed(what,2000);
    }

    static public class LoadingHandler extends Handler {
        LoadingDialog ld;
        Activity activity;
        public LoadingHandler(LoadingDialog ld){
            this.ld=ld;
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_SUCCESS:
                    ld.loadSuccess();
                    break;
                case LOAD_FAILED:
                    ld.loadFailed();
                    break;
                case LOADING:
                    ld.show();
                    break;
                case LOAD_WITHOUT_ANIM_FAILED:
                    ld.loadFailed();
                    break;
                case LOAD_WITHOUT_ANIM_SUCCESS:
                    ld.loadSuccess();
                    break;
                case SAVE_YOU:
                    ld.close();
                    break;
            }
        }
    }
}
