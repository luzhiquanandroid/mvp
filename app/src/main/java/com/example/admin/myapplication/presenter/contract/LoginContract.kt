package com.example.admin.myapplication.presenter.contract

import com.example.admin.myapplication.base.BasePresenter
import com.example.admin.myapplication.base.BaseView
import com.example.admin.myapplication.model.bean.LoginBean

/**
 * Created by admin on 2018/8/3.
 */
object LoginContract {
    interface View : BaseView {
        fun onLoginResult(data:LoginBean)
    }

    interface LoginPresenter : BasePresenter<View> {
        fun login(phone: String, pwd: String, loginType: String, code: String)
    }
}