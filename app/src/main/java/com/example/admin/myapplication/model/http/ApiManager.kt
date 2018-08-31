package com.example.admin.myapplication.model.http

import com.example.admin.myapplication.Utils.Utils
import com.example.admin.myapplication.base.App
import com.example.admin.myapplication.log.okHttpLog.HttpLoggingInterceptorM
import com.example.admin.myapplication.log.okHttpLog.LogInterceptor
import com.example.admin.myapplication.model.bean.LoginBean
import com.example.admin.myapplication.model.bean.MyHttpResponse
import com.google.gson.GsonBuilder
import io.reactivex.Flowable
import okhttp3.*
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * Created by admin on 2018/8/3.
 */
object ApiManager {
    //正式
    const val HOST_PRO: String = "http://mgapi.huaxick.com/"
    //测试
    const val HOST_DEV: String = "http://dev.mgapi.huaxick.com/"
    //预发布
    public val HOST_PRE: String = "http://pre.mgapi.huaxick.com/"

    val HTTP_LOG_TAG: String = "http"
    var HOST = ""

    private lateinit var mApiService: ApiService

    init {
        val retrofit = initRetrofit()
        initServices(retrofit)
    }

    fun setApiHost() {
        val retrofit = initRetrofit()
        initServices(retrofit)
    }


    private fun initRetrofit(): Retrofit {
        var builder = OkHttpClient.Builder()
        //打印日志 不区分是否是debug模式
        val interceptor = HttpLoggingInterceptorM(LogInterceptor(HTTP_LOG_TAG))
        interceptor.level = HttpLoggingInterceptorM.Level.BODY
        builder.addInterceptor(interceptor)
//        测试数据
//        builder.addInterceptor(FakeApiInterceptor())

        val cachePath = Utils.getOKHttpCachePath()
        var cacheFile = File(cachePath)
        val cache = Cache(cacheFile, (1024 * 1024 * 50).toLong())
        val cacheInterceptor = Interceptor { chain ->
            var request = chain.request()
            if (!Utils.isNetworkConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }
            val response = chain.proceed(request)
            if (Utils.isNetworkConnected()) {
                val maxAge = 0
                // 有网络时, 不缓存, 最大保存时长为0
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build()
            } else {
                // 无网络时，设置超时为4周
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build()
            }
        }
//        val apikey = Interceptor { chain ->
//            var request = chain.request()
//            request = request.newBuilder()
//                    .addHeader("apikey", "header")
//                    .build()
//            chain.proceed(request)
//        }
//        //设置统一的请求头部参数
//        builder.addInterceptor(apikey)
        builder.addNetworkInterceptor(cacheInterceptor)
        builder.addInterceptor(cacheInterceptor)
        //设置缓存
        builder.cache(cache)
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
        //错误重连
        builder.retryOnConnectionFailure(true)
        //不设置代理
        builder.proxy(Proxy.NO_PROXY)
        var okHttpClient = builder.build()



        when (App.instance.mCurrentHost) {
            App.HOST.DEV -> HOST = HOST_DEV
            App.HOST.PRE -> HOST = HOST_PRE
            App.HOST.PRO -> HOST = HOST_PRO
        }

        return Retrofit.Builder().baseUrl(HOST)
                .client(okHttpClient)
                .addConverterFactory(createGsonConverter())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun initServices(retrofit: Retrofit) {
        mApiService = retrofit.create(ApiService::class.java)
    }

    fun getApiService() = mApiService
    private fun createGsonConverter(): Converter.Factory {
        val builder = GsonBuilder().serializeNulls()
        return CheckGsonConverterFactory.create()
    }

    //登录
    fun login(body:RequestBody):Flowable<MyHttpResponse<LoginBean>> = mApiService.login(body)

}