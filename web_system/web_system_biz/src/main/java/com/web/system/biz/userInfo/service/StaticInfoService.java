package com.web.system.biz.userInfo.service;

import com.web.system.api.entity.StaticInfo;
import com.web.system.biz.userInfo.entity.StaticInfo;
import com.web.core.service.BaseService;

import java.util.List;

/**
 * <p>
 * 字典信息表 服务类
 * </p>
 *
 * @author sunhua
 * @since 2020-12-24
 */
public interface StaticInfoService extends BaseService<StaticInfo> {

    List<StaticInfo> dictListByCode(String staticType, String code);
}
