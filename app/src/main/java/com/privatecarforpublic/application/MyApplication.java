package com.privatecarforpublic.application;


import android.app.Application;

import com.xiasuhuei321.loadingdialog.manager.StyleManager;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setLoadingDialog();
    }

    private void setLoadingDialog(){
        StyleManager s = new StyleManager();
        //在这里调用方法设置s的属性
        // code here...
        s.Anim(false).repeatTime(0).contentSize(-1).intercept(true);
        LoadingDialog.initStyle(s);
    }

}
