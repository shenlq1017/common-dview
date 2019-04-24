package com.sucsoft.jt.acjtdview.config;

import com.sucsoft.jt.acjtdview.bean.UserApiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * description swagger初始化工作
 *
 * @author shenlq
 * @date 2018/12/27 18:10
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "jt.swagger.enabled", havingValue = "true")
public class SwaggerConfig {
    /**
     * 哪些包需要scan
     */
    @Value("${jt.swagger.apiPackages:}")
    String[] basePackages;
    /**
     * 哪些类需要scan
     */
    @Value("${jt.swagger.class.annotations:io.swagger.annotations.Api}")
    Class[] classAnnotations;
    /**
     * 哪些方法需要scan
     */
    @Value("${jt.swagger.method.annotations:io.swagger.annotations.ApiOperation}")
    Class[] methodAnnotations;

    /**
     * 是否全部加载
     */
    @Value("${jt.swagger.apisAny:false}")
    boolean apisAny;

    /**
     * 是否全部不加载
     */
    @Value("${jt.swagger.apisNone:false}")
    boolean apisNone;
    /**
     * 正则
     */
    @Value("${jt.swagger.pathRegex:}")
    String pathRegex;
    /**
     * 通配符
     */
    @Value("${jt.swagger.pathPattern:}")
    String pathPattern;
    /**
     * 是否加载所有
     */
    @Value("${jt.swagger.pathAny:false}")
    boolean pathAny;
    /**
     * 是否都不加载
     */
    @Value("${jt.swagger.pathNone:false}")
    boolean pathNone;

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        ApiSelectorBuilder apiBuilder = docket.select();
        if (basePackages!=null && !StringUtils.isEmpty(basePackages)) {
            for (String basePackage : basePackages) {
                apiBuilder.apis(RequestHandlerSelectors.basePackage(basePackage));
            }
        }
        /**
         * 注解类
         */
        if (classAnnotations != null && classAnnotations.length > 0) {
            for (Class classAnnotation : classAnnotations) {
                apiBuilder.apis(RequestHandlerSelectors.withClassAnnotation(classAnnotation));
            }
        }
        /**
         * 注解方法
         */
        if (methodAnnotations != null && methodAnnotations.length > 0) {
            for (Class methodAnnotation : methodAnnotations) {
                apiBuilder.apis(RequestHandlerSelectors.withMethodAnnotation(methodAnnotation));
            }
        }
        /**
         * 所有
         */
        if (apisAny) {
            apiBuilder.apis(RequestHandlerSelectors.any());
        }
        /**
         * 一个不要
         */
        if (apisNone) {
            apiBuilder.apis(RequestHandlerSelectors.none());
        }
        /**
         * 正则匹配路径
         */
        if(!StringUtils.isEmpty(pathRegex)) {
            apiBuilder.paths(PathSelectors.regex(pathRegex));
        }
        /**
         * 通配匹配路径
         */
        if (!StringUtils.isEmpty(pathPattern)) {
            apiBuilder.paths(PathSelectors.ant(pathPattern));
        }
        /**
         * 所有路径
         */
        if (pathAny) {
            apiBuilder.paths(PathSelectors.any());
        }
        /**
         * 取消所有路径
         */
        if (pathNone) {
            apiBuilder.paths(PathSelectors.none());
        }
        return apiBuilder.build().apiInfo(apiInfo());
    }

    @Autowired
    UserApiInfo userApiInfo;

    /**
     * 基本信息
     * @return
     */
    private ApiInfo apiInfo() {
        boolean apiInfoFlag = false;
        ApiInfoBuilder apiInfo = new ApiInfoBuilder();
        if (userApiInfo.getTitle() != null) {
            apiInfo.title(userApiInfo.getTitle());
            apiInfoFlag = true;
        }
        if (userApiInfo.getDescription() != null) {
            apiInfo.description(userApiInfo.getDescription());
            apiInfoFlag = true;
        }
        if (userApiInfo.getLicense() != null) {
            apiInfo.license(userApiInfo.getLicense());
            apiInfoFlag = true;
        }
        if (userApiInfo.getLicenseUrl() != null) {
            apiInfo.licenseUrl(userApiInfo.getLicenseUrl());
            apiInfoFlag = true;
        }
        if (userApiInfo.getTermsOfServiceUrl() != null) {
            apiInfo.termsOfServiceUrl(userApiInfo.getTermsOfServiceUrl());
            apiInfoFlag = true;
        }
        if (userApiInfo.getVersion() != null) {
            apiInfo.version(userApiInfo.getVersion());
            apiInfoFlag = true;
        }
        if (!apiInfoFlag) {
            return ApiInfo.DEFAULT;
        }else {
            return apiInfo.build();
        }
    }
}
