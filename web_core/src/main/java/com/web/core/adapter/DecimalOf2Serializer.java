package com.web.core.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * JSON序列化: 数值保留2位小数
 * @author
 * @version	1.0
 */
public class DecimalOf2Serializer<Number> extends JsonSerializer<Number> {
	
	@Override
	public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers) 
			throws IOException {
		/*DecimalFormat format = new DecimalFormat("0.00");//保留2位小数
		format.setRoundingMode(RoundingMode.HALF_UP);//四舍五入
		gen.writeNumber(new BigDecimal(format.format(value)));*/
		
		BigDecimal decimal = new BigDecimal(value.toString());
		decimal = decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		gen.writeNumber(decimal);
	}
	
}
