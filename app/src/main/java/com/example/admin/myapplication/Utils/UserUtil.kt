package com.example.admin.myapplication.Utils

import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.example.admin.myapplication.base.App
import java.util.*

/**
 * Created by admin on 2018/8/3.
 */
object UserUtil {

    val TOKEN: String = "token"
    val DEVICE_ID: String = "device_id"
    val JPUSH_ID: String = "jpush_id"
    val CUSTOMER_ID: String = "customer_id"
    fun getDevice_id(): String {
        var uniquePsuedoID = SharedPreferencesUtil.getInstance(App.instance)!!.getString(DEVICE_ID, "")
        if (!TextUtils.isEmpty(uniquePsuedoID)) {
            return uniquePsuedoID.toString();
        }
        /**
         * Return pseudo unique ID
         * @return ID
         */
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        val m_szDevIDShort = "35" + Build.BOARD.length % 10 +
                Build.BRAND.length % 10 +
                Build.CPU_ABI.length % 10 +
                Build.DEVICE.length % 10 +
                Build.MANUFACTURER.length % 10 +
                Build.MODEL.length % 10 +
                Build.PRODUCT.length % 10

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        var serial: String? = null
        try {
            serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()
            // Go ahead and return the serial for api => 9
            uniquePsuedoID = UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (exception: Exception) {
            // String needs to be initialized
            serial = "serial" // some value
            uniquePsuedoID = UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        }
        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        uniquePsuedoID = uniquePsuedoID.toString().replace("-", "")
        saveDeviceId(App.instance, uniquePsuedoID)
        return uniquePsuedoID
    }


    fun saveToken(context: Context, token: String) {
        SharedPreferencesUtil.getInstance(context).putString(TOKEN, token)
    }

    fun getToken(context: Context): String {
        return SharedPreferencesUtil.getInstance(context).getString(TOKEN, "").toString()
    }

    fun saveDeviceId(context: Context, deviceId: String) {
        SharedPreferencesUtil.getInstance(context).putString(DEVICE_ID, deviceId)
    }

    /**
     * 得到设备ID
     */
    fun getUserId(context: Context): String =
            SharedPreferencesUtil.getInstance(context).getString(CUSTOMER_ID).toString()


    /**
     * 获取用户id
     */
    fun getUserJPushId(context: Context): String =
            SharedPreferencesUtil.getInstance(context).getString(JPUSH_ID)

    fun saveUserJPushId(context: Context, jpush_id: String) {
        SharedPreferencesUtil.getInstance(context).putString(JPUSH_ID, jpush_id)
    }

    /**
     * 清除所有的用户信息
     */
    fun clearUser(context: Context) {
        SharedPreferencesUtil.getInstance(context).clearSp()
    }

}