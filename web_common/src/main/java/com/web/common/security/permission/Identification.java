package com.web.common.security.permission;

import java.util.Set;

/**
 * 用户身份
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
public interface Identification {

    /**
     * 获取：用户id
     */
    public String getUserId();

    /**
     * 获取：用户菜单集合
     */
    Set<String> getUserMenuCodes();

    /**
     * 获取：当前机构类型
     */
    Integer getOrgType();

}
