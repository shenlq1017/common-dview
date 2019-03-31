package com.sucsoft.jt.acjtdview.web

import com.sucsoft.jt.acjtdview.service.HttpExceptionService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * description 返回相关的一些基础类接口
 *
 * @author shenlq
 * @date 2018/12/27 17:50
 */
@RestController
@RequestMapping("/rest/response")
@Api(value = "异常类型", tags = ["异常类型"])
class ResponseController {

    @Autowired
    internal var httpExceptionService: HttpExceptionService? = null

    @GetMapping("/allExceptions")
    @ApiOperation(value = "获取所有异常类型")
    fun allExceptions(): List<HttpExceptionService.MyHttpStatus> {
        return httpExceptionService!!.allExceptions()
    }
}
