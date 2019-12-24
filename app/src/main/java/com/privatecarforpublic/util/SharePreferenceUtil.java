package com.privatecarforpublic.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        // 和Map<key, value>一样保存数据，取数据也是一样简单
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        // 和Map<key, value>一样获取数据，存数据也是一样简单
        String value = sp.getString(key, defValue);
        return value;
    }

    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        // 和Map<key, value>一样获取数据，存数据也是一样简单
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
    }
}
