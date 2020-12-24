package com.web.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogInfo {
    /**
     * 需要记录日志的功能名称
     */
    String funcName() default "";

}