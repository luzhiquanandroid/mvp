package com.example.admin.myapplication.presenter.impl

import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.example.admin.myapplication.Utils.*
import com.example.admin.myapplication.base.App
import com.example.admin.myapplication.base.RxPresenter
import com.example.admin.myapplication.model.bean.LoginBean
import com.example.admin.myapplication.model.bean.MyHttpResponse
import com.example.admin.myapplication.model.http.ApiManager
import com.example.admin.myapplication.model.http.ApiSettings
import com.example.admin.myapplication.model.http.CommonSubscriber
import com.example.admin.myapplication.presenter.contract.LoginContract
import com.tencent.bugly.crashreport.CrashReport
import org.json.JSONObject

/**
 * Created by admin on 2018/8/3.
 */
class LoginPresenter : RxPresenter<LoginContract.View>(), LoginContract.LoginPresenter {
    override fun login(phone: String, pwd: String, loginType: String, code: String) {
        val jsonObject = JSONObject()
        jsonObject.put("mobile", phone)
        jsonObject.put("password", pwd)
        jsonObject.put("login_type", loginType)
        var jpushId = UserUtil.getUserJPushId(App.instance)
        if (TextUtils.isEmpty(jpushId)) {
            jpushId = JPushInterface.getRegistrationID(App.instance)
        }
        jsonObject.put("push_id", jpushId)
        jsonObject.put("code", code)

        val channel_id = Utils.getChannelId()
        jsonObject.put("channel_id", channel_id)

        EncryptUtil.encryptPassword(jsonObject)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addDisposable(ApiManager.login(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<LoginBean>>())
                .compose(RxUtil.handleResult<LoginBean>())
                .subscribeWith(object : CommonSubscriber<LoginBean>(mView, true, ApiSettings.LOGIN) {
                    override fun onNext(t: LoginBean) {
                        UserUtil.saveUserJPushId(App.instance, jpushId)
                        CrashReport.setUserId(t.customer_id)
                        mView?.onLoginResult(t)
                    }
                }))
    }

}