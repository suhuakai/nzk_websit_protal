package com.web.core.util;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


/**
 * 系统配置工具<br/>
 * 加载系统Properties配置文件至内存中<br/>
 * @author
 * @version 1.0
 */
public class SystemConfig {
    private static final Logger logger = LoggerFactory.getLogger(SystemConfig.class);
    private static final Properties properties = new Properties();

    public synchronized static void init(String resourcePattern) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(SystemConfig.class.getClassLoader());
        Resource[] resources = resolver.getResources(resourcePattern);
        for (Resource resource : resources) {
            if (logger.isDebugEnabled()) {
                logger.debug("加载Properties配置文件{}", resource.getFilename());
            }
            properties.load(new InputStreamReader(resource.getInputStream(), "UTF-8"));
        }
        if (logger.isDebugEnabled()) {
            Map<String, Object> configuration = MapUtils.typedSortedMap(new TreeMap(properties), String.class, Object.class);
            logger.debug("****************系统配置参数表****************\n{}", JSONUtils.toPrettyJsonLoosely(configuration));
        }
    }

    /**
     * 根据key找出value值
     * @param key 键
     * @return String
     * @author
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 根据key找出value值.没有找到则返回第二个参数的值
     * @param key 键
     * @param defaultValue 默认值
     * @return String
     * @author
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 获取整数参数值
     * @param key
     * @return Integer
     */
    public static Integer getIntProperty(String key) {
        String value = properties.getProperty(key);
        if (StringUtils.isNotBlank(value)) {
            return Integer.parseInt(value);
        }
        return null;
    }

    /**
     * 获取整数参数值，必需指定默认值
     * @param key
     * @param defaultValue
     * @return int
     */
    public static int getIntProperty(String key, Integer defaultValue) {
        LocalAssert.notNull(defaultValue, "defaultValue，不能为空");
        String value = properties.getProperty(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    /**
     * 检查是否包含指定key
     * @param key
     * @return boolean
     * @author
     */
    public static boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    /**
     * 检查系统环境是否包含指定值
     * @param value
     * @return boolean
     * @author
     */
    public static boolean containsValue(Object value) {
        return properties.containsValue(value);
    }
}
