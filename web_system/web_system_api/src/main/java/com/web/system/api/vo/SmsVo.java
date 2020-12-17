package com.web.system.api.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class SmsVo {

    @Value("${spring.sms.regoinId}")
    public String regoinId;

    @Value("${spring.sms.product}")
    public String product;

    @Value("${spring.sms.accessKeyId}")
    public String accessKeyId;

    @Value("${spring.sms.secret}")
    public String secret;

    @Value("${spring.sms.domain}")
    public String domain;

    @Value("${spring.sms.signName}")
    public String signName;

    @Value("${spring.sms.templateCode}")
    public String templateCode;
}
