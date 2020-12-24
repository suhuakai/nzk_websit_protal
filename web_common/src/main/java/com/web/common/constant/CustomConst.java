package com.web.common.constant;

/**
 * 常量定义类: 用于定义系统中用到的常量值或者码值
 *
 * @author
 * @version 1.0
 */
public class CustomConst {
    /**
     * 用户成功登录后存储在session里的信息变量名称
     */
    public final static class LoginUser {

        /**
         * 用户对象
         */
        public final static String SESSION_USER_INFO = "sessionUserInfo";

    }

    /**
     * 用户默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 默认验证码
     */
    public static final String DEFAULT_IMAGECODE = "8888";

    /**
     * 用户状态 0停用  1启用
     */
    public enum UserInfoFstate {
        UN_Enable(0, "停用"),
        Enable(1, "启用");
        private Integer code;
        private String value;

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        UserInfoFstate(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

    }


}


