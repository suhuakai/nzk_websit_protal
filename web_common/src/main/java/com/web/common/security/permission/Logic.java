package com.web.common.security.permission;

import java.lang.annotation.Annotation;

/**
 * 权限控制逻辑
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
public interface Logic<T extends Annotation> {

	/**
	 * 适用条件
	 * @param annotation
	 * @return boolean
	 */
	boolean supportsPermitted(Class<T> annotation);

	/**
	 * 权限控制
	 * @param annotation
	 * @return boolean
	 */
	boolean processPermit(T annotation) throws Exception;

}
