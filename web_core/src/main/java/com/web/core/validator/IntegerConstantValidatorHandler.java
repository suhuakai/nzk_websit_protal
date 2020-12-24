package com.web.core.validator;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 整数 常量检查处理器
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
public class IntegerConstantValidatorHandler implements ConstraintValidator<Constant, Integer> {

    private int[] constantValues;

    @Override
    public void initialize(Constant constraintAnnotation) {
        String[] values = constraintAnnotation.value();
        this.constantValues = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            if (StringUtils.isNotBlank(value)) {
                this.constantValues[i] = Integer.parseInt(value);
            }
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (constantValues == null || constantValues.length == 0) {
            return false;
        }
        for (int constantValue : constantValues) {
            if (constantValue == value) {
                return true;
            }
        }
        return false;
    }

}