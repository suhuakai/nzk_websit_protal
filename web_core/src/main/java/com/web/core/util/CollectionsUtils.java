package com.web.core.util;

import org.apache.commons.beanutils.BeanUtils;

import java.util.*;

/**
 * 集合工具类
 * 
 * @author
 *
 */
public class CollectionsUtils {

	/**
	 * 将Map的KEY大写转成小写
	 * 
	 * @param map
	 * @return
	 */
	public static Map MapKeyToLowerCase(Map map) {
		Map result = new HashMap();
		for (Object key : map.keySet()) {
			/*
			 * if(!((String)key).toLowerCase().equals(key)){
			 * result.put(((String)key).toLowerCase(), map.get(key)); }else{
			 * result.put(key, map.get(key)); }
			 */
			if (key instanceof String) {
				if (!((String) key).toLowerCase().equals(key)) {
					result.put(((String) key).toLowerCase(), map.get(key));
				} else {
					result.put(key, map.get(key));
				}
			} else {
				result.put(key, map.get(key));
			}
		}
		return result;
	}

	/**
	 * 将List里的Map的KEY大写转成小写
	 * 
	 * @param list
	 * @return
	 */
	public static List<Map> ListMapKeyToLowerCase(List<Map> list) {
		List result = new ArrayList();
		for (Map m : list) {
			result.add(MapKeyToLowerCase(m));
		}
		return result;
	}

	/**
	 * Map转Bean
	 * @param map
	 * @param t
	 * @return
	 */
	public static <T> T MapToBean(Class<T> t,Map map) {
		T obj = null;
		try {
			obj = t.newInstance();
			BeanUtils.populate(obj, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 集合对象转ArrayList对象
	 * @param collection
	 * @return ArrayList<T>
	 */
	public static <T> ArrayList<T> CollectionToList(Collection<T> collection){
		ArrayList list=null;
		if(collection!=null){
			list=new ArrayList<T>();
			if(!collection.isEmpty()){
				for(T e: collection){
					list.add(e);
				}
			}
		}
		return list;
	}

}
