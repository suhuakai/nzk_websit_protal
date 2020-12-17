package com.web.core.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import java.text.SimpleDateFormat;

/**
 * ygb配置
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Configuration
public class WebConfig {

    public final static String HTTP_SESSION_ID = "token";

    @Bean
    @Primary
    public HttpSessionIdResolver httpSessionIdResolver() {
        return new HeaderHttpSessionIdResolver(HTTP_SESSION_ID);
    }

    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //创建ObjectMapper实例
        ObjectMapper objectMapper = new ObjectMapper();
        //设置：默认日期格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //设置：忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //设置：忽略null值属性
        objectMapper.setSerializationInclusion(Include.NON_NULL);

        //创建Jackson2JsonRedisSerializer序列化与反序列化器
        Jackson2JsonRedisSerializer<Object> jacksonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jacksonRedisSerializer.setObjectMapper(objectMapper);

        //创建RedisTemplate实例
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jacksonRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}
