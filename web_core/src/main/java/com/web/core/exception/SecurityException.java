package com.web.core.exception;

/**
 * [功能说明]: 安全与风险类异常
 * @author
 * @version 1.0
 */
public class SecurityException extends MediotException {

	public SecurityException(Integer code, Object... args) {
		super(code, args);
	}

	public SecurityException(Codable codable, Object... args) {
		super(codable, args);
	}

	public SecurityException(String message) {
		super(message);
	}

	public SecurityException(Throwable cause) {
		super(cause);
	}

	public SecurityException(Integer code, Throwable cause) {
		super(code, cause);
	}

	public SecurityException(String message, Throwable cause) {
		super(message, cause);
	}

}