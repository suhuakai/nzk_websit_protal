package com.web.core.util;

import com.web.core.entity.ResResult;
import com.web.core.exception.Codable;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

public class RestHelper {

    /**
     * 请求处理成功
     * @param <T>
     * @return
     */
    public static <T> ResResult<T> ok() {
        return ok(null);
    }

    /**
     * 请求处理成功
     * @param <T>
     * @return
     */
    public static <T> ResResult<T> ok(T data) {
        return new ResResult<T>()
                .code(HttpStatus.OK.value())
                .data(data)
                .putTimeStamp();
    }

    /**
     * 请求处理失败（默认500错误）
     * @param message 调用结果消息
     * @param <T>
     * @return
     */
    public static <T> ResResult<T> error(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    /**
     * 请求处理失败
     * @param code 状态码
     * @param message 调用结果消息
     * @param <T>
     * @return
     */
    public static <T> ResResult<T> error(Integer code, String message) {
        return error(null, code, message);
    }

    /**
     * 请求处理失败
     * @param code 状态码
     * @param message 调用结果消息
     * @param <T>
     * @return
     */
    public static <T> ResResult<T> error(Integer code, String message,T data) {
        return error(null, code, message,data);
    }

    /**
     * 业务处理失败（默认500错误）
     * @param code 业务代码
     * @param message 调用结果消息
     * @param <T>
     * @return
     */
    public static <T> ResResult<T> bizError(Integer code, String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), code, message);
    }

    /**
     * 业务处理失败（默认500错误）
     * @param
     * @param <T>
     * @return
     */
    public static <T> ResResult<T> bizError(Codable codable) {
        return bizError(codable.getCode(), codable.getMessage());
    }

    /**
     * 请求处理失败
     * @param status 状态码
     * @param code 业务代码
     * @param message 调用结果消息
     * @param <T>
     * @return
     */
    public static <T> ResResult<T> error() {
        return new ResResult<T>()
                .code(HttpStatus.EXPECTATION_FAILED.value())
                .putTimeStamp();
    }


    /**
     * 请求处理失败
     * @param status 状态码
     * @param code 业务代码
     * @param message 调用结果消息
     * @param <T>
     * @return
     */
    public static <T> ResResult<T> error(Integer status, Integer code, String message) {
        return new ResResult<T>()
                .code(code)
                .message(message)
                .putTimeStamp();
    }

    /**
     * 请求处理失败
     * @param status 状态码
     * @param code 业务代码
     * @param message 调用结果消息
     * @param <T>
     * @return
     */
    public static <T> ResResult<T> error(Integer status, Integer code, String message,T data) {
        return new ResResult<T>()
                .data(data)
                .code(code)
                .message(message)
                .putTimeStamp();
    }

    /**
     * 判断是否是ajax请求
     */
    public static boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
    }

}
