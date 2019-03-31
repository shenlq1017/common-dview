package com.sucsoft.jt.acjtdview.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * description 统一返回对象
 *
 * @author shenlq
 * @date 2018/12/26 18:22
 */
@ApiModel("统一返回对象")
public class Response<T> implements Serializable  {
    /**
     * 状态码,默认为成功
     */
    @ApiModelProperty("状态码,默认为成功200")
    private int code = HttpStatus.OK.value();

    /**
     * 消息传递
     */
    @ApiModelProperty("消息传递")
    private String msg;

    /**
     * 返回的消息内容
     */
    @ApiModelProperty("返回的消息内容")
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 提供默认的8种构造函数，适应所有初始化结果
     */

    public Response() {
    }

    public Response(int code) {
        this.code = code;
    }

    public Response(String msg) {
        this.msg = msg;
    }

    public Response(T data) {
        this.data = data;
    }

    public Response(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * -------------------------常用
     */

    /**
     * 返回成功 响应,200
     */
    public static Response ok() {
        return builder().code(HttpStatus.OK.value()).build();
    }

    /**
     * 返回一个失败的请求 响应，400
     * @return
     */
    public static Response badReq() {
        return builder().code(HttpStatus.BAD_REQUEST.value()).build();
    }

    /**
     * 返回一个失败的后台 响应，500
     */
    public static Response error() {
        return builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }

    /**
     * 使用链式结构方便赋值
     * @return
     */
    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public static class ResponseBuilder<T>{
        private int code = HttpStatus.OK.value();
        private String msg;
        private T data;


        public ResponseBuilder code(int code) {
            this.code = code;
            return this;
        }

        public ResponseBuilder age(String msg) {
            this.msg = msg;
            return this;
        }

        public ResponseBuilder data(T data) {
            this.data = data;
            return this;
        }

        public Response build() {
            Response response = new Response();
            response.setCode(code);
            response.setMsg(msg);
            response.setData(data);
            return response;
        }
    }


}
