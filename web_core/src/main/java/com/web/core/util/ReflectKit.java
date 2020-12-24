package com.web.core.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具类
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Slf4j
public class ReflectKit {

	/**
	 * 反射对象获取泛型
	 * @param clazz 对象
	 * @param index 泛型所在位置
	 * @return Class
	 */
	public static Class getClassGenericType(final Class clazz, final int index) {
		Type interfaceType = clazz.getGenericInterfaces()[0];
		if (!(interfaceType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) interfaceType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			throw new RuntimeException("泛型参数位置指定越界！");
		}
		Type paramType = params[index];
		/*if (!(paramType instanceof Class)) {
			log.warn(String.format("注意: %s not set the actual class on superclass generic parameter", clazz.getSimpleName()));
			return Object.class;
		}*/
		return (Class) paramType;
	}

}
