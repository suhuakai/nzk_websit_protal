package com.web.common.security.permission.annotation;

import java.lang.annotation.*;

/**
 * 必须是指定机构类型的用户
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Target(value = {
        ElementType.TYPE,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireOrgType {
    /**
     * 机构类型
     */
    int[] value() default {};
}
