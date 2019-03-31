package com.sucsoft.jt.acjtdview.handle

import com.sucsoft.jt.acjtdview.simulate.SimulateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * description 将所有非Response对象返回的转为统一对象
 *
 * @author shenlq
 * @date 2018/12/29 14:22
 */
@ControllerAdvice
class HttpAdvanceHandler : ResponseBodyAdvice<Any> {

    @Value("\${jt.response.request.paths:/rest}")
    internal var requestPathAdvices: Array<String>? = null

    @Autowired
    private val simulateService: SimulateService? = null

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(body: Any?,
                                 returnType: MethodParameter,
                                 selectedContentType: MediaType, selectedConverterType: Class<out HttpMessageConverter<*>>,
                                 request: ServerHttpRequest,
                                 response: ServerHttpResponse): Any? {
        var body = body

        val requestPath = request.uri.path
        //改成模拟数据
        if (simulateService!!.isInSimulate(requestPath)) {
            body = simulateService!!.getSimulateByUrl(requestPath)
        }

        for (requestPathAdvice in requestPathAdvices!!) {
            if (!requestPath.startsWith(requestPathAdvice)) {
                return body
            }
        }
        return body

    }
}
