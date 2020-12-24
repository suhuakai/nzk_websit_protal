package com.web.core.service.impl;

import com.web.core.dao.MysqlMapper;
import com.web.core.service.MysqlService;
import com.web.core.dao.BasicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * mysql数据库常用操作
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Service
public class MysqlServiceImpl extends CommonServiceImpl implements MysqlService {

    @Autowired
    MysqlMapper mysqlMapper;

    @Override
    public void insertListSqlAware(List<BasicMapper.InsertMeta> insertMetas) {
        //批量插入操作
        mysqlMapper.insertList(insertMetas);
    }

    @Override
    public void mergeSqlAware(BasicMapper.MergeMeta mergeMeta, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames) {
        //根据主键批量合并更新
        mysqlMapper.merge(mergeMeta, insertExcludeFieldNames, updateExcludeFieldNames);
    }

    @Override
    public void mergeListSqlAware(List<BasicMapper.MergeMeta> mergeMetas, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames) {
        //根据主键批量合并更新
        mysqlMapper.mergeList(mergeMetas, insertExcludeFieldNames, updateExcludeFieldNames);
    }

}
