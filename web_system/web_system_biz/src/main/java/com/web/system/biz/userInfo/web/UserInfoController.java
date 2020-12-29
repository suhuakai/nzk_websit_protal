package com.web.system.biz.userInfo.web;


import com.web.common.security.permission.annotation.Opened;
import com.web.core.util.LocalAssert;
import com.web.system.api.entity.UserInfo;
import com.web.system.biz.userInfo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sunhua
 * @since 2020-12-18
 */
@RestController
@RequestMapping("/userInfo")
@Opened
public class UserInfoController {
    @Autowired
    UserInfoService userInfoService;

    @RequestMapping("findById")
    public UserInfo findById(String id){
        LocalAssert.notNull(id,"ID不允许为空");
        UserInfo userInfo = userInfoService.selectById(id);
        return  userInfo;

    }

}
