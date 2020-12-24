package com.web.core.util;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.reflect.FieldUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Bean工具类
 * @author
 * @version 1.0
 */
public class LocalBeanUtils {

    /**
     * 获取实体的指定属性
     * @param clazz
     * @param fieldName
     * @return Field
     * @author
     * @author
     */
    public static Field getDeclaredField(Class<?> clazz, String fieldName, boolean forceAccess) {
        LocalAssert.notNull(clazz, "被查找的类，不能为空");
        LocalAssert.notNull(fieldName, "查找的属性名，不能为空");
        Field field = null;
        for (Class<?> superClass = clazz; field == null && superClass != Object.class; superClass = superClass.getSuperclass()) {
            field = FieldUtils.getDeclaredField(superClass, fieldName, forceAccess);
        }
        return field;
    }

    /**
     * 获取实体的指定属性的Setter方法
     * @param clazz
     * @param fieldName
     * @return Method
     * @throws Exception
     * @author
     * @author
     */
    public static Method getSetterMethodByField(Class<?> clazz, String fieldName, boolean forceAccess) throws Exception {
        LocalAssert.notNull(clazz, "被查找的类，不能为空");
        LocalAssert.notNull(fieldName, "查找的属性名，不能为空");
        Method setterMethod = null;
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            // 获取BeanInfo对象
            BeanInfo beanInfo = Introspector.getBeanInfo(superClass);
            // 通过BeanInfo对象获取所有的属性描述器
            PropertyDescriptor[] propertyDescriptor = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor prop : propertyDescriptor) {
                if (prop.getName().equals(fieldName)) {
                    // 通过属性描述器，获取写方法
                    setterMethod = prop.getWriteMethod();
                }
            }
        }
        if (setterMethod != null && forceAccess) {
            setterMethod.setAccessible(true);
        }
        return setterMethod;
    }

    /**
     * 获取实体的指定属性的Setter方法
     * @param clazz
     * @param fieldName
     * @return Method
     * @throws Exception
     * @author
     * @author
     */
    public static Method getGetterMethodByField(Class<?> clazz, String fieldName, boolean forceAccess) throws Exception {
        LocalAssert.notNull(clazz, "被查找的类，不能为空");
        LocalAssert.notNull(fieldName, "查找的属性名，不能为空");
        Method getterMethod = null;
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            // 获取BeanInfo对象
            BeanInfo beanInfo = Introspector.getBeanInfo(superClass);
            // 通过BeanInfo对象获取所有的属性描述器
            PropertyDescriptor[] propertyDescriptor = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor prop : propertyDescriptor) {
                if (prop.getName().equals(fieldName)) {
                    // 通过属性描述器，获取写方法
                    getterMethod = prop.getReadMethod();
                }
            }
        }
        if (getterMethod != null && forceAccess) {
            getterMethod.setAccessible(true);
        }
        return getterMethod;
    }

    /**
     * 设置对象的属性值
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    public static void setProperty(Object bean, String name, Object value) throws Exception {
        Method setterMethod = getSetterMethodByField(bean.getClass(), name, true);
        setterMethod.invoke(bean, ConvertUtils.convert(value, setterMethod.getParameterTypes()[0]));
    }

	/*public static void main(String[] args) throws Exception {

		System.out.println(getDeclaredField(Teacher.class, "id", true));
		System.out.println(getDeclaredField(Teacher.class, "name", true));
		System.out.println(getDeclaredField(Teacher.class, "sex", true));
		System.out.println(getDeclaredField(Teacher.class, "job", true));

		System.out.println(getSetterMethodByField(Teacher.class, "id"));
		System.out.println(getSetterMethodByField(Teacher.class, "name"));
		System.out.println(getSetterMethodByField(Teacher.class, "sex"));
		System.out.println(getSetterMethodByField(Teacher.class, "job"));
	}*/

}