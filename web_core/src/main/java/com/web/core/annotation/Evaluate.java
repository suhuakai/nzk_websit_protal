package com.web.core.annotation;

import java.lang.annotation.*;

/**
 * 性能评估使用
 *
 * @author
 * @version	1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Evaluate {

	String value() default "";
	
}
