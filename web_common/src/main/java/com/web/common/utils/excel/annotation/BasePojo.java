package com.web.common.utils.excel.annotation;

import com.web.common.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BasePojo {
    public boolean validate() throws Exception {

        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            if(field.isAnnotationPresent(ParamsRequired.class)) {
                ParamsRequired paramsRequired = field.getAnnotation(ParamsRequired.class);
                if(paramsRequired.requrie()) {
                    // 如果类型是String
                    if ("class java.lang.String".equals(field.getGenericType().toString())) { // 如果type是类类型，则前面包含"class "，后面跟类名
                        // 拿到该属性的gettet方法
                        /**
                         * 这里需要说明一下：他是根据拼凑的字符来找你写的getter方法的
                         * 在Boolean值的时候是isXXX（默认使用ide生成getter的都是isXXX）
                         * 如果出现NoSuchMethod异常 就说明它找不到那个gettet方法 需要做个规范
                         */
                        Method m = this.getClass().getMethod(
                                "get" + getMethodName(field.getName()));

                        String val = (String) m.invoke(this);// 调用getter方法获取属性值
                        if(StringUtils.isEmpty(val)) {
                            throw new Exception(field.getName() + " 不能为空!");
                        } else if (val != null) {
                            System.out.println("String type:" + val);
                        }

                    } else if ("class java.lang.Integer".equals(field.getGenericType().toString())) { // 如果type是类类型，则前面包含"class "，后面跟类名
                        Method m = this.getClass().getMethod(
                                "get" + getMethodName(field.getName()));

                        Integer val = (Integer) m.invoke(this);// 调用getter方法获取属性值
                        if (val != null) {
                            System.out.println("String type:" + val);
                        }

                    }
                }

            }
        }
        return  true;
    }

    /**
     * 把一个字符串的第一个字母大写、效率是最高的、
     */
    private String getMethodName(String fildeName) throws Exception{
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }
}
