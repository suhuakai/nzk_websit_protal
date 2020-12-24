/**
 * 
 */
package com.web.common.utils.bean2map;

/**  
* <p>Description: </p>
*  @version 1.0
* @since   JDK 1.8
*/


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web.common.mapper.JsonMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;


public class BeanMapConvertUtil {
	
	
	private static Logger logger = Logger.getLogger(BeanMapConvertUtil.class);

	

	public  static <T> List<Map<String, Object>> beanToMap(List<T> list,Class<T> clazz) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		try {
			for(T t : list){
				Map<String, Object> map = new HashMap<String, Object>();
				map =  JsonMapper.toMapString(t);
				mapList.add(map);
			}

		} catch (Exception e) {
			logger.error("transBean2Map Error " + e);
		}
		return mapList;
	}
	
	public static Map<String, Object> beanToMap(Object bean) {
		if (bean == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!"class".equals(key)) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(bean);
					map.put(key, value);
				}

			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}

		return map;
	}

	public static <T> T mapToBean(Map<String, Object> map, T bean) {
		try {
			BeanUtils.populate(bean, map);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error(e, e.fillInStackTrace());
		}
		return bean;
	}
	
	
	
//	public static void main(String[] args) {
//		List<YpjxqStatics> list = Lists.newArrayList();
//		YpjxqStatics xx  = new YpjxqStatics();
//		xx.setApprovalNo("nn");
//
//		list.add(xx);
//
//		beanToMap(list,YpjxqStatics.class);
//
//
//	}
}
