package com.example.admin.myapplication.model.http

import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import com.example.admin.myapplication.Utils.LoadingDialogUtil
import com.example.admin.myapplication.Utils.UserUtil
import com.example.admin.myapplication.base.App
import com.example.admin.myapplication.base.BaseView
import com.example.admin.myapplication.event.TokenErrorEvent
import com.example.admin.myapplication.log.LogUtil
import com.example.admin.myapplication.ui.activity.LoginActivity
import com.tencent.bugly.crashreport.BuglyLog
import com.tencent.bugly.crashreport.CrashReport
import io.reactivex.subscribers.ResourceSubscriber
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.getStackTraceString
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by admin on 2018/8/3.
 */
abstract class CommonSubscriber<T>(view: BaseView?, isShowLoading: Boolean = true, url: String = "") : ResourceSubscriber<T>() {

    private var mErrorMsg: String = ""
    private var mView: BaseView? = view
    private var mUrl = url    //请求的url
    private var mIsShowLoading: Boolean = isShowLoading

    override fun onStart() {
        super.onStart()
        if (mIsShowLoading && mView != null) {
            LoadingDialogUtil.showProgressDialog(App.instance.getCurrentActivity(), "加载中...", false)
        }
    }

    override fun onComplete() {
        if (mIsShowLoading) {
            LoadingDialogUtil.dismissProgressDialog()
            mIsShowLoading = false
        }
    }

    override fun onError(e: Throwable?) {

        if (mView == null) {
            return
        }
        var responseData = ""
        var errorTip = ""

        if (!TextUtils.isEmpty(mErrorMsg)) {
            errorTip = mErrorMsg
            mView?.showMsgError(mErrorMsg)
        } else if (e is ApiException) {

            try {
                responseData = e.getResponseData()
                val json = JSONObject(responseData)
                errorTip = json.optString("msg")
                val code = json.optInt("code")
                when (code) {
                    ApiCode.TOKEN_ERROR -> {
                        errorTip = ""
                        UserUtil.clearUser(App.instance)
                        EventBus.getDefault().post(TokenErrorEvent())
                        val intent = Intent(App.instance, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        App.instance.startActivity(intent)
                    }
                    ApiCode.UPDATE -> {
                        val data = json.getJSONObject("data")
                        val download_url = data.getString("download_url")
                        val introduction = data.getString("introduction")
                        //DialogUtil.showUpdateDialog(download_url, introduction)
                        errorTip = ""
                    }
                    ApiCode.ERROR -> {
                        val data = json.getJSONObject("data")
                        val image = data.optString("system_maint_img")
                        //EventBus.getDefault().post(ServiceErrorEvent(mErrorMsg))
                       //DialogUtil.showServiceErrorDialog(mErrorMsg, image)
                    }
                }
            } catch (e: Exception) {
                errorTip = "服务器数据错误"
                e.printStackTrace()
            }

            mView?.showMsgError(e.toString())
        } else if (e is HttpException || e is ConnectException || e is UnknownHostException || e is SocketTimeoutException || e is NoRouteToHostException) {
            e.printStackTrace()
            errorTip = "网络不给力"
            mView?.showMsgError(errorTip)

        } else {
            errorTip = "未知错误ヽ(≧Д≦)ノ"
            LogUtil.d(ApiManager.HTTP_LOG_TAG, "error =" + e?.getStackTraceString())
            mView?.showMsgError(errorTip)
        }

        //上报服务器错误日志
        val userId = UserUtil.getUserId(App.instance)
        BuglyLog.d("http", "$errorTip-$mUrl-$userId-$responseData")
        CrashReport.postCatchedException(ApiException())

//        when (mUrl) {
//            ApiSettings.OCR_IDCARD -> {
//                EventBus.getDefault().postSticky(IDCardErrorEvent())
//            }
//            ApiSettings.SAVE_IDNUM_INFO -> {
//                EventBus.getDefault().postSticky(IDCardErrorEvent())
//            }
//            ApiSettings.CHECK_FACE -> {
//                EventBus.getDefault().postSticky(FaceErrorEvent())
//            }
//        }


        if (!TextUtils.isEmpty(errorTip)) {
            Toast.makeText(App.instance, errorTip, Toast.LENGTH_LONG).show()
        }

        if (mIsShowLoading) {
            LoadingDialogUtil.dismissProgressDialog()
            mIsShowLoading = false
        }
        mView?.showError()

    }
}