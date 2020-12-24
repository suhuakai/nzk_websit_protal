package com.web.common.security.permission.logic;

import com.web.common.security.permission.Logic;
import com.web.common.security.permission.annotation.Opened;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 开放（任何人都可以访问） 权限控制
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Component
@Order(5)
public class OpenedPermission implements Logic<Opened> {

	/**
	 * 适用条件
	 * @param annotation
	 * @return boolean
	 */
	@Override
	public boolean supportsPermitted(Class<Opened> annotation) {
		return Opened.class.isAssignableFrom(annotation);
	}

	/**
	 * 权限控制
	 * @param permission
	 * @return boolean
	 */
	@Override
	public boolean processPermit(Opened permission) throws Exception {
		return true;
	}

}
