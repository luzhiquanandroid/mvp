package com.example.admin.myapplication.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.multidex.MultiDex
import cn.jpush.android.api.JPushInterface
import com.balsikandar.crashreporter.CrashReporter
import com.example.admin.myapplication.Utils.Utils
import com.example.admin.myapplication.log.LogUtil
import com.github.tamir7.contacts.Contact
import com.github.tamir7.contacts.Contacts
import com.liulishuo.filedownloader.FileDownloader
import com.meituan.android.walle.WalleChannelReader
import com.moxie.client.commom.GlobalParams
import com.moxie.client.manager.MoxieSDK
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import kotlin.properties.Delegates

/**
 * Created by admin on 2018/8/2.
 */
class App : Application(), Application.ActivityLifecycleCallbacks {

    private var resumeCount: Int = 0;//当前显示的activity个数
    private var allActivities: LinkedHashSet<Activity> = LinkedHashSet();//所有打开的activity

    var mCurrentHost = HOST.DEV;//开发  开发HTTP  预发布  上线

    enum class HOST {
        DEV, DEV_HTTPS, PRE, PRO
    }

    companion object {
        var instance: App by Delegates.notNull()
        var isOnResume: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //判断程序是否重新启动
        val applicationRepeat = Utils.isApplicationRepeat(this)
        if (applicationRepeat) {
            return
        }
        //魔蝎初始化
        MoxieSDK.init(this)
        initUMeng()

        JPushInterface.setDebugMode(false)
        JPushInterface.init(this)

        LogUtil.init(true)
        // dex突破65535的限制
        MultiDex.install(this)

        CrashReport.initCrashReport(this, "bdc7d2e891", false)
        CrashReport.setAppChannel(this, Utils.getChannelId())

        initSmartRefreshLayout()
        FileDownloader.setupOnApplicationOnCreate(this)
        //APP Crash后保存log日志 log日志在/Android/data/package-name/files/crashLog
        CrashReporter.initialize(this, Utils.getCrashLogPath())
        //联系人初始化环境
        Contacts.initialize(this)
    }

    /**
     * 保存Activity的引用
     */
    fun addActivity(activity: Activity) {
        allActivities.add(activity)
    }

    /**
     * 清除Activity的引用
     */
    fun removeActivity(activity: Activity) {
        allActivities.remove(activity)
    }

    /**
     * 得到当前栈顶的Activity
     */
    fun getCurrentActivity(): Activity {
        return allActivities.last()
    }

    /**
     * 退出App
     */
    fun exitApp() {
        synchronized(allActivities) {
            for (act in allActivities) {
                act.finish()
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }

    private fun initUMeng() {
        val channel = WalleChannelReader.getChannel(instance)
        UMConfigure.init(instance, com.example.admin.myapplication.base.GlobalParams.U_MENG_APPKEY, channel, UMConfigure.DEVICE_TYPE_PHONE, null)
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL)
        UMConfigure.setLogEnabled(true)
    }

    /**
     * 初始化下拉刷新控件
     */
    private fun initSmartRefreshLayout() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate)//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        isOnResume = true
        resumeCount++
    }

    override fun onActivityPaused(activity: Activity) {
        resumeCount--
        if (resumeCount == 0) {
            isOnResume = false
        }
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }


}
