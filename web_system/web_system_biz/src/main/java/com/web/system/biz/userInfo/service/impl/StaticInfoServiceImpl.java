package com.web.system.biz.userInfo.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.system.api.entity.StaticInfo;
import com.web.system.biz.userInfo.dao.StaticInfoMapper;
import com.web.system.biz.userInfo.service.StaticInfoService;
import com.web.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典信息表 服务实现类
 * </p>
 *
 * @author sunhua
 * @since 2020-12-24
 */
@Service
public class StaticInfoServiceImpl extends BaseServiceImpl<StaticInfoMapper, StaticInfo> implements StaticInfoService {

    @Override
    public List<StaticInfo> dictListByCode(String staticType, String code) {
        List<StaticInfo> staticInfoList = baseMapper.selectList(new QueryWrapper<StaticInfo>().eq("static_type",staticType).eq("code",code));
        return staticInfoList;
    }
}
