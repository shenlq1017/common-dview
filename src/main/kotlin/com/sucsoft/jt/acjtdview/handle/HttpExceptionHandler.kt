package com.sucsoft.jt.acjtdview.handle

import com.cgs.sscf.commons.exception.*
import com.cgs.sscf.commons.exceptionreg.ExceptionRegistry
import com.cgs.sscf.commons.exceptionreg.ExceptionRegistry.CodeAndMessage
import com.cgs.sscf.commons.exceptionreg.ExceptionRegistry.LogLevel
import com.sucsoft.jt.acjtdview.bean.Response
import com.sucsoft.jt.acjtdview.exception.server.CheckError
import com.sucsoft.jt.acjtdview.exception.server.DataDealError
import com.sucsoft.jt.acjtdview.exception.server.QueryError
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletResponse
import java.io.IOException

@ControllerAdvice
class HttpExceptionHandler {

    @Autowired
    private val exceptionRegistry: ExceptionRegistry? = null

    @ExceptionHandler(value = [NotFound::class, Unauthorized::class, Forbidden::class])
    @ResponseBody
    fun handleDebug(e: ApplicationException): ResponseEntity<*> {
        val cm = CodeAndMessage(e.code(), e.message, LogLevel.DEBUG)
        logger.debug(e.message, e)
        return ResponseEntity(Response<String>(e.code(), e.message), HttpStatus.valueOf(e.code()))
    }

    @ExceptionHandler(value = [BadRequest::class])
    @ResponseBody
    @Throws(Exception::class)
    fun handleWarn(e: ApplicationException, response: HttpServletResponse): ResponseEntity<*> {
        val cm = CodeAndMessage(e.code(), e.message, LogLevel.WARN)
        logger.warn(e.message, e)
        return ResponseEntity(Response<String>(e.code(), e.message), HttpStatus.valueOf(e.code()))
    }

    @ExceptionHandler(value = [CheckError::class, DataDealError::class, QueryError::class, InternalServerError::class])
    @ResponseBody
    @Throws(Exception::class)
    private fun handleError(e: ApplicationException, response: HttpServletResponse): ResponseEntity<*> {
        val cm = CodeAndMessage(e.code(), e.message, LogLevel.ERROR)
        logger.error(e.message, e)
        return ResponseEntity(Response<String>(e.code(), e.message), HttpStatus.valueOf(e.code()))
    }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    @Throws(Exception::class)
    fun handleException(e: Exception): ResponseEntity<*> {

        var cm: CodeAndMessage? = exceptionRegistry!!.getExceptionCodeAndMessage(e)
        if (cm == null) {
            val msg = ("发生未知的错误，类型: " + e.javaClass + ", 信息: " + e.message
                    + ", 请检查系统运行日志获得进一步的信息。")
            cm = CodeAndMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, LogLevel.ERROR)
        }
        try {
            logException(cm, e)
        } finally {
            return ResponseEntity(Response<String>(cm.code, e.message), HttpStatus.valueOf(cm.code))
        }
    }

    private fun logException(cm: CodeAndMessage, e: Exception) {
        when (cm.logLevel) {
            ExceptionRegistry.LogLevel.DEBUG -> logger.debug(cm.message, e)
            ExceptionRegistry.LogLevel.ERROR -> logger.error(cm.message, e)
            ExceptionRegistry.LogLevel.INFO -> logger.info(cm.message, e)
            ExceptionRegistry.LogLevel.TRACE -> logger.trace(cm.message, e)
            ExceptionRegistry.LogLevel.WARN -> logger.warn(cm.message, e)
            ExceptionRegistry.LogLevel.NONE -> {
            }
            else -> {
            }
        }
    }

    companion object {

        private val logger = LoggerFactory.getLogger(HttpExceptionHandler::class.java)
    }
}
