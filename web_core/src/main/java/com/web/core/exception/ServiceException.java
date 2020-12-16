package com.web.core.exception;

/**
 * 服务层异常定义
 */
public class ServiceException extends MediotException {

    public ServiceException(Integer code, Object... args) {
        super(code, args);
    }

    public ServiceException(Codable codable, Object... args) {
        super(codable, args);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(Integer code, Throwable cause) {
        super(code, cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
