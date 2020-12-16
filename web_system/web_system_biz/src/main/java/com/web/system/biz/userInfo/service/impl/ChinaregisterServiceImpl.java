package com.web.system.biz.userInfo.service.impl;
import com.web.core.entity.Pager;
import com.web.core.service.impl.BaseServiceImpl;
import com.web.system.biz.userInfo.dao.ChinaregisterDao;
import com.web.system.api.entity.Chinaregister;
import com.web.system.biz.userInfo.service.ChinaregisterService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sunhua
 * @since 2020-12-15
 */
@Service
public class ChinaregisterServiceImpl extends BaseServiceImpl<ChinaregisterDao, Chinaregister> implements ChinaregisterService {

    @Override
    public List<Chinaregister> getList(Pager pager) {
        return baseMapper.getList(pager);
    }
}
