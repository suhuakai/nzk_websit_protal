package com.web.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface CommonService {

    /**
     * 新增
     * @param entity 实体对象
     * @throws Exception
     *
     */
    <T> int insert(T entity) throws Exception;

    /**
     * 按id删除一个对象
     * @param clazz 类型
     * @param id 主键
     * @throws Exception
     *
     */
    <T> int deleteById(Class<T> clazz, Serializable id) throws Exception;

    /**
     * 删除: 按照指定条件
     * @param condition 条件
     * @throws Exception
     *
     */
    <T> int delete(T condition) throws Exception;

    /**
     * 根据条件修改（全部字段更新）
     * @param entity 对象
     * @param condition 条件
     * @throws Exception
     *
     */
    <T> int updateCover(T entity, T condition) throws Exception;

    /**
     * 根据条件修改（只更新非空值属性对应的字段，空属性忽略）
     * @param entity 信息
     * @param condition 条件
     * @throws Exception
     *
     */
    <T> int update(T entity, T condition) throws Exception;

    /**
     * 按主键更新对象（全部字段更新）
     * @param entity 信息
     * @throws Exception
     *
     */
    <T> int updateCoverById(T entity) throws Exception;

    /**
     * 按主键更新对象（非空项更新）
     * @param entity 对象
     * @throws Exception
     *
     */
    <T> int updateById(T entity) throws Exception;

    /**
     * 根据id查询一个对象
     * @param clazz 类型
     * @param id id
     * @throws Exception
     *
     */
    <T> T findById(Class<T> clazz, Serializable id) throws Exception;

    /**
     * 根据条件查找一个对象
     * @param condition 条件
     * @throws Exception
     *
     */
    <T> T find(T condition) throws Exception;

    /**
     * 根据条件查找列表
     * @param condition 条件
     * @throws Exception
     *
     */
    <T> List<T> queryList(T condition) throws Exception;

    /**
     * 批量插入操作
     * @param entityList 实体列表
     * @throws Exception
     */
    void insertList(Collection<?> entityList) throws Exception;

    /**
     * 批量插入操作
     * @param pageSize 分页size
     * @param entityList 实体列表
     * @throws Exception
     */
    void insertListPager(int pageSize, Collection<?> entityList) throws Exception;

    /**
     * 批量插入操作（兼容动态入参）
     * @param entitys 实体列表
     * @throws Exception
     */
    void insertList(Object... entitys) throws Exception;

    /**
     * 批量插入操作（兼容动态入参）
     * @param pageSize 分页size
     * @param entitys 实体列表
     * @throws Exception
     */
    void insertListPager(int pageSize, Object... entitys) throws Exception;

    /**
     * 根据主键批量合并更新
     * @param entity 实体
     * @param insertExcludeFieldNames insert排除的字段
     * @param updateExcludeFieldNames update排除的字段
     * @throws Exception
     */
    <T> void merge(T entity, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames) throws Exception;

    /**
     * 根据主键合并更新
     * @param entity 实体对象
     * @param excludeFieldNames 排除的字段
     * @throws Exception
     */
    <T> void merge(T entity, String... excludeFieldNames) throws Exception;

    /**
     * 根据主键批量合并更新
     * @param entityList 实体列表
     * @param insertExcludeFieldNames insert排除的字段
     * @param updateExcludeFieldNames update排除的字段
     * @throws Exception
     */
    void mergeList(Collection<?> entityList, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames) throws Exception;

    /**
     * 根据主键批量合并更新
     * @param pageSize 分页size
     * @param entityList 实体列表
     * @param insertExcludeFieldNames insert排除的字段
     * @param updateExcludeFieldNames update排除的字段
     * @throws Exception
     */
    void mergeListPager(int pageSize, Collection<?> entityList, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames) throws Exception;

    /**
     * 根据主键合并更新（批量）
     * @param entityList 实体列表
     * @param excludeFieldNames 排除的字段
     * @throws Exception
     */
    void mergeList(Collection<?> entityList, String... excludeFieldNames) throws Exception;

    /**
     * 根据主键合并更新（批量）
     * @param pageSize 分页size
     * @param entityList 实体列表
     * @param excludeFieldNames 排除的字段
     * @throws Exception
     */
    void mergeListPager(int pageSize, Collection<?> entityList, String... excludeFieldNames) throws Exception;

    /**
     * 根据主键批量合并更新（兼容动态入参）
     * @param entitys 实体列表
     * @throws Exception
     */
    void mergeList(Object... entitys) throws Exception;

    /**
     * 根据主键批量合并更新（兼容动态入参）
     * @param pageSize 分页size
     * @param entitys 实体列表
     * @throws Exception
     */
    void mergeListPager(int pageSize, Object... entitys) throws Exception;

}