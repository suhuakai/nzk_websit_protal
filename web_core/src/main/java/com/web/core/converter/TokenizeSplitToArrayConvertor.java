package com.web.core.converter;

import org.springframework.util.StringUtils;

public class TokenizeSplitToArrayConvertor implements Convertor {
	/**分隔符（字符串）*/
	final String CONFIG_LOCATION_STRING_DELIMITERS = ",; \t\n";

	@Override
	public String[] convert(String source) {
		if(!StringUtils.isEmpty(source)) {
			return StringUtils.tokenizeToStringArray(source, CONFIG_LOCATION_STRING_DELIMITERS);
		}
		return null;
	}
}
