package com.web.system.biz.userInfo.web;


import com.web.common.security.permission.annotation.Opened;
import com.web.system.api.entity.StaticInfo;
import com.web.system.biz.userInfo.service.StaticInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 字典信息表 前端控制器
 * </p>
 *
 * @author sunhua
 * @since 2020-12-24
 */
@RestController
@RequestMapping("/staticInfo")
@Opened
public class StaticInfoController {

    @Autowired
    StaticInfoService staticInfoService;

    /**
     *
     * @param staticType
     * @param code
     * @return
     */
    @RequestMapping("/dictListByCode")
    public List<StaticInfo> dictListByCode(String staticType, String code) {
        List<StaticInfo> staticInfoList = staticInfoService.dictListByCode(staticType, code);
        return  staticInfoList;
    }

}
