package com.web.system.biz.userInfo.service;

import com.web.core.service.BaseService;
import com.web.core.util.PageUtils;
import com.web.system.api.entity.Chinaregister;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sunhua
 * @since 2020-12-15
 */
public interface ChinaregisterService extends BaseService<Chinaregister> {

    PageUtils queryPage(Map<String, Object> params);
}
