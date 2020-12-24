package com.web.core.adapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime反序列化
 * @author
 *  * @since JDK 1.8
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    DateTimeFormatter ymdformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 日期时间反序列化
     * @param parser
     * @param context
     * @throws IOException
     */
    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String text = parser.getText();
        if (parser.getText().length() > 10) {
            text = text.substring(0, 10);
        }
        return LocalDate.parse(text, ymdformatter);
    }

}
