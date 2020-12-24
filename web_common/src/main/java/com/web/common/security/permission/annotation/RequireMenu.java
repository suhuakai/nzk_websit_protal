package com.web.common.security.permission.annotation;

import java.lang.annotation.*;

/**
 * 必需拥有指定菜单权限
 * @author
 * @version 1.0
 */
@Target(value = {
        ElementType.TYPE,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireMenu {
    /**
     * 菜单编码
     */
    String[] value() default {};
}
