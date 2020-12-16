package com.web.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.core.constant.SystemConst;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页信息实体
 * @author
 * @version 1.0
 */
public class Pager<E> implements Serializable {

    /**
     * 总记录数
     */
    @JsonProperty("count")
    private Integer count =0;

    /**
     * 总页数
     */
    @JsonProperty("totalPage")
    private Integer totalPage;

    /**
     * 每页记录数量（默认每页15条）
     */
    @JsonIgnore
    private Integer pageSize = 15;

    /**
     * 当前页码（从1开始）
     */
    @JsonIgnore
    private Integer pageNum = 1;

    /**
     * 查询结果集(列表)
     */
    private List<E> rows;

    /**
     * 开始行号
     */
    @JsonIgnore
    private Integer startIndex;

    /**
     * 结束行号
     */
    @JsonIgnore
    private Integer endIndex;

    /**
     * 分页计算就位标识
     */
    @JsonIgnore
    private boolean doneEval = Boolean.FALSE;

    /**
     * 自动分页开关（默认自动）
     */
    @JsonIgnore
    private boolean isAutoPagable = Boolean.TRUE;

    /**
     * 是否自动统计总记录数（默认自动）
     */
    @JsonIgnore
    private boolean isAutoCount = Boolean.TRUE;

    /**
     * 分页模式，默认0
     */
    @JsonIgnore
    private int paginateMode = SystemConst.PaginateMode.DEFAULT;

    /**
     * 查询参数
     */
    @JsonIgnore
    private Map<String, Object> conditiions;

    /**
     * 额外的响应数据项
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("extra")
    private Map<String, Object> extras;

    public Pager() {
        super();
        this.eval();
    }

    public Pager(boolean isAutoPagable) {
        this();
        this.isAutoPagable = isAutoPagable;
        if (!isAutoPagable) {
            this.isAutoCount = Boolean.FALSE;
        }
    }

    public Pager(boolean isAutoPagable, boolean isAutoCount) {
        this(isAutoPagable);
        this.isAutoCount = isAutoCount;
    }

    public Integer getCount() {
        return count;
    }

    public Pager<E> setCount(Integer count) {
        this.count = count;
        this.eval();
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Pager<E> setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;//每页记录数默认值
        }
        this.pageSize = pageSize;
        this.eval();
        return this;
    }

    /**
     * 设置每页记录数量（允许指定默认值）
     * @param pageSize
     * @param defaultPageSize
     * @return void
     * @author
     *
     */
    public Pager<E> setPageSize(Integer pageSize, Integer defaultPageSize) {
        if (pageSize == null || pageSize <= 0) {
            pageSize = defaultPageSize == null ? 10 : defaultPageSize;//每页记录数默认值
        }
        this.pageSize = pageSize;
        this.eval();
        return this;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public Pager<E> setPageNum(Integer pageNum) {
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;//页码默认值
        }
        this.pageNum = pageNum;
        this.eval();
        return this;
    }

    public void eval() {
        if (this.pageSize != null && this.pageNum != null) {
            this.startIndex = (pageNum - 1) * pageSize + 1;
            this.endIndex = (pageNum - 1) * pageSize + pageSize;
            this.doneEval = Boolean.TRUE;//已计算开始行索引与结束行索引
        }
        if (this.count != null && this.pageSize != null) {
            this.totalPage = (this.count % this.pageSize == 0) ? this.count / this.pageSize : this.count / this.pageSize + 1;
        }
    }

    public List<E> getRows() {
        return rows != null ? rows : new ArrayList<E>();
    }

    public Pager<E> setRows(List<E> rows) {
        this.rows = rows;
        return this;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public Pager<E> setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
        return this;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public Pager<E> setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
        return this;
    }

    public boolean isDoneEval() {
        return doneEval;
    }

    public Pager<E> setDoneEval(boolean doneEval) {
        this.doneEval = doneEval;
        return this;
    }

    public Map<String, Object> getConditiions() {
        return conditiions;
    }

    public Pager<E> setConditiions(Map<String, Object> conditiions) {
        this.conditiions = conditiions;
        return this;
    }

    public Pager<E> addExtra(String name, Object value) {
        if (this.extras == null) {
            this.extras = new HashMap<String, Object>();
        }
        this.extras.put(name, value);
        return this;
    }

    public Pager<E> addExtra(String name, Object value, Object defaultValue) {
        return this.addExtra(name, value != null ? value : defaultValue);
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public Object getExtra(String name) {
        if (extras == null || extras.isEmpty()) {
            return null;
        }
        return extras.get(name);
    }

    @JsonIgnore
    public boolean isAutoPagable() {
        return isAutoPagable;
    }

    public Pager<E> setAutoPagable(boolean autoPagable) {
        this.isAutoPagable = autoPagable;
        return this;
    }

    /**
     * 添加查询的过滤参数 <br>
     * @param paramName
     * @param paramValue
     * @return void
     */
    public Pager<E> addQueryParam(String paramName, Object paramValue) {
        if (paramValue == null || (paramValue instanceof CharSequence && "".equals(paramValue.toString().trim()))) {
            return this;
        }
        if (this.conditiions == null) {
            this.conditiions = new HashMap<String, Object>();
        }
        conditiions.put(paramName, paramValue);
        return this;
    }

    /**
     * 获取查询参数  <br>
     * @param paramName
     * @return Object
     * @author
     *  <br>
     */
    public Object getQueryParam(String paramName) {
        if (this.conditiions != null) {
            return this.conditiions.get(paramName);
        }
        return null;
    }

    @JsonIgnore
    public boolean isAutoCount() {
        return isAutoCount;
    }

    public Pager<E> setAutoCount(boolean autoCount) {
        this.isAutoCount = autoCount;
        return this;
    }

    public int getPaginateMode() {
        return paginateMode;
    }

    public Pager<E> setPaginateMode(int paginateMode) {
        this.paginateMode = paginateMode;
        return this;
    }

    public Integer getTotalPage() {
        if (totalPage == null) {
            if (count != null && pageSize != null) {
                totalPage = (this.count % this.pageSize == 0) ? this.count / this.pageSize : this.count / this.pageSize + 1;
            }
        }
        return totalPage;
    }

    public Pager<E> setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        return this;
    }
}