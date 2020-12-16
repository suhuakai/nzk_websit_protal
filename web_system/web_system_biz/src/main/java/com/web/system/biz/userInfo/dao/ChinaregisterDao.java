package com.web.system.biz.userInfo.dao;

import com.web.core.entity.Pager;
import com.web.system.api.entity.Chinaregister;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sunhua
 * @since 2020-12-15
 */
public interface ChinaregisterDao extends BaseMapper<Chinaregister> {

    List<Chinaregister> getList(Pager pager);
}
