package com.example.admin.myapplication.ui.activity

import android.util.Log
import com.example.admin.myapplication.R
import com.example.admin.myapplication.base.BaseActivity
import com.example.admin.myapplication.model.bean.LoginBean
import com.example.admin.myapplication.model.http.ApiManager.login
import com.example.admin.myapplication.presenter.contract.LoginContract
import com.example.admin.myapplication.presenter.impl.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

/**
 * Created by admin on 2018/8/3.
 */
class LoginActivity : BaseActivity<LoginContract.View, LoginContract.LoginPresenter>(), LoginContract.View {
    override fun onLoginResult(data: LoginBean) {
        if (data == null) {
            return
        }
        Log.d("login_token", data.token)
        toast("登录成功")
    }

    override var mPresenter: LoginContract.LoginPresenter = LoginPresenter()

    override fun initData() {
    }

    override fun initView() {
        btn_login.setOnClickListener {
            loginByPwd()
        }
    }

    /**
     * 登录
     */
    private fun loginByPwd() {
        /**
         * 输入信息的验证
         */
        mPresenter.login(et_phone.text.toString().trim(), et_pwd.text.toString().trim(), "2", "")
    }

    override fun getLayout(): Int = R.layout.activity_login
    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError() {
    }

    override fun showMsgError(msg: String) {
    }

    override fun showEmpty() {
    }


}