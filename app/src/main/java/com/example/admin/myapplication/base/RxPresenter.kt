package com.example.admin.myapplication.base

import com.example.admin.myapplication.Utils.RequsetUtil
import com.example.admin.myapplication.Utils.RxUtil
import com.example.admin.myapplication.Utils.SignUtils
import com.example.admin.myapplication.model.bean.MyHttpResponse
import com.example.admin.myapplication.model.http.ApiManager
import com.example.admin.myapplication.model.http.ApiSettings
import com.example.admin.myapplication.model.http.CommonSubscriber
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONObject

/**
 * Created by admin on 2018/8/3.
 */
open class RxPresenter<T : BaseView> : BasePresenter<T> {

    var mView: T? = null
    var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: T) {
        mView = view
    }

    override fun detachView() {
        this.mView = null
        unDisposable()
    }

    fun addDisposable(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }

    fun unDisposable() {
        mCompositeDisposable?.clear()
    }

    override fun buriedPoint(type: String) {
        val jsonObject = JSONObject()
        jsonObject.put("type", type)

        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addDisposable(ApiManager.getApiService().buriedPoint(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult())
                .subscribeWith(object : CommonSubscriber<Any>(mView, false, ApiSettings.BURIED_POINT) {
                    override fun onNext(t: Any) {
                    }
                }))
    }
}