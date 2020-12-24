package com.web.core.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.cglib.beans.BeanMap;

import java.util.*;

/**
 * MybatisPlus分页实体（本地实现）
 * @author
 * @since
 */
public class Page<T> implements IPage<T> {

    /**
     * 默认每页记录数量
     */
    private static final int DEFAULT_SIZE = 15;

    /**
     * 查询数据列表
     */
    @JsonProperty("rows")
    private List<T> records = Collections.emptyList();
    /**
     * 总记录数
     */
    @JsonProperty("count")
    private long total = 0;
    /**
     * 每页显示条数，默认 15
     */
    private long size = DEFAULT_SIZE;
    /**
     * 当前页
     */
    @JsonIgnore
    private long current = 1;
    /**
     * <p>
     * SQL 排序 ASC 数组
     * </p>
     */
    @JsonIgnore
    private String[] ascs;
    /**
     * <p>
     * SQL 排序 DESC 数组
     * </p>
     */
    @JsonIgnore
    private String[] descs;
    /**
     * <p>
     * 自动优化 COUNT SQL
     * </p>
     */
    @JsonIgnore
    private boolean optimizeCountSql = true;
    /**
     * 是否进行 count 查询
     */
    @JsonIgnore
    private boolean isSearchCount = true;

    /**
     * 是否分页
     */
    @JsonIgnore
    private boolean autoPageable = true;

    /**
     * 查询参数
     */
    @JsonIgnore
    private Map<String, Object> conditiions;

    /**
     * 额外的响应数据项<p/>
     * <ul>常用场景：
     * <li>页眉</li>
     * <li>页头</li>
     * <li>页脚</li>
     * </ul>
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("extra")
    private Map<String, Object> extras;

    /**
     * 分页构造函数
     */
    public Page() {
        super();
    }

    /**
     * 分页构造函数
     * @param current 当前页
     * @param size 每页显示条数
     */
    public Page(Number current, Number size) {
        this(current, size, true, true);
    }

    /**
     * 分页构造函数
     * @param isPageable 是否自动分页
     */
    public Page(boolean isPageable) {
        this(1, DEFAULT_SIZE, isPageable, true);
    }

    /**
     * 分页构造函数
     * @param current 当前页
     * @param size 每页显示条数
     * @param isPageable 是否自动分页
     * @param isSearchCount 是否自动统计总记录数
     */
    public Page(Number current, Number size, boolean isPageable) {
        this(current, size, isPageable, isPageable);
    }

    /**
     * 分页构造函数
     * @param current 当前页
     * @param size 每页显示条数
     * @param total 总记录数
     * @param isPageable 是否自动分页
     * @param isSearchCount 是否自动统计总记录数
     */
    public Page(Number current, Number size, boolean isPageable, boolean isSearchCount) {
        if (current != null && current.longValue() > 0) {
            this.current = current.longValue();
        }
        if (size != null && size.longValue() > 0) {
            this.size = size.longValue();
        }
        this.autoPageable = isPageable;
        this.isSearchCount = isSearchCount;
    }

    /**
     * <p>
     * 是否存在上一页
     * </p>
     * @return true / false
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * <p>
     * 是否存在下一页
     * </p>
     * @return true / false
     */
    public boolean hasNext() {
        return this.current < this.getPages();
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public Page<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getPages() {
        long pages = this.total / this.size;
        if (this.total % this.size != 0) {
            pages++;
        }
        return pages;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public Page<T> setTotal(Long total) {
        if (total != null && total > 0) {
            this.total = total;
        }
        return this;
    }

    @JsonIgnore
    @Override
    public long getSize() {
        return autoPageable ? this.size : -1;
    }

    @Override
    public Page<T> setSize(long size) {
        this.size = (size <= 0 ? DEFAULT_SIZE : size);
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public Page<T> setCurrent(long current) {
        this.current = (current <= 0 ? 1 : current);
        return this;
    }

    @Override
    public String[] ascs() {
        return ascs;
    }

    public Page<T> setAscs(List<String> ascs) {
        if (CollectionUtils.isNotEmpty(ascs)) {
            this.ascs = ascs.toArray(new String[0]);
        }
        return this;
    }

    /**
     * <p>
     * 升序
     * </p>
     * @param ascs 多个升序字段
     */
    public Page<T> setAsc(String... ascs) {
        this.ascs = ascs;
        return this;
    }

    @Override
    public String[] descs() {
        return descs;
    }

    public Page<T> setDescs(List<String> descs) {
        if (CollectionUtils.isNotEmpty(descs)) {
            this.descs = descs.toArray(new String[0]);
        }
        return this;
    }

    /**
     * <p>
     * 降序
     * </p>
     * @param descs 多个降序字段
     */
    public Page<T> setDesc(String... descs) {
        this.descs = descs;
        return this;
    }

    @Override
    public boolean optimizeCountSql() {
        return optimizeCountSql;
    }

    @JsonIgnore
    @Override
    public boolean isSearchCount() {
        if (total < 0) {
            return false;
        }
        return isSearchCount;
    }

    public Page<T> setSearchCount(boolean isSearchCount) {
        this.isSearchCount = isSearchCount;
        return this;
    }

    public Page<T> setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
        return this;
    }

    public Map<String, Object> getConditiions() {
        return conditiions;
    }

    public Page<T> setConditiions(Map<String, Object> conditiions) {
        this.conditiions = conditiions;
        return this;
    }

    /**
     * 添加查询的过滤参数
     * @param paramName 参数名
     * @param paramValue 参数值
     * @return void
     */
    public Page<T> addQueryParam(String paramName, Object paramValue) {
        if (paramValue == null) {
            return this;
        } else if (paramValue instanceof CharSequence && "".equals(paramValue.toString())) {
            return this;
        } else if (paramValue instanceof Collection && ((Collection) paramValue).isEmpty()) {
            return this;
        } else if (paramValue.getClass().isArray() && ArrayUtils.isEmpty((Object[]) paramValue)) {
            return this;
        } else if (paramValue instanceof Map && ((Map) paramValue).isEmpty()) {
            return this;
        }
        if (this.conditiions == null) {
            this.conditiions = new HashMap<>();
        }
        conditiions.put(paramName, paramValue);
        return this;
    }

    /**
     * 添加查询的过滤参数
     * @param paramsBean 参数对象
     * @return Page
     */
    public Page<T> addQueryParam(Object paramsBean) {
        if (paramsBean != null) {
            BeanMap map = BeanMap.create(paramsBean);
            for (Object key : map.keySet()) {
                this.addQueryParam(key.toString(), map.get(key));
            }
        }
        return this;
    }

    /**
     * 获取查询参数
     * @param paramName 参数名
     * @return Object
     * @author
     *      */
    public Object getQueryParam(String paramName) {
        if (this.conditiions != null) {
            return this.conditiions.get(paramName);
        }
        return null;
    }

    public Page<T> addExtra(String name, Object value) {
        if (this.extras == null) {
            this.extras = new HashMap<>();
        }
        this.extras.put(name, value);
        return this;
    }

    public Page<T> addExtra(String name, Object value, Object defaultValue) {
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
    public boolean isAutoPageable() {
        return autoPageable;
    }

    /**
     * 手动分页：计算当前分页偏移量
     */
    @JsonIgnore
    public long getOriginOffset() {
        return getCurrent() > 0 ? (getCurrent() - 1) * this.size : 0;
    }

    /**
     * 手动分页：获取size
     */
    @JsonProperty("size")
    public long getOriginSize() {
        return this.size;
    }

    public void setAutoPageable(boolean autoPageable) {
        this.autoPageable = autoPageable;
    }



}