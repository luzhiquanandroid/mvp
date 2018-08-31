package com.example.admin.myapplication.Utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtil private constructor(context: Context) {

    init {
        mSharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun getString(key: String): String {
        return mSharedPreferences!!.getString(key, "")
    }

    fun getString(key: String, defaultValue: String): String? {
        return mSharedPreferences?.getString(key, defaultValue)
    }

    fun putString(key: String, value: String) {
        val edit = mSharedPreferences!!.edit()
        edit.putString(key, value)
        edit.commit()
    }


    fun getInt(key: String): Int {
        return mSharedPreferences!!.getInt(key, 0)
    }

    fun putInt(key: String, value: Int) {
        val edit = mSharedPreferences!!.edit()
        edit.putInt(key, value)
        edit.commit()
    }

    fun getLong(key: String): Long {
        return mSharedPreferences!!.getLong(key, 0)
    }

    fun putLong(key: String, value: Long) {
        val edit = mSharedPreferences!!.edit()
        edit.putLong(key, value)
        edit.commit()
    }

    fun getBoolean(key: String): Boolean? {
        return mSharedPreferences!!.getBoolean(key, false)
    }

    fun putBoolean(key: String, value: Boolean) {
        val edit = mSharedPreferences!!.edit()
        edit.putBoolean(key, value)
        edit.commit()
    }

    /**
     * 通过key进行删除
     *
     * @param key
     */
    fun remove(key: String) {
        val edit = mSharedPreferences!!.edit()
        edit.remove(key)
        edit.commit()
    }

    /*
    * 清楚所有数据*/
    fun clearSp() {
        mSharedPreferences!!.edit().clear().commit();
    }

    companion object {

        private var mSharedPreferencesUtil: SharedPreferencesUtil? = null
        private var mSharedPreferences: SharedPreferences? = null

        @Synchronized
        fun getInstance(context: Context): SharedPreferencesUtil {
            if (mSharedPreferencesUtil == null) {
                mSharedPreferencesUtil = SharedPreferencesUtil(context)
            }
            return mSharedPreferencesUtil as SharedPreferencesUtil
        }
    }
}
