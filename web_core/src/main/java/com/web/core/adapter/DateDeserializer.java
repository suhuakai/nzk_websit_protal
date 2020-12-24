package com.web.core.adapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * jackson日期反序列化器
 *
 * @author
 */
public class DateDeserializer extends JsonDeserializer<Date> {
	
	/**
	 * jackson日期反序列化
	 * @param parser
	 * @param context
	 * @throws IOException
	 */
	@Override
	public Date deserialize(JsonParser parser,	DeserializationContext context) throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = parser.getText();
		try {
			return format.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException("日期格式必须符合“yyyy-MM-dd”格式！", e);
		}
	}
}
