package com.web.common.security.permission.logic;

import com.web.common.security.permission.Identification;
import com.web.common.security.permission.Logic;
import com.web.common.security.permission.annotation.RequireMenu;
import com.web.common.constant.CustomConst;
import com.web.core.util.LocalAssert;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * 菜单 权限控制
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Component
@Order(1)
public class MenuPermission implements Logic<RequireMenu> {

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
	public boolean supportsPermitted(Class<RequireMenu> annotation) {
		return RequireMenu.class.isAssignableFrom(annotation);
	}

	/**
	 * 【接口权限】根据菜单权限控制
	 */
	@Override
	public boolean processPermit(RequireMenu permission) throws Exception {
		//权限编码
		String[] values = permission.value();
		LocalAssert.isNotEmpty(values, "权限注解：请指定“菜单编码”！");
		//权限控制是否通过标识
		boolean permissFlag = false;
		if (ArrayUtils.isNotEmpty(values)) {
			//当前会话用户
			Identification sessionUser = (Identification) session.getAttribute(CustomConst.LoginUser.SESSION_USER_INFO);
			LocalAssert.notNull(sessionUser, "请确保用户已正确登录！");
			//当前用户的权限列表
			Set<String> userMenuCodes = sessionUser.getUserMenuCodes();
			//结合接口权限注解判断是否有权限
			if (CollectionUtils.isNotEmpty(userMenuCodes)) {
				for (String menu : values) {
					LocalAssert.notBlank(menu, "权限注解：请指定“菜单编码”！");
					if (userMenuCodes.contains(menu.trim())) {//拥有对应权限
						permissFlag = true;
						break;
					}
				}
			}
		}
		if (!permissFlag) {
			throw new SecurityException("鉴权：无权访问该接口！");
		}
		return permissFlag;
	}

}
