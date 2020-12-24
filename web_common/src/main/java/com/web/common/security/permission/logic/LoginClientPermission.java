package com.web.common.security.permission.logic;

import com.web.common.security.permission.Logic;
import com.web.common.security.permission.annotation.RequireLoginClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 第三方接入 权限控制
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Component
@Order(8)
public class LoginClientPermission implements Logic<RequireLoginClient> {

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
	public boolean supportsPermitted(Class<RequireLoginClient> annotation) {
		return RequireLoginClient.class.isAssignableFrom(annotation);
	}

	/**
	 * 【接口权限】登录模式
	 * @param permission
	 * @return boolean
	 * @throws Exception
	 */
	@Override
	public boolean processPermit(RequireLoginClient permission) throws Exception {
		//TODO 待实现
		throw new RuntimeException("必须先实现该方法的处理逻辑！");
	}

}
