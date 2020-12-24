package com.web.core.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.web.core.converter.LocalStringConverter;
import com.web.core.dao.BasicMapper;
import com.web.core.service.CommonService;
import com.web.core.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Clob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 实体对象（ORM）基本操作
 * @author
 * @version 1.0
 */
public abstract class CommonServiceImpl implements CommonService {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    protected BasicMapper basicMapper;

    /**
     * 实体类元信息缓存
     */
    private Map<Class, BasicMapper.EntityMeta> entityMetaMap = new HashMap<>();

    @PostConstruct
    void init() {
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPatterns(new String[]{
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd",
                "yyyy-MM-dd HH:mm:ss.SSS"
        });
        ConvertUtils.register(dateConverter, Date.class);
        ConvertUtils.deregister(StringConverter.class);
        ConvertUtils.register(new LocalStringConverter(), String.class);
    }

    /**
     * 新增
     * @param entity 实体对象
     * @throws Exception
     * @author
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> int insert(T entity) throws Exception {
        LocalAssert.notNull(entity, "entity，不能为空");
        BasicMapper.InsertMeta insertMeta = insertMetaWrapper(entity, true);
        int num = basicMapper.insert(insertMeta.getTableName(), insertMeta.getColumnNames(), insertMeta.getValueMappings());
        logger.trace("表{}: 成功插入{}条记录", insertMeta.getTableName(), num);
        return num;
    }

    /**
     * 封装Insert操作的元信息
     * @param entity 实体
     * @return ignoreNoneValue
     * @throws Exception
     * @author
     *      */
    protected <T> BasicMapper.InsertMeta insertMetaWrapper(T entity, boolean ignoreNoneValue) throws Exception {
        BasicMapper.EntityMeta entityMeta = entityMetaWrapper(entity.getClass());//封装:实体对象（ORM）元信息

        List<String> columnNames = new ArrayList<String>();
        List<BasicMapper.FieldMapping> valueMappings = new ArrayList<BasicMapper.FieldMapping>();
        for (ResultMapping rm : entityMeta.getResultMappings()) {
            //封装一个字段属性关系映射信息
            BasicMapper.FieldMapping fm = fieldMappingWrapper(entity, rm, ignoreNoneValue);
            if (fm != null) {
                columnNames.add(fm.getColumn());
                valueMappings.add(fm);
            }
        }
        LocalAssert.notEmpty(columnNames, "插入操作：columnNames，不能为空");
        LocalAssert.notEmpty(valueMappings, "插入操作：valueMappings，不能为空");
        return new BasicMapper.InsertMeta(entityMeta.getTableName(), columnNames, valueMappings);
    }

    /**
     * 封装Merge操作的元信息
     * @param entity 实体
     * @return MergeMeta
     * @throws Exception
     * @author
     *      */
    protected <T> BasicMapper.MergeMeta mergeMetaWrapper(T entity) throws Exception {
        BasicMapper.EntityMeta entityMeta = entityMetaWrapper(entity.getClass());//封装:实体对象（ORM）元信息

        List<BasicMapper.FieldMapping> valueMappings = new ArrayList<BasicMapper.FieldMapping>();
        List<String> idColumnNames = new ArrayList<String>();
        List<String> updateColumnNames = new ArrayList<String>();

        //主键列
        List<ResultMapping> idResultMappings = entityMeta.getIdResultMappings();
        LocalAssert.notEmpty(idResultMappings, "merge操作，" + entityMeta.getTableName() + "必需主键列");
        for (ResultMapping rm : idResultMappings) {
            idColumnNames.add(rm.getColumn());
        }

        for (ResultMapping rm : entityMeta.getResultMappings()) {
            //封装一个字段属性关系映射信息
            BasicMapper.FieldMapping fm = fieldMappingWrapper(entity, rm, false);
            if (fm != null) {
                valueMappings.add(fm);
                boolean wetherIdColumn = false;
                for (String idColumn : idColumnNames) {
                    if (fm.getColumn().equalsIgnoreCase(idColumn)) {
                        wetherIdColumn = true;
                    }
                }
                if (!wetherIdColumn) {
                    updateColumnNames.add(fm.getColumn());
                }
            }
        }

        LocalAssert.notEmpty(valueMappings, "合并操作：valueMappings，不能为空");
        LocalAssert.notEmpty(idColumnNames, "合并操作：idColumnNames，不能为空");
        LocalAssert.notEmpty(updateColumnNames, "合并操作：updateColumnNames，不能为空");
        return new BasicMapper.MergeMeta(entityMeta.getTableName(), valueMappings, idColumnNames, updateColumnNames);
    }

    /**
     * 根据条件修改（全部字段更新）
     * @param entity 对象
     * @param condition 条件
     * @throws Exception
     * @author
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> int updateCover(T entity, T condition) throws Exception {
        return updateProcessing(entity, condition, false);
    }

    /**
     * 根据条件修改（非空项更新）
     * @param entity 信息
     * @param condition 条件
     * @throws Exception
     * @author
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> int update(T entity, T condition) throws Exception {
        return updateProcessing(entity, condition, true);
    }

    /**
     * 按主键更新对象（全部字段更新）
     * @param entity 实体
     * @throws Exception
     * @author
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> int updateCoverById(T entity) throws Exception {
        return updateByIdProcessing(entity, false);
    }

    /**
     * 按主键更新对象（非空项更新）
     * @param entity 实体对象
     * @throws Exception
     * @author
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> int updateById(T entity) throws Exception {
        return updateByIdProcessing(entity, true);
    }

    /**
     * 按主键删除一个对象
     * @param clazz 类型
     * @param id 主键值
     * @throws Exception
     * @author
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> int deleteById(Class<T> clazz, Serializable id) throws Exception {
        LocalAssert.notNull(id, "id，不能为空");

        T condition = copyIdProperties(clazz.newInstance(), id);//设置主键属性的值
        BasicMapper.EntityMeta entityMeta = entityMetaWrapper(condition.getClass());//封装:实体对象（ORM）元信息

        List<BasicMapper.FieldMapping> conditions = new ArrayList<BasicMapper.FieldMapping>();
        //条件字段值列表
        for (ResultMapping rm : entityMeta.getIdResultMappings()) {
            BasicMapper.FieldMapping fm = fieldMappingWrapper(id, rm, true);
            if (fm != null) {
                conditions.add(fm);
            }
        }
        Assert.notEmpty(conditions, "删除操作（根据id）：删除条件，不能为空! ");
        int num = basicMapper.delete(entityMeta.getTableName(), conditions);
        if (num > 0) {
            logger.trace("表" + entityMeta.getTableName() + ": 成功删除" + num + "条记录");
        }
        return num;
    }

    /**
     * 删除: 按照指定条件
     * @param condition 条件
     * @throws Exception
     * @author
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> int delete(T condition) throws Exception {
        LocalAssert.notNull(condition, "condition，不能为空");
        BasicMapper.EntityMeta entityMeta = entityMetaWrapper(condition.getClass());//封装:实体对象（ORM）元信息

        List<BasicMapper.FieldMapping> conditions = new ArrayList<BasicMapper.FieldMapping>();
        //条件字段值列表
        for (ResultMapping rm : entityMeta.getResultMappings()) {
            BasicMapper.FieldMapping fm = fieldMappingWrapper(condition, rm, true);
            if (fm != null) {
                conditions.add(fm);
            }
        }
        Assert.notEmpty(conditions, "删除操作：删除条件，不能为空");
        int num = basicMapper.delete(entityMeta.getTableName(), conditions);
        if (num > 0) {
            logger.trace("表" + entityMeta.getTableName() + ": 成功删除" + num + "条记录");
        }
        return num;
    }

    /**
     * 根据id查询一个对象
     * @param clazz 类型
     * @param id id
     * @throws Exception
     * @author
     *
     */
    @Override
    public <T> T findById(Class<T> clazz, Serializable id) throws Exception {
        LocalAssert.notNull(id, "id，不能为空");
        T condition = copyIdProperties(clazz.newInstance(), id);//设置主键属性的值
        return find(condition);
    }

    /**
     * 根据条件查找一个对象
     * @param condition 条件
     * @throws Exception
     * @author
     *
     */
    @Override
    public <T> T find(T condition) throws Exception {
        List<T> entityList = queryProcessing(condition);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        } else if (entityList.size() > 1) {
            throw new TooManyResultsException("查询结果不唯一，请检查数据");
        }
        return entityList.get(0);
    }

    /**
     * 根据条件查找列表
     * @param condition 条件
     * @throws Exception
     * @author
     *
     */
    @Override
    public <T> List<T> queryList(T condition) throws Exception {
        return queryProcessing(condition);
    }

    /**
     * 更新处理过程
     * @param entity
     * @param condition
     * @param ignoreNoneValue
     * @param <T>
     * @return int
     * @throws Exception
     * @author
     *      */
    private <T> int updateProcessing(T entity, T condition, Boolean ignoreNoneValue) throws Exception {
        LocalAssert.notNull(entity, "entity，不能为空");
        BasicMapper.EntityMeta entityMeta = entityMetaWrapper(entity.getClass());//封装:实体对象（ORM）元信息

        List<BasicMapper.FieldMapping> valueMappings = new ArrayList<BasicMapper.FieldMapping>();
        List<BasicMapper.FieldMapping> conditions = new ArrayList<BasicMapper.FieldMapping>();
        //新字段值列表
        for (ResultMapping rm : entityMeta.getResultMappings()) {
            BasicMapper.FieldMapping fm = fieldMappingWrapper(entity, rm, ignoreNoneValue);
            if (fm != null) {
                valueMappings.add(fm);
            }
        }
        //条件字段值列表
        for (ResultMapping rm : entityMeta.getResultMappings()) {
            BasicMapper.FieldMapping fm = fieldMappingWrapper(condition, rm, true);
            if (fm != null) {
                conditions.add(fm);
            }
        }
        Assert.notEmpty(conditions, "更新操作：更新条件，不能为空");
        return basicMapper.update(entityMeta.getTableName(), valueMappings, conditions);
    }

    /**
     * 根据id更新处理过程
     * @param entity
     * @param ignoreNoneValue
     * @param <T>
     * @return int
     * @throws Exception
     * @author
     *      */
    private <T> int updateByIdProcessing(T entity, Boolean ignoreNoneValue) throws Exception {
        LocalAssert.notNull(entity, "entity，不能为空");
        BasicMapper.EntityMeta entityMeta = entityMetaWrapper(entity.getClass());//封装:实体对象（ORM）元信息
        Assert.notEmpty(entityMeta.getIdResultMappings(), "实体必须指明主键属性！");

        List<BasicMapper.FieldMapping> valueMappings = new ArrayList<BasicMapper.FieldMapping>();
        List<BasicMapper.FieldMapping> conditions = new ArrayList<BasicMapper.FieldMapping>();
        //新字段值列表
        for (ResultMapping rm : entityMeta.getResultMappings()) {
            BasicMapper.FieldMapping fm = fieldMappingWrapper(entity, rm, ignoreNoneValue);
            if (fm != null) {
                valueMappings.add(fm);
            }
        }
        //条件字段值列表
        for (ResultMapping rm : entityMeta.getIdResultMappings()) {
            BasicMapper.FieldMapping fm = fieldMappingWrapper(entity, rm, true);
            if (fm != null) {
                conditions.add(fm);
            }
        }
        Assert.notEmpty(conditions, "更新操作（根据id）：更新条件，不能为空");
        return basicMapper.update(entityMeta.getTableName(), valueMappings, conditions);
    }

    /**
     * 查询处理过程
     * @param condition
     * @param <T>
     * @return int
     * @throws Exception
     * @author
     *      */
    private <T> List<T> queryProcessing(T condition) throws Exception {
        BasicMapper.EntityMeta entityMeta = entityMetaWrapper(condition.getClass());//封装:实体对象（ORM）元信息

        List<String> columnNames = new ArrayList<String>();
        List<BasicMapper.FieldMapping> conditions = new ArrayList<BasicMapper.FieldMapping>();
        //字段名列表
        for (ResultMapping rm : entityMeta.getResultMappings()) {
            columnNames.add(rm.getColumn());
        }
        //条件字段值列表
        for (ResultMapping rm : entityMeta.getResultMappings()) {
            BasicMapper.FieldMapping fm = fieldMappingWrapper(condition, rm, true);
            if (fm != null) {
                conditions.add(fm);
            }
        }

        LocalAssert.notEmpty(entityMeta.getTableName(), "查询操作：tableName，不能为空");
        LocalAssert.notEmpty(columnNames, "查询操作：columnNames，不能为空");
        LocalAssert.notEmpty(conditions, "查询操作：查询条件，不能为空");
        //封装查询结果
        List<Map<String, Object>> rsList = basicMapper.query(entityMeta.getTableName(), columnNames, conditions);
        List<T> newList = new ArrayList<T>();
        if (CollectionUtils.isNotEmpty(rsList)) {
            for (Map<String, Object> record : rsList) {
                T entity = (T) entityMeta.getEntityClazz().newInstance();
                for (ResultMapping rm : entityMeta.getResultMappings()) {
                    Object columnValue = record.get(rm.getColumn());
                    if (columnValue != null) {
                        BeanUtils.setProperty(entity, rm.getProperty(), columnValue);
                    }
                }
                newList.add(entity);
            }
        }
        return newList;
    }

    /**
     * 封装:一个字段属性关系映射信息
     * @param entity 实体对象
     * @param rm 字段属性映射信息
     * @param ignoreNoneValue 是否忽略空值（true:忽略，false不忽略）
     * @return FieldMapping
     * @throws Exception
     * @author
     *
     */
    protected <T> BasicMapper.FieldMapping fieldMappingWrapper(T entity, ResultMapping rm, Boolean ignoreNoneValue) throws Exception {
        Field property = LocalBeanUtils.getDeclaredField(entity.getClass(), rm.getProperty(), true);
        LocalAssert.notNull(property, "{}实体类: 没有发现“{}”属性", entity.getClass().getSimpleName(), rm.getProperty());

        property.setAccessible(true);
        Object value = property.get(entity);
        if (ignoreNoneValue && value == null) {
            return null;
        } else if (ignoreNoneValue && (value instanceof CharSequence) && StringUtils.isEmpty(value.toString())) {
            return null;
        }

        BasicMapper.FieldMapping fm = new BasicMapper.FieldMapping();
        fm.setColumn(rm.getColumn());
        fm.setProperty(rm.getProperty());
        fm.setValue(value);
        fm.setJavaType(rm.getJavaType());
        fm.setJdbcType(rm.getJdbcType() != null ? rm.getJdbcType() : resolveJdbcTypeFromJavaType(rm.getJavaType()));
        fm.setJdbcTypeName(fm.getJdbcType().toString());
        return fm;
    }

    /**
     * JavaType到JdbcType映射
     * @param javaType
     * @author
     *      */
    private JdbcType resolveJdbcTypeFromJavaType(Class<?> javaType) {
        if (javaType == null) {
            return JdbcType.NULL;
        } else if (CharSequence.class.isAssignableFrom(javaType)) {
            return JdbcType.VARCHAR;
        } else if (Number.class.isAssignableFrom(javaType)) {
            return JdbcType.DECIMAL;
        } else if (LocalDateTime.class.isAssignableFrom(javaType)) {
            return JdbcType.TIMESTAMP;
        } else if (LocalDate.class.isAssignableFrom(javaType)) {
            return JdbcType.DATE;
        } else if (Date.class.isAssignableFrom(javaType)) {
            return JdbcType.TIMESTAMP;
        } else if (javaType.isPrimitive()) {
            return JdbcType.DECIMAL;
        } else if (javaType == byte[].class) {
            return JdbcType.BINARY;
        } else if (Blob.class.isAssignableFrom(javaType)) {
            return JdbcType.BLOB;
        } else if (Clob.class.isAssignableFrom(javaType)) {
            return JdbcType.CLOB;
        }
        return JdbcType.VARCHAR;
    }

    /**
     * 封装:实体对象（ORM）元信息
     * @param entityClass 实体类
     * @return EntityMeta    实体对象（ORM）元信息
     * @throws Exception
     * @author
     *
     */
    protected <T> BasicMapper.EntityMeta entityMetaWrapper(Class<T> entityClass) throws Exception {
        BasicMapper.EntityMeta entityMetaInfo = entityMetaMap.get(entityClass);
        if (entityMetaInfo != null) {
            return entityMetaInfo;
        }

        String tableName = null;
        String resultMapId = null;
        ResultMap resultMap = null;
        List<ResultMapping> resultMappings = null;
        List<ResultMapping> idResultMappings = null;

        TableName annotation = AnnotationUtils.findAnnotation(entityClass, TableName.class);
        String entityClassName = entityClass.getSimpleName();
        Assert.notNull(annotation, "实体类" + entityClassName + "：必须添加注解@TableName");

        tableName = annotation.value();
        resultMapId = annotation.resultMap();
        LocalAssert.notBlank(tableName, "实体类" + entityClassName + " => @TableName: 请指明“表名”！");
        //LocalAssert.notBlank(resultMapId, "实体类" + entityClassName + " => @TableName：请指明“resultMap”！");

        //默认orm结果映射id
        if (StringUtils.isBlank(resultMapId)) {
            resultMapId = entityClass.getCanonicalName().replace(".entity.", ".dao.") + "Mapper.BaseResultMap";
        }

        resultMap = sqlSessionFactory.getConfiguration().getResultMap(resultMapId);
        resultMappings = resultMap.getResultMappings();
        idResultMappings = resultMap.getIdResultMappings();
        Assert.notEmpty(resultMappings, "实体类" + entityClassName + " => 属性字段映射（resultMap），不能为空");

        BasicMapper.EntityMeta entityMeta = new BasicMapper.EntityMeta();
        entityMeta.setEntityClazz(entityClass);
        entityMeta.setEntityName(entityClassName);
        entityMeta.setTableName(tableName);
        entityMeta.setResultMapId(resultMapId);
        entityMeta.setResultMap(resultMap);
        entityMeta.setResultMappings(resultMappings);
        entityMeta.setIdResultMappings(idResultMappings);
        entityMetaMap.put(entityClass, entityMeta);//缓存实体元信息
        logger.info("***************缓存实体元信息｛{}｝***************", entityClassName);
        return entityMeta;
    }

    /**
     * 设置主键属性的值
     * @param condition 条件对象
     * @param id id
     * @return T
     * @throws Exception
     * @author
     *
     */
    protected <T> T copyIdProperties(T condition, Serializable id) throws Exception {
        Assert.notNull(id, "id，不能为空");
        BasicMapper.EntityMeta entityMeta = entityMetaWrapper(condition.getClass());//封装:实体对象（ORM）元信息

        List<ResultMapping> idResultMappings = entityMeta.getIdResultMappings();
        Assert.notEmpty(idResultMappings, "请指明: 实体主键属性映射（" + entityMeta.getEntityName() + "）！");
        if (idResultMappings.size() == 1) {
            //单键
            String idProperty = idResultMappings.get(0).getProperty();
            Field idField = LocalBeanUtils.getDeclaredField(condition.getClass(), idProperty, true);
            Assert.notNull(idField, entityMeta.getEntityName() + "实体类: 没有发现“" + idProperty + "”属性");
            idField.set(condition, id);//设置实体属性的值
            LocalAssert.notNull(BeanUtils.getProperty(condition, idProperty), "{}实体类: 主键属性“{}”的值，不能为空！", condition.getClass().getSimpleName(), idProperty);
        } else {
            //复合主键
            for (ResultMapping rm : idResultMappings) {
                String idProperty = rm.getProperty();
                Field idField = LocalBeanUtils.getDeclaredField(condition.getClass(), idProperty, true);
                Assert.notNull(idField, entityMeta.getEntityName() + "实体类: 没有发现“" + idProperty + "”属性");
                idField.set(condition, BeanUtils.getProperty(id, idProperty));//设置实体属性的值
                LocalAssert.notNull(BeanUtils.getProperty(condition, idProperty), "{}实体类: 复合主键属性“{}”的值，不能为空！", condition.getClass().getSimpleName(), idProperty);
            }
        }
        return condition;
    }

    /**
     * 批量插入操作
     * @param entityList 实体列表
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertList(Collection<?> entityList) throws Exception {
        if (logger.isTraceEnabled()) {
            LocalStringUtils.printLogPretty(logger, entityList);
        }
        LocalAssert.notEmpty(entityList, "entityList，不能为空");
        List<BasicMapper.InsertMeta> insertMetas = null;
        for (Object entity : entityList) {
            BasicMapper.InsertMeta insertMeta = insertMetaWrapper(entity, false);
            (insertMetas = insertMetas == null ? new ArrayList<BasicMapper.InsertMeta>() : insertMetas).add(insertMeta);
        }
        LocalAssert.notEmpty(insertMetas, "插入操作：insertMetas，不能为空");
        //if (logger.isTraceEnabled()) logger.trace("insertMetas = {}", JSONUtils.toJsonLoosely(insertMetas));
        this.insertListSqlAware(insertMetas);
    }

    /**
     * 批量插入操作
     * @param insertMetas 插入操作元信息
     * @return int
     * @author
     *      */
    protected abstract void insertListSqlAware(List<BasicMapper.InsertMeta> insertMetas);

    /**
     * 批量插入操作（兼容动态入参）
     * @param entitys 实体列表
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertList(Object... entitys) throws Exception {
        LocalAssert.notNull(entitys, "entitys，不能为空");
        List entityList = new ArrayList();
        for (Object arg : entitys) {
            if (arg != null) {
                if (arg instanceof Collection) {
                    entityList.addAll((Collection<Object>) arg);
                } else if (arg.getClass().isArray()) {
                    entityList.addAll(Arrays.asList((Object[]) arg));
                } else {
                    entityList.add(arg);
                }
            }
        }
        LocalAssert.notNull(entityList, "entityList，不能为空");
        //根据主键批量合并更新
        this.insertList(entityList);
    }

    /**
     * 根据主键批量合并更新
     * @param entity 实体
     * @param excludeFieldNames 排除的字段
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void merge(T entity, String... excludeFieldNames) throws Exception {
        LocalAssert.notNull(entity, "entity，不能为空");
        this.merge(entity, excludeFieldNames, excludeFieldNames);
    }

    /**
     * 根据主键批量合并更新
     * @param entity 实体
     * @param insertExcludeFieldNames insert排除的字段
     * @param updateExcludeFieldNames update排除的字段
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void merge(T entity, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames) throws Exception {
        LocalAssert.notNull(entity, "entity，不能为空");
        this.mergeSqlAware(mergeMetaWrapper(entity), insertExcludeFieldNames, updateExcludeFieldNames);
    }

    /**
     * 根据主键批量合并更新，适配sql语句
     * @param mergeMeta 合并操作元信息
     * @param insertExcludeFieldNames insert排除的字段
     * @param updateExcludeFieldNames update排除的字段
     * @return int
     * @author
     *      */
    protected abstract void mergeSqlAware(BasicMapper.MergeMeta mergeMeta, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames);

    /**
     * 根据主键批量合并更新
     * @param entityList 实体列表
     * @param excludeFieldNames 排除的字段
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergeList(Collection<?> entityList, String... excludeFieldNames) throws Exception {
        this.mergeList(entityList, excludeFieldNames, excludeFieldNames);
    }

    /**
     * 根据主键批量合并更新
     * @param entityList 实体列表
     * @param insertExcludeFieldNames insert排除的字段
     * @param updateExcludeFieldNames update排除的字段
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergeList(Collection<?> entityList, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames) throws Exception {
        if (logger.isTraceEnabled()) {
            LocalStringUtils.printLogPretty(logger, entityList);
        }
        LocalAssert.notEmpty(entityList, "entityList，不能为空");
        List<BasicMapper.MergeMeta> mergeMetas = null;
        for (Object entity : entityList) {
            if (entity != null) {
                BasicMapper.MergeMeta mergeMeta = mergeMetaWrapper(entity);
                (mergeMetas = mergeMetas == null ? new ArrayList<BasicMapper.MergeMeta>() : mergeMetas).add(mergeMeta);
            }
        }
        LocalAssert.notEmpty(mergeMetas, "mergeMetas，不能为空");
        //if (logger.isTraceEnabled()) logger.trace("mergeMetas = {}, excludeFieldNames={}", JSONUtils.toJsonLoosely(mergeMetas), Arrays.toString(excludeFieldNames));
        this.mergeListSqlAware(mergeMetas, insertExcludeFieldNames, updateExcludeFieldNames);
    }

    /**
     * 根据主键批量合并更新，适配sql语句
     * @param mergeMetas 合并操作元信息
     * @param insertExcludeFieldNames insert排除的字段
     * @param updateExcludeFieldNames update排除的字段
     * @return int
     * @author
     *      */
    protected abstract void mergeListSqlAware(List<BasicMapper.MergeMeta> mergeMetas, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames);

    /**
     * 根据主键批量合并更新（兼容动态入参）
     * @param entitys 实体列表
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergeList(Object... entitys) throws Exception {
        LocalAssert.notNull(entitys, "entitys，不能为空");

        List entityList = new ArrayList();
        for (Object obj : entitys) {
            if (obj != null) {
                if (obj instanceof Collection) {
                    entityList.addAll((Collection<Object>) obj);
                } else if (obj.getClass().isArray()) {
                    entityList.addAll(Arrays.asList((Object[]) obj));
                } else {
                    entityList.add(obj);
                }
            }
        }
        LocalAssert.notNull(entityList, "entityList，不能为空");
        //根据主键批量合并更新
        this.mergeList(entityList);
    }

    /**
     * 批量插入操作（分页模式）
     * @param entityList 实体列表
     * @throws Exception
     * @author
     *      */
    @Override
    public void insertListPager(int pageSize, Collection<?> entityList) throws Exception {
        LocalCollectionUtils.pagingProcess(entityList, pageSize, subList -> {
            this.insertList(subList);
        });
    }

    /**
     * 批量插入操作（分页模式）
     * @param entitys 实体列表
     * @throws Exception
     * @author
     *      */
    @Override
    public void insertListPager(int pageSize, Object... entitys) throws Exception {
        LocalArrayUtils.pagingProcess(entitys, pageSize, subList -> {
            this.insertList(subList);
        });
    }

    /**
     * 根据主键批量合并更新（分页模式）
     * @param entityList 实体列表
     * @param insertExcludeFieldNames insert排除的字段
     * @param updateExcludeFieldNames update排除的字段
     * @throws Exception
     * @author
     *      */
    @Override
    public void mergeListPager(int pageSize, Collection<?> entityList, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames) throws Exception {
        LocalCollectionUtils.pagingProcess(entityList, pageSize, subList -> {
            this.mergeList(subList, insertExcludeFieldNames, updateExcludeFieldNames);
        });
    }

    /**
     * 根据主键批量合并更新（分页模式）
     * @param entityList 实体列表
     * @param excludeFieldNames 排除的字段
     * @throws Exception
     * @author
     *      */
    @Override
    public void mergeListPager(int pageSize, Collection<?> entityList, String... excludeFieldNames) throws Exception {
        LocalCollectionUtils.pagingProcess(entityList, pageSize, subList -> {
            this.mergeList(subList, excludeFieldNames);
        });
    }

    /**
     * 根据主键批量合并更新（分页模式）
     * @param entitys 实体列表
     * @throws Exception
     * @author
     *      */
    @Override
    public void mergeListPager(int pageSize, Object... entitys) throws Exception {
        LocalArrayUtils.pagingProcess(entitys, pageSize, subList -> {
            this.mergeList(subList);
        });
    }

}