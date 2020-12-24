package com.web.core.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDate序列化
 * @author
 * @version 1.0
 *  * @since JDK 1.8
 */
public class LocalDateSerializer extends JsonSerializer<LocalDate> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 日期时间序列化
     * @param value
     * @param generator
     * @param provider
     */
    @Override
    public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeString(value.format(formatter));
    }

}
