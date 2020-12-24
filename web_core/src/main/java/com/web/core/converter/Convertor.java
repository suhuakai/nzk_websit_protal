package com.web.core.converter;

/**
 * 数据、对象转换器
 *
 * @author
 * @version 1.0
 */
public interface Convertor<T> {
	
	/**
	 * 数据转换为目标对象
	 * @author
	 *
	 * @param source
	 * @return T
	 */
	T convert(String source);
}
