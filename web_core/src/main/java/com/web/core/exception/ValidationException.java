package com.web.core.exception;

/**
 * 数据校验异常定义
 */
public class ValidationException extends MediotException {

    public ValidationException(Integer code, Object... args) {
        super(code, args);
    }

    public ValidationException(Codable codable, Object... args) {
        super(codable, args);
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(Integer code, Throwable cause) {
        super(code, cause);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
