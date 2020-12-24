package com.web.common.security.permission.annotation;

import java.lang.annotation.*;

/**
 * 必需是已接入的客户端
 * @author
 * @version 1.0
 */
@Target(value = {
        ElementType.TYPE,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireLoginClient {}
