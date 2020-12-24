package com.web.core.util;

import com.web.core.exception.ValidationException;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 本地化扩展数值工具类
 * @author
 * @version	1.0
 */
public final class LocalNumberUtils {

	/**
	 * 判断某整数大于指定整数
	 *
	 *
	 * @author
	 * @param realValue
	 * @param compareTo
	 * @return boolean
	 */
	public static boolean intGreaterThan(Number realValue, Number compareTo) {
		return realValue.longValue() > compareTo.longValue();
	}

	/**
	 * 判断某整数大于或等于指定整数
	 *
	 *
	 * @author
	 * @param realValue
	 * @param compareTo
	 * @return boolean
	 */
	public static boolean intGreaterEqual(Number realValue, Number compareTo) {
		return realValue.longValue() >= compareTo.longValue();
	}

	/**
	 * 判断某整数等于指定整数
	 *
	 * @author
	 *
	 * @param realValue
	 * @param compareTo
	 * @return boolean
	 */
	public static boolean intEqual(Number realValue, Number compareTo) {
		return realValue.longValue() == compareTo.longValue();
	}

	/**
	 * 判断某整数不等于指定整数
	 *
	 * @author
	 * 	 * @param realValue
	 * @param compareTo
	 * @return boolean
	 */
	public static boolean intNotEqual(Number realValue, Number compareTo) {
		return realValue.longValue() != compareTo.longValue();
	}

	/**
	 * 判断某整数小于指定整数
	 *
	 * @author
	 * 	 * @param realValue
	 * @param compareTo
	 * @return boolean
	 */
	public static boolean intLessThan(Number realValue, Number compareTo) {
		return realValue.longValue() < compareTo.longValue();
	}

	/**
	 * 判断某整数小于或等于指定整数
	 *
	 * @author
	 * 	 * @param realValue
	 * @param compareTo
	 * @return boolean
	 */
	public static boolean intLessEqual(Number realValue, Number compareTo) {
		return realValue.longValue() <= compareTo.longValue();
	}
	
	/**
	 * 数值格式化为字符串（可以指定默认值）
	 * @author
	 *
	 * @param	number
	 * @param	pattern
	 * @return	String
	 * @throws	ValidationException 
	 */
	public static String format(Number number, String pattern, String defaultValue) throws ValidationException {
		String value = format(number, pattern);
		return value==null||value.trim().isEmpty() ? defaultValue : value;
	}
	
	/**
	 * 数值格式化为字符串
	 * @author
	 *
	 * @param	number
	 * @param	pattern
	 * @return	String
	 * @throws	ValidationException 
	 */
	public static String format(Number number, String pattern) throws ValidationException {
		LocalAssert.notBlank(pattern, "请指定数值的格式");
		if(number==null){
			return null;
		}
		DecimalFormat format = new DecimalFormat(pattern);//保留2位小数
		format.setRoundingMode(RoundingMode.HALF_UP);//四舍五入
		return format.format(number);
	}
	
	/**
	 * 数值串行化，可以指定默认值
	 * @author
	 *
	 * @param	number
	 * @param	defaultValue
	 * @return	String
	 */
	public static String toString(Number number, String defaultValue) {
		return number==null ? defaultValue : number.toString();
	}
	
}
