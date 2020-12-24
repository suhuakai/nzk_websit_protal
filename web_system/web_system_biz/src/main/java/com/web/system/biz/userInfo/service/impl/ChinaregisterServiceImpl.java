package com.web.system.biz.userInfo.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.core.service.impl.BaseServiceImpl;
import com.web.system.biz.userInfo.dao.ChinaregisterDao;
import com.web.system.api.entity.Chinaregister;
import com.web.system.biz.userInfo.service.ChinaregisterService;
import org.springframework.stereotype.Service;
import java.util.Map;

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

  /*  @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Chinaregister> page =baseMapper.selectPage(
                new Query<Chinaregister>().getPage(params),
                new QueryWrapper<Chinaregister>()
        );

        return new PageUtils(page);
    }*/
}
