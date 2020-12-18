package com.web.system.biz.userInfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.system.api.entity.UserInfo;
import com.web.system.biz.userInfo.dao.UserInfoMapper;
import com.web.system.biz.userInfo.service.UserInfoService;
import com.web.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sunhua
 * @since 2020-12-18
 */
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    /**
     * 根据手机号，账号，邮箱验证用户是否存在
     * @param loginNo
     * @return
     */
    @Override
    public UserInfo getByLoginNo(String loginNo) {
        UserInfo userInfo = baseMapper.selectOne(new QueryWrapper<UserInfo>()
                .eq("login_no", loginNo)
                .or()
                .eq("email", loginNo)
                .or()
                .eq("phone", loginNo));
        return userInfo;
    }

    /**
     * 账号密码
     * @param loginNo
     * @param password
     * @return
     */
    @Override
    public UserInfo findUserInfo(String loginNo, String password) {
        UserInfo userInfo = baseMapper.selectOne(new QueryWrapper<UserInfo>()
                .eq("login_no", loginNo).eq("password",password));
        return  userInfo;
    }
}
