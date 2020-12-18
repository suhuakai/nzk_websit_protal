package com.web.system.biz.userInfo.service;

import com.web.system.api.entity.UserInfo;
import com.web.core.service.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sunhua
 * @since 2020-12-18
 */
public interface UserInfoService extends BaseService<UserInfo> {

    UserInfo getByLoginNo(String loginNo);

    UserInfo findUserInfo(String loginNo, String password);
}
