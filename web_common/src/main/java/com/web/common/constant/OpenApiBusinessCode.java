package com.web.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.web.common.constant.OpenApiConst.ApiType;

@Getter
@AllArgsConstructor
public enum OpenApiBusinessCode {

    JQ3010      (ApiType.webservice, OpenApiConst.IoType.OUT, "目录");


    /** 对接方式，1、http;2、webservice;3、sql;4、sp */
    private ApiType apiType;

    /** 接口方向（1接口被对方调用-提供者、0、调用对方接口-消费者）*/
    private Integer ioType;
    
    /** 摘要 */
    private String summary;

}
