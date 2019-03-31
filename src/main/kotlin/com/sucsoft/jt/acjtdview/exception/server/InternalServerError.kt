package com.sucsoft.jt.acjtdview.exception.server

import org.springframework.http.HttpStatus

open class InternalServerError : RuntimeException {
    constructor() {}

    constructor(message: String) : super(message) {}

    constructor(cause: Throwable) : super(cause) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(message: String, cause: Throwable, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace) {}

    open fun code(): Int {
        return HttpStatus.INTERNAL_SERVER_ERROR.value()
    }
}