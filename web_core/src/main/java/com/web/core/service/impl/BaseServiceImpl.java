package com.web.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.web.core.service.BaseService;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * IService 实现类（ 泛型：M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）
 * @author
 * @see com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 * @since
 */
@SuppressWarnings("unchecked")

public class BaseServiceImpl<M extends BaseMapper<T>, T> implements BaseService<T> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected M baseMapper;

	protected Class<T> currentModelClass() {
		return ReflectionKit.getSuperClassGenericType(getClass(), 1);
	}

	/**
	 * 批量操作 SqlSession
	 */
	protected SqlSession openSqlSessionBatch() {
		return SqlHelper.sqlSessionBatch(currentModelClass());
	}

	/**
	 * 释放sqlSession
	 * @param sqlSession session
	 */
	protected void closeSqlSession(SqlSession sqlSession) {
		SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(currentModelClass()));
	}

	/**
	 * 获取SqlStatement
	 * @param sqlMethod
	 * @return
	 */
	protected String getSqlStatement(SqlMethod sqlMethod) {
		return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean insert(T entity) {
		return SqlHelper.retBool(baseMapper.insert(entity));
	}

	/**
	 * 批量插入
	 * @param entityList
	 * @param batchSize
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean insertBatch(Collection<T> entityList, int batchSize) {
		int i = 0;
		String sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE);
		try (SqlSession batchSqlSession = openSqlSessionBatch()) {
			for (T anEntityList : entityList) {
				batchSqlSession.insert(sqlStatement, anEntityList);
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
					logger.info("insertBatch批量插入：第{}条", i);
				}
				i++;
			}
			batchSqlSession.flushStatements();
		}
		return true;
	}

	/**
	 * TableId 注解存在更新记录，否插入一条记录
	 * @param entity 实体对象
	 * @return boolean
	 * savaOrUpdate
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean 	merge(T entity) {
		if (null != entity) {
			Class<?> cls = entity.getClass();
			TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
			if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
				Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
				return StringUtils.checkValNull(idVal) || Objects.isNull(selectById((Serializable) idVal)) ? insert(entity) : updateById(entity);
			} else {
				throw ExceptionUtils.mpe("Error:  Can not execute. Could not find @TableId.");
			}
		}
		return false;
	}
	//批量更新
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean mergeBatch(Collection<T> entityList, int batchSize) {
		if (CollectionUtils.isEmpty(entityList)) {
			throw new IllegalArgumentException("Error: entityList must not be empty");
		}
		Class<?> cls = currentModelClass();
		TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
		int i = 0;
		try (SqlSession batchSqlSession = openSqlSessionBatch()) {
			for (T anEntityList : entityList) {
				if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
					Object idVal = ReflectionKit.getMethodValue(cls, anEntityList, tableInfo.getKeyProperty());
					if (StringUtils.checkValNull(idVal) || Objects.isNull(selectById((Serializable) idVal))) {
						batchSqlSession.insert(getSqlStatement(SqlMethod.INSERT_ONE), anEntityList);
					} else {
						MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
						param.put(Constants.ENTITY, anEntityList);
						batchSqlSession.update(getSqlStatement(SqlMethod.UPDATE_BY_ID), param);
					}
					//不知道以后会不会有人说更新失败了还要执行插入 😂😂😂
					if (i >= 1 && i % batchSize == 0) {
						batchSqlSession.flushStatements();
					}
					i++;
				} else {
					throw ExceptionUtils.mpe("Error:  Can not execute. Could not find @TableId.");
				}
				batchSqlSession.flushStatements();
			}
		}
		return true;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean deleteById(Serializable id) {
		return SqlHelper.delBool(baseMapper.deleteById(id));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean removeByMap(Map<String, Object> columnMap) {
		if (ObjectUtils.isEmpty(columnMap)) {
			throw ExceptionUtils.mpe("removeByMap columnMap is empty.");
		}
		return SqlHelper.delBool(baseMapper.deleteByMap(columnMap));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean remove(Wrapper<T> wrapper) {
		return SqlHelper.delBool(baseMapper.delete(wrapper));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		return SqlHelper.delBool(baseMapper.deleteBatchIds(idList));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateById(T entity) {
		return SqlHelper.retBool(baseMapper.updateById(entity));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean update(T entity, Wrapper<T> updateWrapper) {
		return SqlHelper.retBool(baseMapper.update(entity, updateWrapper));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateBatchById(Collection<T> entityList, int batchSize) {
		if (CollectionUtils.isEmpty(entityList)) {
			throw new IllegalArgumentException("Error: entityList must not be empty");
		}
		int i = 0;
		String sqlStatement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
		try (SqlSession batchSqlSession = openSqlSessionBatch()) {
			for (T anEntityList : entityList) {
				MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
				param.put(Constants.ENTITY, anEntityList);
				batchSqlSession.update(sqlStatement, param);
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
				i++;
			}
			batchSqlSession.flushStatements();
		}
		return true;
	}

	@Override
	public T selectById(Serializable id) {
		return baseMapper.selectById(id);
	}

	@Override
	public Collection<T> selectBatchIds(Collection<? extends Serializable> idList) {
		return baseMapper.selectBatchIds(idList);
	}

	@Override
	public Collection<T> selectByMap(Map<String, Object> columnMap) {
		return baseMapper.selectByMap(columnMap);
	}

	@Override
	public T selectOne(Wrapper<T> queryWrapper, boolean throwEx) {
		if (throwEx) {
			return baseMapper.selectOne(queryWrapper);
		}
		return SqlHelper.getObject(baseMapper.selectList(queryWrapper));
	}

	@Override
	public Map<String, Object> selectMaps(Wrapper<T> queryWrapper) {
		return SqlHelper.getObject(baseMapper.selectMaps(queryWrapper));
	}

	@Override
	public int selectCount(Wrapper<T> queryWrapper) {
		return SqlHelper.retCount(baseMapper.selectCount(queryWrapper));
	}

	@Override
	public List<T> selectList(Wrapper<T> queryWrapper) {
		return baseMapper.selectList(queryWrapper);
	}

	@Override
	public IPage<T> selectPagable(IPage<T> page, Wrapper<T> queryWrapper) {
		return baseMapper.selectPage(page, queryWrapper);
	}

	@Override
	public List<Map<String, Object>> selectListMaps(Wrapper<T> queryWrapper) {
		return baseMapper.selectMaps(queryWrapper);
	}

	@Override
	public List<Object> selectObjs(Wrapper<T> queryWrapper) {
		return baseMapper.selectObjs(queryWrapper).stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Override
	public IPage<Map<String, Object>> selectMapsPage(IPage<T> page, Wrapper<T> queryWrapper) {
		return baseMapper.selectMapsPage(page, queryWrapper);
	}

}