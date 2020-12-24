package com.web.core.converter;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTransfer extends PropertyEditorSupport {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.hasText(text)) {
            setValue(null);
        } else {
            try {
                setValue(LocalDate.parse(text, formatter));
            } catch (Exception ex) {
                throw new IllegalArgumentException("LocalDate.class - 数据类型绑定异常: " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public String getAsText() {
        LocalDate value = (LocalDate) getValue();
        return (value != null ? value.format(formatter) : "");
    }

}
