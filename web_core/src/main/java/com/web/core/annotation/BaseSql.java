package com.web.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseSql {
	/** 表名 */
	String tableName() default "";
	/** resultMapName */
	String resultName() default "";
	/** 枚举类型 */
	enum OrderType { DESC, ASC }

	/** 排序方式 */
	OrderType type() default OrderType.DESC;
}
