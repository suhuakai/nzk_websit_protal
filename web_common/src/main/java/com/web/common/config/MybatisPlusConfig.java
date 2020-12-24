package com.web.common.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.web.core.interceptor.PaginateInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Mybatis-Plus配置
 * @author
 * @version 1.0
 */
@Configuration
public class MybatisPlusConfig {

    @Primary
    @Bean
    public PaginationInterceptor beanPaginationInterceptor() {
        PaginationInterceptor pageInterceptor = new PaginationInterceptor();
        pageInterceptor.setDialectType("mysql");
        return pageInterceptor;
    }

    @Bean
    public PaginateInterceptor beanPaginateInterceptor() {
        PaginateInterceptor pageInterceptor = new PaginateInterceptor();
        pageInterceptor.setDbType("mysql");
        return pageInterceptor;
    }

}
