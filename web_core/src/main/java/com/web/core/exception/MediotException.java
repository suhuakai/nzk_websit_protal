package com.web.core.exception;

import com.web.core.util.LocalStringUtils;

/**
 * 基本异常类型
 */
public class MediotException extends RuntimeException implements Codable {

    /**
     * 默认错误码
     */
    public final static int DEFAULT_ERROR_CODE = 500;

    /**
     * 错误码
     */
    private Integer code = DEFAULT_ERROR_CODE;

    /**
     * 附带信息
     */
    private Object attachment;
    public MediotException(Integer code, Object... args) {
        super(LocalStringUtils.getMessageByCode(code, args));
        this.code = (code == null ? DEFAULT_ERROR_CODE : code);
    }

    public MediotException(Codable codable, Object... args) {
        super(LocalStringUtils.fomart(codable.getMessage(), args));
        this.code = codable.getCode();
    }

    public MediotException(String message) {
        super(message);
    }

    public MediotException(Throwable cause) {
        super(cause);
    }

    public MediotException(Integer code, Throwable cause) {
        super(cause);
        this.code = (code == null ? DEFAULT_ERROR_CODE : code);
    }

    public MediotException(String message, Throwable cause) {
        super(message, cause);
    }

    public MediotException setCode(Integer code) {
        this.code = code;
        return this;
    }

    public MediotException setAttachment(Object attachment) {
        this.attachment = attachment;
        return this;
    }

    public Object getAttachment() {
        return attachment;
    }

    @Override
    public Integer getCode() {
        return code;
    }

}
