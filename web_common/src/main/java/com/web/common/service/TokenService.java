package com.web.common.service;

import com.web.common.security.token.AccessToken;

/**
 * Token服务
 * @author
 * @version 1.0
 */
public interface TokenService {

	/**
	 * Token验证
	 * @param token
	 * @throws RuntimeException
	 * @author
	 */
	AccessToken verifyToken(String token) throws RuntimeException;

	/**
	 * Token解码
	 * @param token
	 * @return AccessToken
	 * @throws RuntimeException
	 * @author
	 */
	AccessToken decodeToken(String token) throws RuntimeException;

}
