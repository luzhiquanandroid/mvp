package com.example.admin.myapplication.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import cn.jpush.android.api.JPushInterface
import com.example.admin.myapplication.R
import com.example.admin.myapplication.event.DummyEvent
import com.example.admin.myapplication.event.TokenErrorEvent
import com.umeng.analytics.MobclickAgent
import me.yokeyword.fragmentation.SupportActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * Created by admin on 2018/8/2.
 */
abstract class BaseActivity<in V : BaseView, T : BasePresenter<V>> : SupportActivity(), BaseView {
    protected val mActivity: Activity = this
    protected abstract var mPresenter: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        setStatusBar()
        App.instance.addActivity(mActivity)
        EventBus.getDefault().register(mActivity)
        mPresenter.attachView(this as V)
        initView()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        App.instance.removeActivity(mActivity)
        EventBus.getDefault().unregister(mActivity)
        mPresenter.detachView()
    }

    override fun onResume() {
        super.onResume()
        JPushInterface.onResume(App.instance)
        if (JPushInterface.isPushStopped(App.instance)) {
            JPushInterface.resumePush(App.instance)
        }

        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        JPushInterface.onPause(App.instance)
        MobclickAgent.onPause(this)
    }

    protected fun totoActivity(mContext: Context, toActivityClass: Class<*>, bundle: Bundle?) {
        val intent = Intent(mContext, toActivityClass)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        mContext.startActivity(intent)
        (mContext as Activity).overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out)
    }

    protected open fun backActivity() {
        finish()
        overridePendingTransition(R.anim.not_exit_push_left_int, R.anim.push_right_out)
    }

    protected abstract fun initData()

    protected abstract fun initView()


    /**
     * 设置状态栏
     */
    protected open fun setStatusBar() {

    }

    abstract fun getLayout(): Int
    /**
     * 该方法不执行，只是让Event编译通过
     */
    @Subscribe
    fun dummy(event: DummyEvent) {

    }

    @Subscribe
    fun onTokenErrorEvent(event: TokenErrorEvent) {
        //if ()
    }
}