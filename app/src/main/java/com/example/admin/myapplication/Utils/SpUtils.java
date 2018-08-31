package com.example.admin.myapplication.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 2018/8/2.
 */

public class SpUtils {
    private static SpUtils2 spUtils;
    private static SharedPreferences sharedPreferences;

    public SpUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE);

    }

    public static synchronized SpUtils2 getInstance(Context context) {
        if (spUtils == null) {
            spUtils = new SpUtils2(context);
        }
        return spUtils;
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public String getString(String key, String defaultVal) {
        return sharedPreferences.getString(key, defaultVal);
    }

    public void putString(String key, String defaultVal) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, defaultVal);
        edit.commit();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, Boolean defaultVal) {
        return sharedPreferences.getBoolean(key, defaultVal);
    }

    public void putBoolean(String key, boolean defaultVal) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, defaultVal);
        edit.commit();
    }

    //根据key删除sp的数据
    public void remove(String key) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(key);
        edit.commit();
    }

    public void clear() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
    }
}