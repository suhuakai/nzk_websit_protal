package com.web.core.adapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * jackson日期时间反序列化基本解析器
 *
 * @author
 */
public class DateTimeDeserializer extends JsonDeserializer<Date> {
	
	/**
	 * ackson日期时间反序列化
	 * @param parser
	 * @param context
	 * @throws IOException
	 */
	@Override
	public Date deserialize(JsonParser parser,	DeserializationContext context) throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = parser.getText();
		try {
			return format.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
