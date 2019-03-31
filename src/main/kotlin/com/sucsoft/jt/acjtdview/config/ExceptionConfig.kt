package com.sucsoft.jt.acjtdview.config

import com.cgs.sscf.commons.exceptionreg.ExceptionRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * description 异常类型
 *
 * @author shenlq
 * @date 2018/12/25 17:57
 */
@Configuration
class ExceptionConfig {
    @Bean
    fun exceptionRegistry(): ExceptionRegistry {
        return ExceptionRegistry()
    }
}
