package com.web.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;

/**
 * 应用初始化完成以后追加操作
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Component
public class WebListener implements ApplicationListener<ContextRefreshedEvent> {

    public static final Logger logger = LoggerFactory.getLogger(WebListener.class);

    @Value("${spring.session.timeout}")
    private int sessionTimeout = 1800;

    @Autowired
    RedisOperationsSessionRepository sessionRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        sessionRepository.setDefaultMaxInactiveInterval(sessionTimeout);
        logger.info("应用初始化完成以后追加操作成功。");
    }

}
