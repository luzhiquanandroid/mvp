package com.example.admin.myapplication.Utils

import com.example.admin.myapplication.base.App
import com.example.admin.myapplication.log.LogUtil
import com.example.admin.myapplication.model.bean.MyHttpResponse
import com.example.admin.myapplication.model.http.ApiException
import com.tencent.bugly.crashreport.CrashReport
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.getStackTraceString

/**
 * Created by admin on 2018/8/3.
 */
object RxUtil {

    private val ERROR_LOG_TAG: String = "http"

    /**
     * 统一线程处理
     * @param <T>
     * @return
    </T> */
    fun <T> rxSchedulerHelper(): FlowableTransformer<T, T> {    //compose简化线程
        return FlowableTransformer<T, T> {
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 处理返回结果
     */
    fun <T> handleResult(): FlowableTransformer<MyHttpResponse<T>, T> {
        return FlowableTransformer<MyHttpResponse<T>, T> {
            it.flatMap {
                createData(it.data)
            }
        }

    }

    /**
     * 生成Flowable
     * @param <T>
     * @return
    </T> */
    fun <T> createData(t: T): Flowable<T> {
        return Flowable.create({ emitter ->
            try {
                if (t != null) {
                    emitter.onNext(t)
                } else {
                    ToastUtil.showToast(App.instance, "服务器数据错误")
                    CrashReport.postCatchedException(ApiException())
                }
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
                ToastUtil.showToast(App.instance, "服务器数据异常")
                CrashReport.postCatchedException(ApiException())
                LogUtil.d(ERROR_LOG_TAG, e.getStackTraceString())
                LoadingDialogUtil.dismissProgressDialog()
            }
        }, BackpressureStrategy.BUFFER)
    }

}