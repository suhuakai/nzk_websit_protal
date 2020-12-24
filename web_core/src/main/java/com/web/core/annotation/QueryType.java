package com.web.core.annotation;

import java.lang.annotation.*;

/**
 * 列表查询，字段查询方式
 * */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryType {
	/** 查询方式 */
	String value() default "=";
	
}
