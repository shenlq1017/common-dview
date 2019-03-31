package com.sucsoft.jt.acjtdview.exception.server

/**
 * description 数据库取完数据以后的数据处理异常
 *
 * @author shenlq
 * @date 2018/12/27 17:39
 */
class DataDealError : InternalServerError {
    constructor() : super() {}

    constructor(message: String) : super(message) {}

    constructor(cause: Throwable) : super(cause) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(message: String, cause: Throwable, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace) {}

    override fun code(): Int {
        return super.code()
    }
}
