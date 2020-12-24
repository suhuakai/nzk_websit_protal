package com.web.common.constant;

import com.web.core.exception.Codable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码表<p/>
 * ===========================================<p/>
 * 【错误码表】<p/>
 * 200表示成功，所以不包含200状态码。<p/>
 * ----------编码规则（五位制）：10001 -------<p/>
 * <ul>
 * <li>（1）首位：系统标识（以后有可能2位）。<p/>
 * 1：ygb-core 基础核心模块<br/>
 * 2：ygb-common 公用模块<br/>
 * </li>
 * <li>（2）后4位：错误码，固定4位。</li>
 * </ul>
 * ---------- 使用案例 ----------<p/>
 * <ul>
 * <li>（1）10000 = 登录已过期，请重新登录！<p/>
 * throw new ValidationException(YgbError.LOGIN_SESSION_EXPIRE);</li>
 * <li>（2）12345 = {}星{}旗，{}!<p/>
 * throw new ValidationException(YgbError.E12345, 5, "红", "迎风飘扬", "好");</li>
 * <ul>
 * ===========================================
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Getter
@AllArgsConstructor
public enum WebError implements Codable {

    ERROR(500, "处理失败"),
    LOGIN_SESSION_EXPIRE(10000, "登录已过期，请重新登录！"),
    LOGIN_REQUIRE_TOKEN(10008, "缺少请求头“token”！"),
    LOGIN_SESSION_UNKNOWN(10006, "用户未正常登录！必须明确当前用户身份！"),

    LOGIN_REQUIRE_STORAGE_ID(20001, "登录检查：必须明确“库房”！"),
    LOGIN_REQUIRE_STORAGE_NAME(20002, "登录检查：必须明确“库房名称”！"),
    LOGIN_REQUIRE_STORAGE_TYPE(20003, "登录检查：必须明确“库房级别”！"),
    LOGIN_REQUIRE_DEPT_ID(20004, "登录检查：必须明确“科室”！"),
    LOGIN_REQUIRE_DEPT_NAME(20005, "登录检查：必须明确“科室名称”！"),
    LOGIN_REQUIRE_LARGE_STORAGE(20006, "登录检查：只有“大库房”才能做该操作！"),
    LOGIN_REQUIRE_DEPT_STORAGE(20007, "登录检查：只有“科室库”才能做该操作！"),



    /**  openapi错误码  */
    OPENAPI_STOP_USING(400 , "接口暂停使用"),
    OPENAPI_API_MISSING(404 , "接口不存在"),
    OPENAPI_ERROR(405 , "出错"),
    OPENAPI_PARAMETER(406 , "参数错误"),
    OPENAPI_DATA_MISSING(407 , "数据不存在"),
    OPENAPI_KEY_MISSING(408 , "appkey和appSecret不存在"),
    OPENAPI_ANONYMOUSAPI_BEYOND(409 , "匿名接口访问次数达上限"),
    OPENAPI_API_BEYOND(410	, "接口访问次数达上限"),
    OPENAPI_TOKEN_MISSING(801 , "token未授权"),
    OPENAPI_CODE_OVERDUE(802 , "过期code"),
    OPENAPI_TOKEN_OVERDUE(803 , "过期AccessToken"),

    ;

    /**错误码*/
    private final Integer code;
    /**错误信息*/
    private final String message;
}
