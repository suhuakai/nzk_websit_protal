package com.web.core.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基本 Service
 * @author
 * @see com.baomidou.mybatisplus.extension.service.IService
 * @since
 */
public interface BaseService<T> {

	/**
	 * 插入一条记录（选择字段，策略插入）
	 * @param entity 实体对象
	 */
	boolean insert(T entity);

	/**
	 * 插入（批量）
	 * @param entityList 实体对象集合
	 */
	default boolean insertBatch(Collection<T> entityList) {
		return insertBatch(entityList, 1000);
	}

	/**
	 * 插入（批量）
	 * @param entityList 实体对象集合
	 * @param batchSize 插入批次数量
	 */
	boolean insertBatch(Collection<T> entityList, int batchSize);

	/**
	 * 批量修改插入
	 * @param entityList 实体对象集合
	 */
	default boolean mergeBatch(Collection<T> entityList) {
		return mergeBatch(entityList, 1000);
	}

	/**
	 * 批量修改插入
	 * @param entityList 实体对象集合
	 * @param batchSize 每次的数量
	 */
	boolean mergeBatch(Collection<T> entityList, int batchSize);

	/**
	 * 根据 ID 删除
	 * @param id 主键ID
	 */
	boolean deleteById(Serializable id);

	/**
	 * 根据 columnMap 条件，删除记录
	 * @param columnMap 表字段 map 对象
	 */
	boolean removeByMap(Map<String, Object> columnMap);

	/**
	 * 根据 entity 条件，删除记录
	 * @param queryWrapper 实体包装类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	boolean remove(Wrapper<T> queryWrapper);

	/**
	 * 删除（根据ID 批量删除）
	 * @param idList 主键ID列表
	 */
	boolean removeByIds(Collection<? extends Serializable> idList);

	/**
	 * 根据 ID 选择修改
	 * @param entity 实体对象
	 */
	boolean updateById(T entity);

	/**
	 * 根据 whereEntity 条件，更新记录
	 * @param entity 实体对象
	 * @param updateWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
	 */
	boolean update(T entity, Wrapper<T> updateWrapper);

	/**
	 * 根据ID 批量更新
	 * @param entityList 实体对象集合
	 */
	default boolean updateBatchById(Collection<T> entityList) {
		return updateBatchById(entityList, 1000);
	}

	/**
	 * 根据ID 批量更新
	 * @param entityList 实体对象集合
	 * @param batchSize 更新批次数量
	 */
	boolean updateBatchById(Collection<T> entityList, int batchSize);

	/**
	 * TableId 注解存在更新记录，否插入一条记录
	 * @param entity 实体对象
	 */
	boolean merge(T entity);

	/**
	 * 根据 ID 查询
	 * @param id 主键ID
	 */
	T selectById(Serializable id);

	/**
	 * 查询（根据ID 批量查询）
	 * @param idList 主键ID列表
	 */
	Collection<T> selectBatchIds(Collection<? extends Serializable> idList);

	/**
	 * 查询（根据 columnMap 条件）
	 * @param columnMap 表字段 map 对象
	 */
	Collection<T> selectByMap(Map<String, Object> columnMap);

	/**
	 * 根据 Wrapper，查询一条记录
	 * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	default T selectOne(Wrapper<T> queryWrapper) {
		return selectOne(queryWrapper, false);
	}

	/**
	 * 根据 Wrapper，查询一条记录
	 * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 * @param throwEx 有多个 result 是否抛出异常
	 */
	T selectOne(Wrapper<T> queryWrapper, boolean throwEx);

	/**
	 * 根据 Wrapper，查询一条记录
	 * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	Map<String, Object> selectMaps(Wrapper<T> queryWrapper);

	/**
	 * 根据 Wrapper，查询一条记录
	 * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	default Object getObj(Wrapper<T> queryWrapper) {
		return SqlHelper.getObject(selectObjs(queryWrapper));
	}

	/**
	 * 根据 Wrapper 条件，查询总记录数
	 * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	int selectCount(Wrapper<T> queryWrapper);

	/**
	 * 查询总记录数
	 * @see Wrappers#emptyWrapper()
	 */
	default int selectCount() {
		return selectCount(Wrappers.<T>emptyWrapper());
	}

	/**
	 * 查询列表
	 * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	List<T> selectList(Wrapper<T> queryWrapper);

	/**
	 * 查询所有
	 * @see Wrappers#emptyWrapper()
	 */
	default List<T> selectList() {
		return selectList(Wrappers.<T>emptyWrapper());
	}

	/**
	 * 翻页查询
	 * @param page 翻页对象
	 * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	IPage<T> selectPagable(IPage<T> page, Wrapper<T> queryWrapper);

	/**
	 * 无条件翻页查询
	 * @param page 翻页对象
	 * @see Wrappers#emptyWrapper()
	 */
	default IPage<T> selectPagable(IPage<T> page) {
		return selectPagable(page, Wrappers.<T>emptyWrapper());
	}

	/**
	 * 查询列表
	 * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	List<Map<String, Object>> selectListMaps(Wrapper<T> queryWrapper);

	/**
	 * 查询所有列表
	 * @see Wrappers#emptyWrapper()
	 */
	default List<Map<String, Object>> selectListMaps() {
		return selectListMaps(Wrappers.<T>emptyWrapper());
	}

	/**
	 * 根据 Wrapper 条件，查询全部记录
	 * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	List<Object> selectObjs(Wrapper<T> queryWrapper);

	/**
	 * 查询全部记录
	 * @see Wrappers#emptyWrapper()
	 */
	default List<Object> selectObjs() {
		return selectObjs(Wrappers.<T>emptyWrapper());
	}

	/**
	 * 翻页查询
	 * @param page 翻页对象
	 * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
	 */
	IPage<Map<String, Object>> selectMapsPage(IPage<T> page, Wrapper<T> queryWrapper);

	/**
	 * 无条件翻页查询
	 * @param page 翻页对象
	 * @see Wrappers#emptyWrapper()
	 */
	default IPage<Map<String, Object>> selectMapsPage(IPage<T> page) {
		return selectMapsPage(page, Wrappers.<T>emptyWrapper());
	}
}
