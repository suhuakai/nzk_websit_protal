package com.web.core.util;

/**
 * 数据处理抽象方法
 * @author
 * @version 1.0
 */
public interface IProcesser<T> {

    /**
     * 数据处理
     * @param data
     * @return Object
     * @throws Exception
     */
    void process(T data) throws Exception;

}
