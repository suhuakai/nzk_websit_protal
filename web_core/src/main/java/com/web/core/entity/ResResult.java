package com.web.core.entity;

import com.web.core.util.DateUtils;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Http响应模板
 *
 * @author
 * @version 1.0
 */
@Data
@ToString
public class ResResult<T> implements Serializable {
    /**
     * 业务代码，失败时返回具体错误码
     */
    private Integer code;

    /**
     * 成功时返回 null，失败时返回具体错误消息
     */
    private String message;

    /**
     * 成功时具体返回值，失败时为 null
     */
    private T data;

    /**
     * 时间戳
     */
    private String timestamp;

    public ResResult() {}

    public ResResult<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public ResResult<T> message(String message) {
        this.message = message;
        return this;
    }

    public ResResult<T> data(T data) {
        this.data = data;
        return this;
    }

    public ResResult<T> putTimeStamp() {
        this.timestamp = DateUtils.format(new Date());
        return this;
    }
}
