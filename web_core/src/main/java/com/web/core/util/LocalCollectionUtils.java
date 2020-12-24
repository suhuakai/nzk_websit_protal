package com.web.core.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 集合处理工具类（内部扩展）
 * @author
 * @version 1.0
 */
public final class LocalCollectionUtils {

    /**
     * 去掉两个集合中相同的元素<p/>
     * @param originList 原始集合
     * @param comparedList 被比较的集合
     * @return Set 被删除元素的集合
     */
    public static <E1, E2> Set<E1> removeEqualElements(Set<E1> originList, Set<E2> comparedList) {
        HashSet deleteElements = null;
        if (CollectionUtils.isNotEmpty(originList) && CollectionUtils.isNotEmpty(comparedList)) {
            //遍历查找，找出删除项、添加项
            Iterator<E1> it1 = originList.iterator();
            while (it1.hasNext()) {
                E1 e1 = it1.next();
                Iterator<E2> it2 = comparedList.iterator();
                while (it2.hasNext()) {
                    E2 e2 = it2.next();
                    if (e1.equals(e2)) {
                        it1.remove();
                        it2.remove();
                        (deleteElements = deleteElements == null ? new HashSet<E1>() : deleteElements).add(e1);
                    }
                }
            }
        }
        return deleteElements;
    }

    /**
     * 数组转成已去重的列表
     * @param array 数组
     * @return Set
     * @author
     */
    public static <E> HashSet<E> arrayToSet(E... array) {
        return new HashSet(Arrays.asList(array));
    }

	/*public static HashSet<T> arrayToSet(T... elements){
		if(elements!=null) {
			HashSet<T> set = new HashSet<T>();
			for(T e: elements){
				set.add(e);
			}
		}
		return null;
	}*/

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
    public static <T> void pagingProcess(Collection<T> entityList, int pageSize, IProcesser<Collection<T>> processer) throws Exception {
        LocalAssert.intGreaterEqual(pageSize, 1, "pageSize，必须大于0");
        LocalAssert.notNull(processer, "processer，不能为空");
        if (entityList != null && !entityList.isEmpty()) {
            int count = entityList.size();
            if (count <= pageSize) {
                //数据列表处理
                processer.process(entityList);
            } else {
                Iterator<T> iterator = entityList.iterator();
                List<T> subList = new ArrayList<T>(pageSize);
                int currentIndex = 1;
                while (iterator.hasNext()) {
                    T entity = iterator.next();
                    subList.add(entity);
                    if (currentIndex % pageSize == 0 || currentIndex == count) {
                        //数据列表处理
                        processer.process(subList);
                        subList.clear();
                    }
                    currentIndex++;
                }
            }
        }
    }

    /**
     * 将集合元素串化并返回新列表
     * @param collection
     * @return Collection<E>
     * @author
     *
     */
    public static <E> List<String> toStringList(Collection<E> collection) {
        if (collection == null) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        if (collection.isEmpty()) {
            return list;
        }

        Iterator<E> ite = collection.iterator();
        while (ite.hasNext()) {
            E e = ite.next();
            list.add(e != null ? e.toString() : null);
        }
        return list;
    }

    /**
     * 向集合中添加非空元素
     * @param collection 集合
     * @param element 元素
     * @param <E>
     * @return Collection<E>
     * @author
     *      */
    public static <E> Collection<E> addIfNotNull(Collection<E> collection, E element) {
        if (element != null) {
            collection.add(element);
        }
        return collection;
    }

    /**
     * 向集合中添加非空元素
     * @param collection 集合
     * @param element 元素
     * @return Collection<String>
     * @author
     *      */
    public static Collection<String> addIfNotEmpty(Collection<String> collection, String element) {
        if (StringUtils.isNotEmpty(element)) {
            collection.add(element);
        }
        return collection;
    }

    /**
     * 如果条件为真，向集合中添加元素
     * @param <E>
     * @param trueOrFalse 布尔条件
     * @param collection 集合
     * @param element 要添加的元素
     * @return Collection<E>
     * @author
     *      */
    public static <E> Collection<E> addIfTrue(boolean trueOrFalse, Collection<E> collection, E element) {
        if (trueOrFalse) {
            collection.add(element);
        }
        return collection;
    }

    /**
     * 列表转哈希表
     * @param list
     * @param keyName
     * @param valueName
     * @return Map
     * @author
     *      */
    public static <K, V> Map<K, V> listToMap(List<?> list, String keyName, String valueName) throws Exception {
        Map<K, V> mapper = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object object : list) {
                K key = null;
                V value = null;
                if(object instanceof Map) {
                    key = (K) ((Map) object).get(keyName);
                    value = (V) ((Map) object).get(valueName);
                }else{
                    Field keyField = LocalBeanUtils.getDeclaredField(object.getClass(), keyName, true);
                    if(keyField!=null){
                        key = (K) keyField.get(object);
                    }
                    Field valueField = LocalBeanUtils.getDeclaredField(object.getClass(), valueName, true);
                    if(valueField!=null){
                        value = (V) valueField.get(object);
                    }
                }
                LocalAssert.notNull(key, "找不到指定的属性“{}”!", keyName);
                mapper.put(key, value);
            }
        }
        return mapper;
    }

    /**
     * 列表转哈希表
     * @param list
     * @param keyName
     * @return Map
     * @author
     *      */
    public static <K, V> Map<K, V> listToMap(List<?> list, String keyName) throws Exception {
        Map<K, V> mapper = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object object : list) {
                K key = null;
                if(object instanceof Map) {
                    key = (K) ((Map) object).get(keyName);
                }else{
                    Field field = LocalBeanUtils.getDeclaredField(object.getClass(), keyName, true);
                    if(field!=null){
                        key = (K) field.get(object);
                    }
                }
                LocalAssert.notNull(key, "找不到指定的属性“{}”!", keyName);
                mapper.put(key, (V) object);
            }
        }
        return mapper;
    }

    public static void main(String[] args) throws Exception {
        /*Set<String> originList = arrayToSet("a", "bb", "bb", "c");
        Set<String> comparedList = arrayToSet("11", "bb", "bb", "c", "f");
        System.out.println(removeEqualElements(originList, comparedList));
        System.out.println(originList);
        System.out.println(comparedList);*/

        /*Set<String> set = new HashSet<>();
        set.add("11");
        set.add("22");
        set.add("33");
        pagingProcess(set, 5, subList -> System.out.println(subList));*/

        /*List<String> list = new ArrayList<>();
        list.add("111");
        list.add("222");
        list.add("333");
        pagingProcess(list, 5, subList -> System.out.println(subList));*/

        Map<String, Object> entity = new LinkedHashMap<>();
        entity.put("userId", 100001);
        entity.put("name", "张三");
        Person person = new Person();
        person.setUserId("100002");
        person.setName("乾隆");
        List<Object> list = new ArrayList<>(2);
        list.add(entity);
        list.add(person);
        System.out.println(listToMap(list, "userId", "name"));
        System.out.println(listToMap(list, "userId"));

    }

}

class IdAware {
    protected String userId;
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}

class Person extends IdAware {
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Person{" + "userId='" + userId + '\'' + ", name='" + name + '\'' + '}';
    }
}
