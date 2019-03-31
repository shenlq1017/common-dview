package com.sucsoft.jt.acjtdview.exception.server


/**
 * description 参数检查异常,一般指的是前端传递的值引起的问题,sql前
 *
 * @author shenlq
 * @date 2018/12/27 17:34
 */
class CheckError : InternalServerError {
    constructor() : super() {}

    constructor(message: String) : super(message) {}

    constructor(cause: Throwable) : super(cause) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(message: String, cause: Throwable, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace) {}

    override fun code(): Int {
        return super.code()
    }
}
