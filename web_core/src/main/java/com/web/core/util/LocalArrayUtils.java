package com.web.core.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

/**
 * 本地数组工具
 * @author
 * @version 1.0
 */
public class LocalArrayUtils {

    /**
     * 列表数据逐页进行处理
     * @param entityList 数据列表
     * @param pageSize 每页记录数量
     * @param processer 数据处理过程
     * @return void
     * @throws Exception
     * @author
     *
     */
    public static void pagingProcess(Object[] entityList, int pageSize, IProcesser<Object[]> processer) throws Exception {
        LocalAssert.intGreaterEqual(pageSize, 1, "pageSize，必须大于0");
        LocalAssert.notNull(processer, "processer，不能为空");
        if (entityList != null && entityList.length > 0) {
            int count = entityList.length;
            if (count <= pageSize) {
                //数据列表处理
                processer.process(entityList);
            } else {
                int totalPage = (count % pageSize == 0 ? count / pageSize : count / pageSize + 1);//总页数
                for (int pageNum = 1; pageNum <= totalPage; pageNum++) {
                    int start = (pageNum - 1) * pageSize;//起
                    int end = start + pageSize - 1;//止
                    if (end >= count) {
                        end = count - 1;
                    }
                    Object[] array = new Object[end - start + 1];
                    for (int i = start, index = 0; i <= end; i++, index++) {
                        array[index] = entityList[i];
                    }
                    //数据列表处理
                    processer.process(array);
                }
            }
        }
    }

    /**
     * 从类弄列表中过滤出指定范围的类型
     * @param source 源类型集合
     * @param supports 过滤范围
     * @return Class[]
     * @author
     *      */
    public static Class[] filterSupports(Class[] source, Class[] supports) {
        Class[] array = new Class[0];
        if (ArrayUtils.isNotEmpty(source)) {
            for (Class clazz : source) {
                if (ArrayUtils.isNotEmpty(supports) && ArrayUtils.contains(supports, clazz)) {
                    Class[] _array = new Class[array.length + 1];
                    System.arraycopy(array, 0, _array, 0, array.length);
                    _array[_array.length - 1] = clazz;
                    array = _array;
                }
            }
        }
        return array;
    }

    /**
     * 从注解列表中过滤出指定范围的注解
     * @param sourceAnnotations 源注解集合
     * @param supportsAnnotations 过滤范围
     * @return 注解列表
     * @author
     *      */
    private static Annotation[] filterAnnotations(Annotation[] sourceAnnotations, Class<Annotation>[] supportsAnnotations) {
        Annotation[] annos = new Annotation[0];
        if (ArrayUtils.isNotEmpty(sourceAnnotations)) {
            for (Annotation anno : sourceAnnotations) {
                if (ArrayUtils.isNotEmpty(supportsAnnotations) && ArrayUtils.contains(supportsAnnotations, anno.annotationType())) {
                    Annotation[] _annos = new Annotation[annos.length + 1];
                    System.arraycopy(annos, 0, _annos, 0, annos.length);
                    _annos[_annos.length - 1] = anno;
                    annos = _annos;
                }
            }
        }
        return annos;
    }

    /**
     * 判断 数组至少有非空元素（(非空元素：不是空白串，不是空列表，不是空数组，不是null）
     * @param array 字符串数组
     * @author
     *      */
    public static boolean notEmptyAny(Object[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return false;
        }
        boolean hasNotEmplty = false;
        for (Object value : array) {
            if ((value instanceof CharSequence) && StringUtils.isNotEmpty(value.toString())) {
                hasNotEmplty = true;
                break;
            } else if ((value instanceof Collection) && ((Collection) value).size() > 0) {
                hasNotEmplty = true;
                break;
            } else if (value != null && value.getClass().isArray() && ArrayUtils.getLength(value) > 0) {
                hasNotEmplty = true;
                break;
            } else if (!(value instanceof CharSequence) && value != null) {
                hasNotEmplty = true;
                break;
            }
        }
        return hasNotEmplty;
    }

    /**
     * 判断 数组至少有一个非空元素(非空元素：不是空白串，不是空列表，不是空数组，不是null)
     * @param array 字符串数组
     * @author
     *      */
    public static boolean notBlankAny(Object[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return false;
        }
        boolean hasNotBlank = false;
        for (Object value : array) {
            if ((value instanceof CharSequence) && StringUtils.isNotBlank(value.toString())) {
                hasNotBlank = true;
                break;
            } else if ((value instanceof Collection) && ((Collection) value).size() > 0) {
                hasNotBlank = true;
                break;
            } else if (value != null && value.getClass().isArray() && ArrayUtils.getLength(value) > 0) {
                hasNotBlank = true;
                break;
            } else if (!(value instanceof CharSequence) && value != null) {
                hasNotBlank = true;
                break;
            }
        }
        return hasNotBlank;
    }

    /**
     * 判断 数组里全部元素非空（元素不是空白串，不是空列表，不是空数组，不是null）
     * @param array 字符串数组
     * @author
     *      */
    public static boolean notBlankAll(Object[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return false;
        }
        boolean hasBlank = false;
        for (Object value : array) {
            if ((value instanceof CharSequence) && StringUtils.isBlank(value.toString())) {
                hasBlank = true;
                break;
            } else if ((value instanceof Collection) && ((Collection) value).size() == 0) {
                hasBlank = true;
                break;
            } else if (value != null && value.getClass().isArray() && ArrayUtils.getLength(value) == 0) {
                hasBlank = true;
                break;
            }  else if (!(value instanceof CharSequence) && value == null) {
                hasBlank = true;
                break;
            }
        }
        return hasBlank;
    }

    public static void main(String[] args) throws Exception {
        pagingProcess(
            new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "I"}, 2, new IProcesser<Object[]>() {
            @Override
            public void process(Object[] data) throws Exception {
                System.out.println(Arrays.toString(data));
            }
        });
    }

}
