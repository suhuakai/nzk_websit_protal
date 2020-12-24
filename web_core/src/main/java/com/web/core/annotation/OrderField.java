package com.web.core.annotation;

import java.lang.annotation.*;

/**
 * 列表查询与分页查询使用，标注字段是否参与排序
 * */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderField {
	
	/**
	 * 排序先后顺序
	 * 如多字段排序时，请务必明确顺序，指定order需大于0
	 * 如未明确顺序可能拿不到预期的结果
	 * */
	int order() default 0;
	
	/**
	 * 备选排序字段
	 * 如此注解标注字段在数据库中为空，就会使用备选字段进行排序(此字段必须在当前实体中存在，不存在忽略备选字段)
	 * */
	String spare() default "";
	
}
