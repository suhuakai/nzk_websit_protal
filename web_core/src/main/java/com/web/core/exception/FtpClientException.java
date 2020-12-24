package com.web.core.exception;

/**
 * FTP客户端请求类异常
 *
 * @author
 * @version 1.0
 */
public class FtpClientException extends MediotException {

	public FtpClientException(String message) {
		super(message);
	}

	public FtpClientException(Throwable cause) {
		super(cause);
	}

	public FtpClientException(Integer code, Throwable cause) {
		super(code, cause);
	}

	public FtpClientException(String message, Throwable cause) {
		super(message, cause);
	}
}
