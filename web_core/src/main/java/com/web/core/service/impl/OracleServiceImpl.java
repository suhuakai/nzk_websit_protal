package com.web.core.service.impl;

import com.web.core.dao.OracleMapper;
import com.web.core.service.OracleService;
import com.web.core.dao.BasicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * oracle数据库常用操作
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
@Service
public class OracleServiceImpl extends CommonServiceImpl implements OracleService {

    @Autowired
    OracleMapper oracleMapper;

    @Override
    public void insertListSqlAware(List<BasicMapper.InsertMeta> insertMetas) {
        //批量插入操作
        oracleMapper.insertList(insertMetas);
    }

    @Override
    public void mergeSqlAware(BasicMapper.MergeMeta mergeMeta, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames) {
        //根据主键批量合并更新
        oracleMapper.merge(mergeMeta, insertExcludeFieldNames, updateExcludeFieldNames);
    }

    @Override
    public void mergeListSqlAware(List<BasicMapper.MergeMeta> mergeMetas, String[] insertExcludeFieldNames, String[] updateExcludeFieldNames) {
        //根据主键批量合并更新
        oracleMapper.mergeList(mergeMetas, insertExcludeFieldNames, updateExcludeFieldNames);
    }

}
