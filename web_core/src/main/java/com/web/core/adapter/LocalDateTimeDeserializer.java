package com.web.core.adapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime反序列化
 * @author
 *  * @since JDK 1.8
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    DateTimeFormatter ymdhmsformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期时间反序列化
     * @param parser
     * @param context
     * @throws IOException
     */
    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String text = parser.getText();
        if (parser.getText().length() == 10) {
            text += " 00:00:00";
        }
        return LocalDateTime.parse(text, ymdhmsformatter);
    }

}
