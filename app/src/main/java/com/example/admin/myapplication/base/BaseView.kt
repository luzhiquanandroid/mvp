package com.example.admin.myapplication.base

/**
 * Created by admin on 2018/8/2.
 * view基类
 */
interface BaseView {
    /**
     * 显示加载框
     */
    fun showProgress()

    /**
     * 隐藏加载框
     */
    fun hideProgress()

    /**
     * 显示错误
     */
    fun showError()

    /**
     * 显示错误信息
     */
    fun showMsgError(msg: String)

    /**
     * 显示空数据页面
     */
    fun showEmpty()

}