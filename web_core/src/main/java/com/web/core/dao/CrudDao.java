/**
 */
package com.web.core.dao;

import java.util.List;
import java.util.Map;

/**
 * DAO支持类实现
 *
 * @param <T>
 * @author
 */
public interface CrudDao<T> {


    T get(String id);

    /**
     * 获取单条数据
     *
     * @param entity
     * @return
     */
    T get(T entity);

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     *
     * @param entity
     * @return
     */
    List<T> findList(T entity);

    /**
     * 查询所有数据列表
     *
     * @param entity
     * @return
     */
    List<T> findAllList(T entity);

    /**
     * 查询所有数据列表
     *
     * @return
     * @see List<T> findAllList(T entity)
     */
    @Deprecated
    List<T> findAllList();

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    int update(T entity);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     *
     * @param id
     * @return
     * @see int delete(T entity)
     */
    int delete(String id);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     *
     * @param entity
     * @return
     */
    int delete(T entity);


    /**
     * 批量插入数据
     *
     * @param list
     * @return
     */
    int batchInsert(List<T> list);

    /**
     * 批量修改数据
     *
     * @param list
     * @return
     */
    int batchUpdate(List<T> list);

    /**
     * 批量获取数据
     *
     * @param map
     * @return
     */
    List<T> batchSelect(Map<String, Object> map);


}