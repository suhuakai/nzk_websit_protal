package com.web.system.api.vo;

import com.web.system.api.entity.UserInfo;
import lombok.Data;

@Data
public class UserInfoVo extends UserInfo {
    private String token;
}
