package com.web.common.security.permission.logic;

import com.web.common.security.permission.Logic;
import com.web.common.security.permission.annotation.RequireToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 访问令牌 权限控制
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Component
@Order(6)
public class TokenPermission implements Logic<RequireToken> {

    @Autowired
    HttpSession session;
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;

    /**
     * 适用条件
     * @param annotation
     * @return boolean
     */
    @Override
    public boolean supportsPermitted(Class<RequireToken> annotation) {
        return RequireToken.class.isAssignableFrom(annotation);
    }

    /**
     * 权限控制
     * @param permission
     * @return boolean
     */
    @Override
    public boolean processPermit(RequireToken permission) throws Exception {
        //TODO 待实现
        throw new RuntimeException("必须先实现该方法的处理逻辑！");
    }

}
