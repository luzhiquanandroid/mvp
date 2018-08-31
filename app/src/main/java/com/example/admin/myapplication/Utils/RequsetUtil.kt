package com.example.admin.myapplication.Utils

import com.example.admin.myapplication.base.App
import com.example.admin.myapplication.base.GlobalParams
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

/**
 * Created by admin on 2018/8/3.
 */
object RequsetUtil {

//    {
//        "token":"除部分接口调用时还没用token之外，都要上传",
//        "version":"1.0.0  当前app的版本号",
//        "type":"0:h5  1:android 2:ios 3:winphone",
//        "timestamp":"时间戳",
//        "customer_id": "用户ID"
//        "platform": "平台标识"
//        "haidai":海带白卡,"wawa":蛙蛙白卡,"dudu":嘟嘟白卡,默认为tsd"
//        "data":{
//        这里写真正接口数据
//    }
//    }

    fun getRequestBody(jsonObject: JSONObject): RequestBody {
        var rootJsonObject = JSONObject()
        val timestamp = System.currentTimeMillis().toString();//时间
        val version = Utils.getVersion(App.instance)//版本号
        val token = UserUtil.getToken(App.instance)//token
        val customer_id = UserUtil.getUserId(App.instance)//用户id

        rootJsonObject.put("token", token)
                .put("version", version)
                .put("type", "1")//"0:h5  1:android 2:ios 3:winphone",
                .put("timestamp", timestamp)
                .put("customer_id", customer_id)
                .put("platform", GlobalParams.PLATFORM_FLAG)
                .put("data", jsonObject)
        val toString = rootJsonObject.toString()
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), toString)
    }
}