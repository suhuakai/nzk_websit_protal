package com.web.core.converter;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 文本串按票房切割成串列表
 *
 * @author
 * @version 1.0
 */
public class TokenizeSplitToListConvertor implements Convertor {
	/**分隔符*/
	final String CONFIG_LOCATION_STRING_DELIMITERS = ",; \t\n";

	@Override
	public List<String> convert(String source) {
		if(!StringUtils.isEmpty(source)) {
			String[] array=StringUtils.tokenizeToStringArray(source, CONFIG_LOCATION_STRING_DELIMITERS);
			return array==null ? null : CollectionUtils.arrayToList(array);
		}
		return null;
	}
}
