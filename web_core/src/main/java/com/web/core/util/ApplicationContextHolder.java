package com.web.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Web应用上下文工具类
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware, DisposableBean {

    public static final Logger logger = LoggerFactory.getLogger(ApplicationContextHolder.class);

    /**
     * 应用上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 配置参数/属性 定义器
     */
    private static ConfigurablePropertyResolver propertyResolver;

    @PostConstruct
    public static void init() {
        if (propertyResolver == null) {
            propertyResolver = getBean(ConfigurablePropertyResolver.class);
        }
    }

    /**
     * 获取配置项
     * @param key 参数名
     * @return String 参数值
     */
    public static String getProperty(String key) {
        String value = propertyResolver.getProperty(key);
        logger.debug("➧获取配置参数：{}={}", key, value);
        return value;
    }

    /**
     * 获取必需的配置项
     * @param key 参数名
     * @return String 参数值
     */
    public static String getRequiredProperty(String key) {
        String value = propertyResolver.getProperty(key);
        //String value = propertyResolver.getRequiredProperty(key);
        logger.debug("➧获取配置参数：{}={}", key, value);
        LocalAssert.notBlank(value, "配置参数“{}”，不能为空！", key);
        return value;
    }

    /**
     * 获取指定类型的Bean
     * @param requiredType
     * @param <T>
     * @return <T>
     */
    public static <T> T getBean(Class<T> requiredType) {
        LocalAssert.notNull(applicationContext, "应用环境：必须正常启动！");
        return applicationContext.getBean(requiredType);
    }

    /**
     * 获取指定名称的Bean
     * @param name
     * @param <T>
     * @return <T>
     */
    public static <T> T getBean(String name) {
        LocalAssert.notNull(applicationContext, "应用环境：必须正常启动！");
        return (T) applicationContext.getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    @Override
    public void destroy() throws Exception {
        applicationContext = null;
        logger.info("应用环境关闭，清理应用上下文对象。");
    }

}
