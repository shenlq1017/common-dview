package com.sucsoft.jt.acjtdview.exception.client

import org.springframework.http.HttpStatus

class Forbidden : BadRequest {
    constructor() {}

    constructor(message: String) : super(message) {}

    constructor(cause: Throwable) : super(cause) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(message: String, cause: Throwable, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace) {}

    override fun code(): Int {
        return HttpStatus.FORBIDDEN.value()
    }
}