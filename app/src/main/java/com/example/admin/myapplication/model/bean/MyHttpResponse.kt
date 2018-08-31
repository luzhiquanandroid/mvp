package com.example.admin.myapplication.model.bean

/**
 * Created by admin on 2018/8/3.
 */
data class MyHttpResponse<T>(val code: Int, val msg: String, val data: T)