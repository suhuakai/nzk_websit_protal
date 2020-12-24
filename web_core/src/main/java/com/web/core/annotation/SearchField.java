package com.web.core.annotation;

import java.lang.annotation.*;

/**
 * 搜索框，查询字段
 * */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchField {
	/** 查询方式 */
	String value() default "like";
	
}
