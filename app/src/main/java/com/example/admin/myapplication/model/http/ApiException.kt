package com.example.admin.myapplication.model.http

/**
 * Created by admin on 2018/8/3.
 */
class ApiException() : RuntimeException() {

    private var mResponseData: String = ""

    constructor(responseData: String) : this() {
        this.mResponseData = responseData
    }

    fun getResponseData(): String = mResponseData

}