package com.web.core.converter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义ObjectMapper
 * @author
 * @version 1.0
 */
public class CustomObjectMapper extends ObjectMapper {

	public CustomObjectMapper() {
		super();
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//配置项:忽略未知属性
	}

}