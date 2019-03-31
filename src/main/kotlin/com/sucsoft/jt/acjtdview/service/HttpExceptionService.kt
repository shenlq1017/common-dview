package com.sucsoft.jt.acjtdview.service

import com.sucsoft.jt.acjtdview.exception.server.QueryError
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

import java.util.ArrayList
import java.util.HashMap

/**
 * description http相关处理类
 *
 * @author shenlq
 * @date 2018/12/27 17:51
 */
@Service
class HttpExceptionService {

    fun allExceptions(): List<MyHttpStatus> {
        val result = ArrayList<MyHttpStatus>()

        for (httpStatus in HttpStatus.values()) {
            val status = HashMap<String, Any>()
            val myHttpStatus = MyHttpStatus(httpStatus.value(), httpStatus.reasonPhrase)
            result.add(myHttpStatus)
        }
        throw QueryError("sss")
        //        return result;
    }

    @ApiModel
    inner class MyHttpStatus {
        @ApiModelProperty("状态码")
        var code: Int? = null

        @ApiModelProperty("状态消息")
        var msg: String? = null

        constructor() {}

        constructor(code: Int?, msg: String) {
            this.code = code
            this.msg = msg
        }
    }


}
