package com.web.common.util;

/**
 * 常量类
 *
 * @author sunhua
 * @date 2020/12/18
 */
public class Constant {

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

    /**
     * 用户默认密码
     */
    public static  final String DEFAULT_PWD = "123456";


}
