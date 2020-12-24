package com.web.common.security.permission.logic;

import com.web.common.security.permission.Identification;
import com.web.common.security.permission.Logic;
import com.web.common.security.permission.annotation.RequireOrgType;
import com.web.common.constant.CustomConst;
import com.web.core.util.LocalAssert;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 机构类型 权限控制
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Component
@Order(2)
public class OrgTypePermission implements Logic<RequireOrgType> {

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
	public boolean supportsPermitted(Class<RequireOrgType> annotation) {
		return RequireOrgType.class.isAssignableFrom(annotation);
	}

	/**
	 * 【接口权限】根据“机构类型”控制
	 * @param permission
	 * @return boolean
	 * @throws Exception
	 */
	@Override
	public boolean processPermit(RequireOrgType permission) throws Exception {
		int[] values = permission.value();
		LocalAssert.notEmpty(values, "权限注解：请指定“机构类型”！");

		//当前会话用户
		Identification sessionUser = (Identification) session.getAttribute(CustomConst.LoginUser.SESSION_USER_INFO);
		LocalAssert.notNull(sessionUser, "请确保用户已正确登录！");
		LocalAssert.notNull(sessionUser.getOrgType(), "登录用户：“机构类型”，不能为空！");
		//当前用户“机构类型”
		int userOrgType = sessionUser.getOrgType();
		if (ArrayUtils.contains(values, userOrgType)) {
			return true;
		} else {
			String userOrgTypeName = null;
			switch (userOrgType){
				/*case CustomConst.OrgType.CARRIER: userOrgTypeName = "运营商"; break;
				case CustomConst.OrgType.HOSPITAL: userOrgTypeName = "医院"; break;
				case CustomConst.OrgType.SUPPLIER: userOrgTypeName = "企业"; break;
				case CustomConst.OrgType.OTHER: userOrgTypeName = "其他"; break;
				default: userOrgTypeName = "未知的机构类型"; break;*/
			}
			throw new SecurityException("鉴权：" + userOrgTypeName + "，无权访问该接口！");
		}
	}

}