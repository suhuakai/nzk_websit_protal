package com.web.core.validator;

import org.apache.commons.lang3.ArrayUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字符串常量检查处理器
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
public class StringConstantValidatorHandler implements ConstraintValidator<Constant, String> {

    private String[] constantValues;

    @Override
    public void initialize(Constant constraintAnnotation) {
        this.constantValues = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ArrayUtils.contains(constantValues, value);
    }

}