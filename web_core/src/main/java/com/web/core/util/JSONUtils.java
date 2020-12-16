package com.web.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;

/**
 * 基于jackson的json工具类
 *
 * @author
 * @version	1.1
 */
public class JSONUtils {
	public final static Logger logger = LoggerFactory.getLogger(JSONUtils.class);
	private static ObjectMapper objectMapper;

	static{
		if(objectMapper==null){
			objectMapper = new ObjectMapper();
			objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));//配置项:默认日期格式
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//配置项:忽略未知属性
		}
		logger.debug("JSONUtils工具类初始化完成");
	}

	public static ObjectMapper getObjectMapper(){
		return objectMapper;
	}

	/**
	 * Java Bean转json格式串
	 * @param 	object
	 * @throws 	JsonProcessingException
	 * @return 	String
	 */
	public static String toJson(Object object) throws JsonProcessingException{
		return objectMapper.writeValueAsString(object);
	}

	/**
	 * Java Bean转json格式串（已做缩进美化展现）
	 * @param 	object
	 * @throws 	JsonProcessingException
	 * @return 	String
	 */
	public static String toPrettyJson(Object object) throws JsonProcessingException{
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
	}

	/**
	 * json格式串转换成Java Bean
	 * @param 	jsonString
	 * @param 	clazz
	 * @throws 	IOException
	 * @return 	String
	 */
	public static <T> T toBean(String jsonString, Class<T> clazz) throws IOException{
		return objectMapper.readValue(jsonString, clazz);
	}

	/**
	 * json格式串转换成Java Bean
	 * @param 	jsonString
	 * @param 	valueTypeRef
	 * @throws 	IOException
	 * @return 	String
	 */
	public static <T> T toBean(String jsonString, TypeReference<T> valueTypeRef) throws IOException{
		return objectMapper.readValue(jsonString, valueTypeRef);
	}

	/**
	 * json转换成Java Bean
	 * @author
	 *
	 * @param	jsonReader
	 * @param	clazz
	 * @throws	IOException
	 * @return	T
	 */
	public static <T> T toBean(Reader jsonReader, Class<T> clazz) throws IOException{
		return objectMapper.readValue(jsonReader, clazz);
	}

	/**
	 * json转换成Java Bean
	 * @author
	 *
	 * @param	jsonReader
	 * @param	valueTypeRef
	 * @throws	IOException
	 * @return	T
	 */
	public static <T> T toBean(Reader jsonReader, TypeReference<T> valueTypeRef) throws IOException{
		return objectMapper.readValue(jsonReader, valueTypeRef);
	}

	/**
	 * json转换成Java Bean
	 * @author
	 *
	 * @param	jsonInputStream
	 * @param	clazz
	 * @throws	IOException
	 * @return	T
	 */
	public static <T> T toBean(InputStream jsonInputStream, Class<T> clazz) throws IOException{
		return objectMapper.readValue(jsonInputStream, clazz);
	}

	/**
	 * json转换成Java Bean
	 * @author
	 *
	 * @param	jsonInputStream
	 * @param	valueTypeRef
	 * @throws	IOException
	 * @return	T
	 */
	public static <T> T toBean(InputStream jsonInputStream, TypeReference<T> valueTypeRef) throws IOException {
		return objectMapper.readValue(jsonInputStream, valueTypeRef);
	}

	/**
	 * Java Bean转json格式串（仅限打印日志等，非严谨场合使用）
	 * @param 	object
	 * @return 	String
	 */
	public static String toJsonLoosely(Object object){
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error("Java Bean转json格式串错误！", e);
		}
		return object.toString();
	}

	/**
	 * Java Bean转已做缩进美化格式json串（仅限打印日志等，非严谨场合使用）
	 * @param 	object
	 * @return 	String
	 */
	public static String toPrettyJsonLoosely(Object object){
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error("Java Bean转已做缩进美化格式json串错误！", e);
		}
		return object.toString();
	}

	/**
     * 获取泛型的Collection Type
     * @param collectionClass 泛型的Collection
     * @param elementClasses 元素类
     * @return JavaType Java类型
     * @since 1.0
     */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
	     return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
}
