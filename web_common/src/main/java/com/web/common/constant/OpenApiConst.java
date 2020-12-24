package com.web.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class OpenApiConst {

    /**
     * 接口方向（1接口被对方调用、0调用对方接口）
     * @author
     *      */
    public static final class IoType {

        /** 1接口被对方调用 */
        public static final int IN = 1;

        /** 0调用对方接口 */
        public static final int OUT = 0;

    }

    /**
     * 状态: 1 待执行，2 执行成功，9、执行失败
     * @author
     *      */
    public static final class Fstate {

        /** 1 待执行 */
        public static final int USABLE = 1;

        /** 2 执行成功 */
        public static final int SUCCESS = 2;

        /** 9 执行失败 */
        public static final int FAIL = 9;

    }

    /**
     * 对接方式，1、http;2、webservice;3、sql;4、sp
     * @author
     *      */
    @Getter
    @AllArgsConstructor
    public enum ApiType {
        http(1, "http"),
        webservice(2, "webservice"),
        sql(3, "sql"),
        sp(4, "sp");

        private int code;
        private String name;
    }

    /**
     * 是否可用，1、可用，0、不可用
     * @author
     *      */
    public static final class ServerStatus {

        /** 1 可用 */
        public static final int available = 1;

        /** 0 不可用 */
        public static final int unavailable = 0;

    }
    /**
     * API参数类型
     * @author
     *      */
    public static final class ApiParameterType {

        /** 1、系统级输入参数 */
        public static final Integer requestHeader = 1;

        /** 2、应用级输入参数 */
        public static final Integer requestBody = 2;

        /** 3、返回结果 */
        public static final Integer responseBody = 3;

    }

    /**
     * API传参格式
     * @author
     *      */
    public static final class ApiReferenceFormat {
        /** 1、FromData */
        public static final Integer FromData = 1;

        /** 2、Json */
        public static final Integer Json = 2;

    }
}
