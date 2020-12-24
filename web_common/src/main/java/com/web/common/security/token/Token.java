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


import com.web.core.exception.ValidationException;

import java.io.Serializable;

/**
 * token（访问票据）
 * @author
 * @version 1.0
 *  */
public interface Token extends Serializable {

	/**
	 * 生成 Token 字符串
	 * @return String
	 */
	String getToken(String secret) throws ValidationException;
}
