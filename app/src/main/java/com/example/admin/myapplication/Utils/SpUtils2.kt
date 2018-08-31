package com.example.admin.myapplication.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.admin.myapplication.base.App

/**
 * Created by admin on 2018/8/2.
 */
class SpUtils2(context: Context) {
    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var spUtils2: SpUtils2

        fun getInstance(context: Context): SpUtils2 {
            if (spUtils2 == null) {
                spUtils2 = SpUtils2(context)
            }
            return spUtils2
        }

    }

    init {
        sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun getString(key: String, defaultVal: String): String {
        return sharedPreferences.getString(key, defaultVal)
    }

    fun getBoolean(key: String, defaultVal: Boolean): Boolean? {
        return sharedPreferences.getBoolean(key, defaultVal)
    }

    fun putString(key: String, defaultVal: String) {
        val edit = sharedPreferences.edit()
        edit?.putString(key, defaultVal)
        edit?.commit()
    }

    fun putBoolean(key: String, defaultVal: Boolean) {
        val edit = sharedPreferences.edit()
        edit.putBoolean(key, defaultVal)
        edit.commit();
    }

    fun clearAllSp() {
        sharedPreferences.edit().clear().commit()
    }
}