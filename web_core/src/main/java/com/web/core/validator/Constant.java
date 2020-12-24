package com.web.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 常量约束注解
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Documented
@Constraint(validatedBy = {
        StringConstantValidatorHandler.class,
        IntegerConstantValidatorHandler.class
})
@Target({
        METHOD,
        FIELD,
        ANNOTATION_TYPE,
        CONSTRUCTOR,
        PARAMETER
})
@Retention(RUNTIME)
public @interface Constant {

    String[] value();

    String message() default "{constraint.default.const.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}