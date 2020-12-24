package com.web.core.converter;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTransfer extends PropertyEditorSupport {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.hasText(text)) {
            setValue(null);
        } else {
            try {
                setValue(LocalDateTime.parse(text, formatter));
            } catch (Exception ex) {
                throw new IllegalArgumentException("LocalDateTime.class - 数据类型绑定异常: " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public String getAsText() {
        LocalDate value = (LocalDate) getValue();
        return (value != null ? value.format(formatter) : "");
    }

}
