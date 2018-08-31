package com.example.admin.myapplication.base

/**
 * Created by admin on 2018/8/2.
 */
interface BasePresenter<in T : BaseView> {
    fun attachView(view: T)

    fun detachView()

    fun buriedPoint(type: String)
}