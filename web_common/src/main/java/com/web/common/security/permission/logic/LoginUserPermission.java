package com.web.common.security.permission.logic;

import com.web.common.security.permission.Logic;
import com.web.common.security.permission.annotation.RequireLoginUser;
import com.web.common.constant.CustomConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录用户 权限控制
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Component
@Order(3)
public class LoginUserPermission implements Logic<RequireLoginUser> {

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
    public boolean supportsPermitted(Class<RequireLoginUser> annotation) {
        return RequireLoginUser.class.isAssignableFrom(annotation);
    }

    /**
     * 【接口权限】登录模式
     * @param permission
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean processPermit(RequireLoginUser permission) throws Exception {
        if (session.getAttribute(CustomConst.LoginUser.SESSION_USER_INFO) != null) {
            return true;
        } else {
            throw new SecurityException("鉴权：请您先登录系统！");
        }
    }

}
