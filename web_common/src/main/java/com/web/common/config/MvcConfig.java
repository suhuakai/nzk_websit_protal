package com.web.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.core.converter.ResResult2HttpMessageConverter;
import com.web.core.interceptor.ResResultInterceptor;
import com.web.core.resolver.StringArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
@Slf4j
public class MvcConfig implements WebMvcConfigurer {

    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ResResult2HttpMessageConverter converter = new ResResult2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        return converter;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(httpMessageConverter -> MappingJackson2HttpMessageConverter.class.isAssignableFrom(httpMessageConverter.getClass()));
        converters.add(0, getMappingJackson2HttpMessageConverter());
        log.info("■ 消息转换器列表：" + converters);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //请求响应标准报文数据结构处理拦截器
        registry.addInterceptor(new ResResultInterceptor()).addPathPatterns("/**").excludePathPatterns(new String[]{
                "/static/**",
                "/webjars/**",
                "doc.html"
        });
        log.info("■ 拦截器设置完成。");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "HEAD", "OPTIONS","PUT").allowedHeaders("*")
                // .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "token")
                .exposedHeaders("Server",
                                "Date",
                                "Content-Length",
                                "Set-Cookie2",
                                "Set-Cookie",
                                "addCookie",
                                "cookie",
                                "Cookies",
                                "Content-Disposition",
                                "token",
                                "Access-Token",
                                "Access-Control-Allow-Origin",
                                "Access-Control-Allow-Credentials").allowCredentials(true);
        log.info("■ CORS跨域处理完成。");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath*:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath*:/META-INF/resources/webjars/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new StringArgumentResolver());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new Formatter<LocalDate>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public LocalDate parse(String date, Locale locale) {
                return LocalDate.parse(date, formatter);
            }

            @Override
            public String print(LocalDate date, Locale locale) {
                return date.format(formatter);
            }
        });
        registry.addFormatter(new Formatter<LocalDateTime>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            public LocalDateTime parse(String date, Locale locale) {
                return LocalDateTime.parse(date, formatter);
            }

            @Override
            public String print(LocalDateTime date, Locale locale) {
                return date.format(formatter);
            }
        });
    }

}
