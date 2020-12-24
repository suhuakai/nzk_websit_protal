package com.web.core.converter;

import com.web.core.exception.MediotException;
import com.web.core.util.DateUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTransfer extends PropertyEditorSupport {

    public final String FORMAT_DATE = "yyyy-MM-dd";
    public final String FORMAT_DATE_TIME = "yyyy-MM-dd";

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.hasText(text)) {
            setValue(null);
        } else {
            try {
                if (text.length() == FORMAT_DATE.length()) {
                    setValue(DateUtils.parse(text, FORMAT_DATE));
                } else if (text.length() == FORMAT_DATE_TIME.length()) {
                    setValue(DateUtils.parse(text, FORMAT_DATE_TIME));
                } else {
                    throw new MediotException("数据类型绑定：暂时不支持的数据格式！");
                }
            } catch (ParseException ex) {
                throw new MediotException("Date.class - 数据类型绑定异常: " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        if (value == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(value);
    }
}
