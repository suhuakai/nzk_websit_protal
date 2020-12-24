package com.web.common.utils.excel.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( FIELD)
@Constraint(validatedBy = {CannotHaveBlankValidator.class})

public @interface CannotHaveBlank {
    //默认错误消息
    String message() default "不能包含空格";

    //分组
    Class<?>[] groups() default {};

    //负载
    Class<? extends Payload>[] payload() default {};

    //指定多个时使用
    @Target(FIELD)
    @Retention(RUNTIME)
    @Documented
    @interface List {
        CannotHaveBlank[] value();
    }

}
