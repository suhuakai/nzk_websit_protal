package com.web.common.security.permission.annotation;

import java.lang.annotation.*;

/**
 * 必须拥有指定访问令牌
 * @author
 * @version 1.0
 */
@Target(value = {
        ElementType.TYPE,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireToken {}
