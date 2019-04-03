package com.sucsoft.jt.acjtdview.bean

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.http.HttpStatus

import java.io.Serializable

/**
 * description 统一返回对象
 *
 * @author shenlq
 * @date 2018/12/26 18:22
 */
@ApiModel("统一返回对象")
class Response<T> : Serializable {
    /**
     * 状态码,默认为成功
     */
    @ApiModelProperty("状态码,默认为成功200")
    var code = HttpStatus.OK.value()

    /**
     * 消息传递
     */
    @ApiModelProperty("消息传递")
    var msg: String? = null

    /**
     * 返回的消息内容
     */
    @ApiModelProperty("返回的消息内容")
    var data: T? = null

    /**
     * 提供默认的8种构造函数，适应所有初始化结果
     */

    constructor() {}

    constructor(code: Int) {
        this.code = code
    }

    constructor(msg: String?) {
        this.msg = msg
    }

    constructor(data: T) {
        this.data = data
    }

    constructor(msg: String?, data: T) {
        this.msg = msg
        this.data = data
    }

    constructor(code: Int, msg: String?) {
        this.code = code
        this.msg = msg
    }

    constructor(code: Int, data: T) {
        this.code = code
        this.data = data
    }

    constructor(code: Int, msg: String?, data: T) {
        this.code = code
        this.msg = msg
        this.data = data
    }

    class ResponseBuilder<T> {
        private var code = HttpStatus.OK.value()
        private var msg: String? = null
        private var data: T? = null


        fun code(code: Int): ResponseBuilder<*> {
            this.code = code
            return this
        }

        fun msg(msg: String): ResponseBuilder<*> {
            this.msg = msg
            return this
        }

        fun data(data: T): ResponseBuilder<*> {
            this.data = data
            return this
        }

        fun build(): Response<*> {
            val response = Response<T>()
            response.code = code
            response.msg = msg
            response.data = data
            return response
        }
    }

}
