package com.web.common.security.permission.logic;

import com.web.common.security.permission.Logic;
import com.web.common.security.permission.annotation.RequireGuest;
import com.web.common.constant.CustomConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 游客 权限控制
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Component
@Order(7)
public class GuestPermission implements Logic<RequireGuest> {

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
    public boolean supportsPermitted(Class<RequireGuest> annotation) {
        return RequireGuest.class.isAssignableFrom(annotation);
    }

    /**
     * 【接口权限】游客模式
     * @param permission
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean processPermit(RequireGuest permission) throws Exception {
        //TODO 游客判断逻辑
        if (session.getAttribute(CustomConst.LoginUser.SESSION_USER_INFO) == null) {
            return true;
        } else {
            throw new SecurityException("鉴权：仅限游客访问！");
        }
    }

}
