package com.web.core.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Jackson 时间格式转化
 */
public class DateTimeSerializer extends JsonSerializer<Date> {

	/**
	 * 自定义时间格式
	 * @param value
	 * @param gen
	 * @param serializers
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String formattedDate = formatter.format(value);  
         gen.writeString(formattedDate);  
	}

}
