package com.web.core.exception;

/**
 *   同步服务器基本异常类
 * @author
 * @version 1.0
 */
public class WebException extends MediotException {

    /**起因*/
    private String cause;

    public WebException(Integer code, Object... args) {
        super(code, args);
    }

    public WebException(Codable codable, Object... args) {
        super(codable, args);
    }

    public WebException(String message) {
        super(message);
    }

    public WebException(Throwable cause) {
        super(cause);
    }

    public WebException(String message, String cause) {
        super(message);
        this.cause = cause;
    }

    public WebException(Integer code, Throwable cause) {
        super(code, cause);
    }

    public WebException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCauseException() {
        return cause;
    }

}
