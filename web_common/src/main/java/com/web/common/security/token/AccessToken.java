/*
 * Copyright (c) 2017-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.web.common.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.core.exception.ValidationException;
import com.web.core.util.LocalAssert;
import lombok.Data;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

/**
 * JWT访问票据
 * @author
 * @version 1.0
 *  */
@Data
public class AccessToken implements Token {
    /**
     * tokenid
     */
    private String tokenId;
    /**
     * 发行者
     */
    private String issuer;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户ip
     */
    private String userIp;
    /**
     * 用户代理
     */
    private String userAgent;
    /**
     * 创建时间
     */
    private Date creTime = new Date();
    /**
     * 过期时间（默认60分钟）
     */
    private Date expTime;

    private AccessToken() {
    }



    /**
     * 获取token
     * @param secret
     * @return String
     * @see com.auth0.jwt.impl.PublicClaims
     */
    @JsonIgnore
    @Override
    public String getToken(String secret) throws ValidationException {
        LocalAssert.notBlank(secret, "Token生成：必需指定密钥！");
        JWTCreator.Builder jwt = JWT.create();
        //tokenId
        jwt.withJWTId(this.tokenId);
        //发行者
        jwt.withIssuer(this.issuer);
        //发行时间
        jwt.withIssuedAt(this.creTime);
        //过期时间
        if (this.expTime != null) {
            jwt.withExpiresAt(expTime);
        } else {
            //默认120分钟
            jwt.withExpiresAt(DateUtils.addMinutes(this.creTime, 120));
        }
        jwt.withClaim("ui", this.userId);
        jwt.withClaim("uh", this.userIp);
        jwt.withClaim("ua", this.userAgent);
        return jwt.sign(Algorithm.HMAC256(secret));
    }
}
