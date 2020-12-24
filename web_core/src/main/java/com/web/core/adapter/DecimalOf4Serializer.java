package com.web.core.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * JSON序列化: 数值保留4位小数
 * @author
 * @version	1.0
 */
public class DecimalOf4Serializer<Number> extends JsonSerializer<Number> {
	
	@Override
	public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers) 
			throws IOException {
		BigDecimal decimal = new BigDecimal(value.toString());
		decimal = decimal.setScale(4, BigDecimal.ROUND_HALF_UP);
		gen.writeNumber(decimal);
	}
}
