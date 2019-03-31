package com.sucsoft.jt.acjtdview.exception.client

import org.springframework.http.HttpStatus

open class BadRequest : RuntimeException {
    constructor() {}

    constructor(message: String) : super(message) {}

    constructor(cause: Throwable) : super(cause) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(message: String, cause: Throwable, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace) {}

    open fun code(): Int {
        return HttpStatus.BAD_REQUEST.value()
    }
}