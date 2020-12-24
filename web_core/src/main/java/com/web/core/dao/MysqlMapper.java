package com.web.core.dao;

import com.web.core.dao.BasicMapper.InsertMeta;
import com.web.core.dao.BasicMapper.MergeMeta;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * mysql数据库公用操作（差异化特性）
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
public interface MysqlMapper {

    /**
     * 批量插入操作
     * @param insertMetas 插入操作元信息
     * @param excludeFieldNames 排除的字段
     * @return int
     * @author
     *      */
    int insertList(@Param("insertMetas") List<InsertMeta> insertMetas,
                   @Param("excludeFieldNames") String... excludeFieldNames);

    /**
     * 根据主键批量合并更新
     * @param meta 合并操作元信息
     * @param insertExcludeFieldNames insert排除的字段
     * @param updateExcludeFieldNames update排除的字段
     * @return int
     * @author
     *      */
    int merge(@Param("meta") MergeMeta meta,
              @Param("insertExcludeFieldNames") String[] insertExcludeFieldNames,
              @Param("updateExcludeFieldNames") String[] updateExcludeFieldNames);

    /**
     * 根据主键批量合并更新
     * @param mergeMetas 合并操作元信息
     * @param insertExcludeFieldNames insert排除的字段
     * @param updateExcludeFieldNames update排除的字段
     * @return int
     * @author
     *      */
    void mergeList(@Param("mergeMetas") List<MergeMeta> mergeMetas,
                  @Param("insertExcludeFieldNames") String[] insertExcludeFieldNames,
                  @Param("updateExcludeFieldNames") String[] updateExcludeFieldNames);

}
