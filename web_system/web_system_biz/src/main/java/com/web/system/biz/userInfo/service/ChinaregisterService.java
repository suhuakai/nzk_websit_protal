package com.web.system.biz.userInfo.service;

import com.web.core.entity.Pager;
import com.web.core.service.BaseService;
import com.web.system.api.entity.Chinaregister;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sunhua
 * @since 2020-12-15
 */
public interface ChinaregisterService extends BaseService<Chinaregister> {

    List<Chinaregister> getList(Pager pager);
}
