package com.example.admin.myapplication.model.http

import com.example.admin.myapplication.model.bean.LoginBean
import com.example.admin.myapplication.model.bean.MyHttpResponse
import io.reactivex.Flowable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by admin on 2018/8/3.
 */
interface ApiService {
    @POST(ApiSettings.BURIED_POINT)
    fun buriedPoint(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.LOGIN)
    fun login(@Body body: RequestBody):Flowable<MyHttpResponse<LoginBean>>
}