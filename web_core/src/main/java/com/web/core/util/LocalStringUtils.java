package com.web.core.util;

import com.web.core.exception.ValidationException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 本地化扩展StringUtils
 * @author
 * @version 1.0
 *
 */
public final class LocalStringUtils {

    /**
     * 将两个字符trimToEmpty以后再比较是否相等
     * @param str1 串1
     * @param str2 串2
     * @return boolean
     * @author
     *
     */
    public static boolean equalsTrim(String str1, String str2) {
        String str1trim = StringUtils.trimToEmpty(str1);
        String str2trim = StringUtils.trimToEmpty(str2);
        return str1trim.equals(str2trim);
    }

    /**
     * 将两个字符trimToEmpty以后再比较是否不相等
     * @param str1 串1
     * @param str2 串2
     * @return boolean
     * @author
     *
     */
    public static boolean notEqualTrim(String str1, String str2) {
        String str1trim = StringUtils.trimToEmpty(str1);
        String str2trim = StringUtils.trimToEmpty(str2);
        return !str1trim.equals(str2trim);
    }

    /**
     * 判断入参的每个字符串，都是空
     * @param value
     * @param moreValue
     * @return boolean
     * @author
     *
     */
    public static boolean isEmptyAll(String[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return false;
        }
        for (String value : array) {
            if (StringUtils.isNotEmpty(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断入参的每个字符串，都是非空
     * @param value
     * @param moreValue
     * @return boolean
     * @author
     *
     */
    public static boolean isNotEmptyAll(String[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return false;
        }
        for (String value : array) {
            if (StringUtils.isEmpty(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断入参的每个字符串，都是非空串
     * @param value
     * @param moreValue
     * @return boolean
     * @author
     *
     */
    public static boolean isNotBlankAll(String[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return false;
        }
        for (String value : array) {
            if (StringUtils.isBlank(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串数组中是否有非空串
     * @param array
     * @return boolean
     */
    public static boolean hasNotBlank(String[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return false;
        }
        for (String value : array) {
            if (StringUtils.isNotBlank(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断 数组里全部元素为空（null、""、空白串，都是空）
     * @param strArray 字符串数组
     * @author
     *      */
    public static boolean isBlankAll(String... strArray) {
        if (ArrayUtils.isEmpty(strArray)) {
            return false;
        }
        for (String value : strArray) {
            if (StringUtils.isNotBlank(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串trimToEmpty处理
     * @param str
     * @return String
     * @author
     *
     */
    public static String trimToEmpty(CharSequence str) {
        if (str == null) {
            return "";
        }
        return StringUtils.trimToEmpty(str.toString());
    }

    /**
     * 字符串trimToEmpty处理（如果是空，返回默认值）
     * @param str
     * @param defaultValue
     * @return String
     * @author
     *
     */
    public static String trimToEmpty(CharSequence str, String defaultValue) {
        String string = trimToEmpty(str);
        return "".equals(string) ? defaultValue : string;
    }

    /**
     * 字符串trimToNull处理
     * @param str
     * @return String
     * @author
     *
     */
    public static String trimToNull(CharSequence str) {
        if (str == null) {
            return null;
        }
        return StringUtils.trimToNull(str.toString());
    }

    /**
     * 字符串trimToNull处理（如果是空，返回默认值）
     * @param str
     * @param defaultValue
     * @return String
     * @author
     *
     */
    public static String trimToNull(CharSequence str, String defaultValue) {
        String string = trimToNull(str);
        return string == null ? defaultValue : string;
    }

    /**
     * 使用指定的字符，拼接指定长度的字符串
     * @param str
     * @param count
     * @return String
     * @author
     *
     */
    public static void concatString(StringBuffer baseSequeue, String str, int count) {
        if (baseSequeue == null || str == null) {
            throw new IllegalArgumentException("入参值不能为空值!");
        }
        if (count < 0) {
            throw new IllegalArgumentException("参数count不能是负值!");
        }
        for (int i = 1; i <= count; i++) {
            baseSequeue.append(str);
        }
    }

    /**
     * 使用指定的字符，拼接指定长度的字符串
     * @param str
     * @param count
     * @return String
     * @author
     *
     */
    public static String concatString(String str, int count) {
        if (str == null) {
            throw new IllegalArgumentException("入参值不能为空值");
        }
        if (count == 0) {
            return null;
        }
        StringBuffer baseSequeue = new StringBuffer();
        concatString(baseSequeue, str, count);
        return baseSequeue.toString();
    }

    /**
     * 数组转字符串显示
     * @param array 元素集合
     * @param startFix 拼接结果：前置符
     * @param prefix 元素前置符
     * @param separator 元素分隔符
     * @param postfix 元素后置符
     * @param endFix 拼接结果：后置符
     * @param ignoreNull 是否忽略空元素
     * @author
     *      */
    public static String concatString(Object[] array, String startFix, String prefix, String postfix, String separator, String endFix, boolean ignoreNull) {
        if (array != null && array.length > 0) {
            StringBuilder baseSequeue = new StringBuilder(StringUtils.defaultString(startFix));
            int length = array.length;
            for (int i = 0; i < length; i++) {
                Object value = array[i];
                if (ignoreNull && value == null) {
                    continue;
                }
                baseSequeue.append(StringUtils.defaultString(prefix)).append(value).append(StringUtils.defaultString(postfix));
                if (i < length - 1) {
                    baseSequeue.append(separator);
                }
            }
            return baseSequeue.append(StringUtils.defaultString(endFix)).toString();
        }
        return null;
    }

    /**
     * 数组转字符串显示
     * @param array 元素集合
     * @param startFix 拼接结果：前置符
     * @param separator 元素分隔符
     * @param endFix 拼接结果：后置符
     * @param ignoreNull 是否忽略空元素
     * @author
     *      */
    public static String concatString(Object[] array, String startFix, String separator, String endFix, boolean ignoreNull) {
        return concatString(array, startFix, null, separator, null, endFix, ignoreNull);
    }

    /**
     * 数组转字符串显示
     * @param collection 元素集合
     * @param startFix 拼接结果：前置符
     * @param prefix 元素前置符
     * @param separator 元素分隔符
     * @param postfix 元素后置符
     * @param endFix 拼接结果：后置符
     * @param ignoreNull 是否忽略空元素
     * @author
     *      */
    public static String concatString(Collection<?> collection, String startFix, String prefix, String separator, String postfix, String endFix, boolean ignoreNull) {
        if (collection != null && !collection.isEmpty()) {
            StringBuilder baseSequeue = new StringBuilder(StringUtils.defaultString(startFix));
            Iterator<?> iterator = collection.iterator();
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if (ignoreNull && object == null) {
                    continue;
                }
                baseSequeue.append(StringUtils.defaultString(prefix)).append(object).append(StringUtils.defaultString(postfix));
                if (iterator.hasNext()) {
                    baseSequeue.append(separator);
                }
            }
            return baseSequeue.append(StringUtils.defaultString(endFix)).toString();
        }
        return null;
    }

    /**
     * 数组转字符串显示
     * @param collection 元素集合
     * @param startFix 拼接结果：前置符
     * @param separator 元素分隔符
     * @param endFix 拼接结果：后置符
     * @param ignoreNull 是否忽略空元素
     * @author
     *      */
    public static String concatString(Collection<?> collection, String startFix, String separator, String endFix, boolean ignoreNull) {
        return concatString(collection, startFix, null, separator, null, endFix, ignoreNull);
    }

    /**
     * 拼接字符串<p/>
     * 如果null对象不作拼接<p/>
     * 如果结果为空就返回""空字符串<p/>
     * @param strings
     * @return Sring
     * @author
     *      */
    public static String concatString(CharSequence... strings) {
        if (strings == null || strings.length == 0) {
            return "";
        }
        StringBuilder s = new StringBuilder();
        for (CharSequence str : strings) {
            if (str != null) {
                s.append(str.toString());
            }
        }
        return s.toString();
    }

    /**
     * 全角字符串转换半角字符串<p>
     * <pre>1.半角字符是从33开始到126结束</pre>
     * <pre>2.与半角字符对应的全角字符是从65281开始到65374结束</pre>
     * <pre>3.其中半角的空格是32.对应的全角空格是12288</pre>
     * <pre>半角和全角的关系很明显,除空格外的字符偏移量是65248(65281-33 = 65248)</pre>
     * @param inputString 源字符串
     * @return String            半角字符串
     * @author
     *
     */
    public static String toHalfWidth(String inputString) {
        if (null == inputString) {
            return null;
        }
        if (inputString.length() == 0) {
            return "";
        }
        char[] charArray = inputString.toCharArray();
        //对全角字符转换的char数组遍历
        for (int i = 0; i < charArray.length; ++i) {
            int charValue = (int) charArray[i];
            //如果符合转换关系,将对应下标之间减掉偏移量65248;如果是空格的话,直接做转换
            if (charValue >= 65281 && charValue <= 65374) {
                charArray[i] = (char) (charValue - 65248);
            } else if (charValue == 12288) {//全角空格
                charArray[i] = (char) 32;//半角空格
            }
        }
        return new String(charArray);
    }

    /**
     * 半角字符串转全角字符串<p>
     * <pre>1.半角字符是从33开始到126结束</pre>
     * <pre>2.与半角字符对应的全角字符是从65281开始到65374结束</pre>
     * <pre>3.其中半角的空格是32.对应的全角空格是12288</pre>
     * <pre>半角和全角的关系很明显,除空格外的字符偏移量是65248(65281-33 = 65248)</pre>
     * @param inputString 源字符串
     * @return String            全角字符串
     * @author
     *
     */
    public static String toFullWidth(String inputString) {
        if (null == inputString) {
            return null;
        }
        if (inputString.length() == 0) {
            return "";
        }
        char charArray[] = inputString.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int charValue = (int) charArray[i];
            if (charValue == 32) {//半角空格
                charArray[i] = 12288;//全角空格
            } else if (charValue > 32 && charValue < 127) {
                charArray[i] = (char) (charArray[i] + 65248);
            }
        }
        return new String(charArray);
    }

    /**
     * 字符串切割生成列表
     * @param string
     * @param splitToken
     * @return List<String>
     * @throws ValidationException
     * @author
     *
     */
    public static List<String> splitToList(String string, String splitToken) throws ValidationException {
        LocalAssert.notEmpty(string, "sequeue，参数不能空!");
        LocalAssert.notEmpty(splitToken, "splitToken，参数不能空!");
        String[] array = string.split(splitToken);
        if (array != null && array.length > 0) {
            List<String> list = new ArrayList<String>();
            for (String e : array) {
                list.add(e);
            }
            return list;
        }
        return null;
    }

    /**
     * 驼峰命名法转下划线命名法
     * @param camelCaseName
     * @return String
     * @author
     *
     */
    public static String namedFromCamelCase(String camelCaseName) {
        if (StringUtils.isNotBlank(camelCaseName)) {
            StringBuffer sequeue = new StringBuffer();
            for (int i = 0, length = camelCaseName.length(); i < length; i++) {
                char c = camelCaseName.charAt(i);
                Boolean addSplit = (i > 0 && c >= 'A' && c <= 'Z');
                sequeue.append(addSplit ? "_" + c : c);
            }
            return sequeue.toString().toUpperCase();
        }
        return camelCaseName;
    }

    /**
     * 字符串常量约定和统一（大小写敏感）
     * @param constVal 常量值1
     * @param elseConstVal 常量值2
     * @param source 原始字面值
     * @param multiValues 多值列表
     * @return String
     * @author
     *
     */
    public static <T> T forceConst(T constVal, T elseConstVal, String source, String... multiValues) {
        Assert.notEmpty(multiValues, "multiValues，非空");
        for (String s : multiValues) {
            if (source == s || (source != null && source.equals(s))) {
                return constVal;
            }
        }
        return elseConstVal;
    }

    /**
     * 字符串常量约定和统一（忽略大小写）
     * @param constVal 常量值1
     * @param elseConstVal 常量值2
     * @param source 原始字面值
     * @param multiValues 多值列表
     * @return String
     * @author
     *
     */
    public static <T> T forceConstIgnoreCase(T constVal, T elseConstVal, String source, String... multiValues) {
        Assert.notEmpty(multiValues, "multiValues，非空");
        for (String s : multiValues) {
            if (source == s || (source != null && source.equalsIgnoreCase(s))) {
                return constVal;
            }
        }
        return elseConstVal;
    }

    /**
     * 码值翻译，不识别报错
     * @param multiValues 码值映射列表
     * @return String
     * @throws ValidationException
     * @author
     *
     */
    public static String caseWhen(String... multiValues) throws ValidationException {
        Assert.notEmpty(multiValues, "multiValues，非空");
        boolean findTarget = false;
        String target = null;

        String first = multiValues[0];
        int length = multiValues.length;
        for (int i = 1; i < length; i += 2) {
            String currentValue = multiValues[i];
            if (i == length - 1) {
                target = currentValue;
                findTarget = true;
                break;
            } else if (first == null ? currentValue == null : first.equals(currentValue)) {
                target = multiValues[i + 1];
                findTarget = true;
                break;
            }
        }
        if (findTarget) {
            return target;
        } else {
            throw new ValidationException(first + "：码值未识别！");
        }
    }

    /**
     * 打印数据日志
     * @param logger
     * @param entitys
     * @throws Exception
     */
    public static void printLogPretty(Logger logger, Object... entitys) throws Exception {
        StringBuffer print = new StringBuffer();
        if (ArrayUtils.isNotEmpty(entitys)) {
            List entityList = new ArrayList();
            for (Object arg : entitys) {
                if (arg != null) {
                    if (arg instanceof Collection) {
                        entityList.addAll((Collection<Object>) arg);
                    } else if (arg.getClass().isArray()) {
                        entityList.addAll(Arrays.asList((Object[]) arg));
                    } else {
                        entityList.add(arg);
                    }
                }
            }
            LocalAssert.notNull(entityList, "entityList，不能为空");

            if (CollectionUtils.isNotEmpty(entityList)) {
                Collections.sort(entityList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return o1.getClass().getSimpleName().compareTo(o2.getClass().getSimpleName());
                    }
                });
            }
            Object preEntity = null;
            for (Object entity : entityList) {
                StringBuffer header = null;
                StringBuffer row = new StringBuffer();
                String className = null;
                for (Map.Entry<String, String> property : BeanUtils.describe(entity).entrySet()) {
                    if (preEntity == null || !preEntity.getClass().equals(entity.getClass())) {
                        header = (header == null ? new StringBuffer() : header);
                        if (!"class".equals(property.getKey())) {
                            header.append(property.getKey()).append(" \t ");
                        }
                    }
                    if (!"class".equals(property.getKey())) {
                        row.append(property.getValue()).append("\t");
                    } else {
                        className = property.getValue().substring(property.getValue().lastIndexOf(".") + 1);
                    }
                }
                preEntity = entity;
                if (header != null) {
                    print.append("【列头说明】\t").append(header.deleteCharAt(header.length() - 2)).append("\n\r");
                }
                print.append("【").append(className).append("】\t").append(row.deleteCharAt(row.length() - 1).append("\n\r"));
            }
            logger.trace("\n----------------------- 打印列表（共{}条） -------------------\n{}", entityList.size(), print);
        }
    }

    /**
     * 判断指定字符器是否在指定范围
     * @param array 字符串数组
     * @param findString 要查找的目标字符串
     * @param isTrimToEmpty 是否忽略空串
     * @param isIgnoreCase 是否忽略大小写
     * @return boolean
     */
    public static boolean contains(String[] array, String findString, boolean isTrimToEmpty, boolean isIgnoreCase) {
        if (array == null || array.length == 0) {
            return false;
        }
        findString = isTrimToEmpty ? StringUtils.trimToEmpty(findString) : findString;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == findString) {
                return true;
            } else if (findString == null) {
                if (isTrimToEmpty ? StringUtils.trimToNull(array[i]) == null : array[i] == null) {
                    return true;
                }
            } else if (findString != null) {
                String currentString = isTrimToEmpty ? StringUtils.trimToEmpty(array[i]) : array[i];
                if (isIgnoreCase ? findString.equalsIgnoreCase(currentString) : findString.equals(array[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断指定字符器是否在指定范围
     * @param array 字符串数组
     * @param objectToFind 要查找的目标字符串
     * @param isIgnoreCase 是否严格区分大小写
     * @return boolean
     */
    public static boolean contains(String[] array, String objectToFind, boolean isIgnoreCase) {
        return contains(array, objectToFind, false, isIgnoreCase);
    }

    /**
     * 如果字符串为空，用默认值代替
     * @param value
     * @param defaultValue
     * @param optionalDefaultValues
     * @return String
     */
    public static String nvl(String value, String defaultValue, String... optionalDefaultValues) {
        if (value == null) {
            if (defaultValue != null) {
                return defaultValue;
            } else {
                if (optionalDefaultValues != null && optionalDefaultValues.length >= 0) {
                    for (String option : optionalDefaultValues) {
                        if (option != null) {
                            return option;
                        }
                    }
                }
            }
        }
        return value;
    }

    /**
     * 字符串占位符替换（占位符：{}）
     * @param string
     * @param args
     * @return String
     * @author
     */
    public static String fomart(String string, Object... args) {
        if (ArrayUtils.isNotEmpty(args) && string.matches(".*(\\{\\})+.*")) {
            StringBuffer msg = new StringBuffer();
            for (int i = 0, bracketNo = -1; i < string.length(); i++) {
                char c = string.charAt(i);
                if (c == '{' && string.charAt(i + 1) == '}') {
                    bracketNo++;
                    if (bracketNo < args.length) {
                        Object arg = args[bracketNo];
                        msg.append(arg != null ? arg.toString() : "");
                    } else {
                        throw new RuntimeException("缺少占位值！bracketNo=" + bracketNo);
                    }
                    i++;
                } else {
                    msg.append(c);
                }
            }
            if (msg.length() > 0) {
                return msg.toString();
            }
        }
        return string;
    }

    /**
     * 字符串占位符替换（占位符：{}）
     * @param code
     * @param args
     * @return String
     */
    public static String getMessageByCode(Integer code, Object... args) {
        String msg = SystemConfig.getProperty(code.toString());
        if (StringUtils.isBlank(msg)) {
            throw new RuntimeException("消息模板不存在！code=" + code);
        }
        return fomart(msg, args);
    }

    /**
     * 字符串截取
     * @param str
     * @param lastIndex
     * @return CharSequence
     * @author
     *      */
    public static String substringTrim(CharSequence str, int lastIndex) {
        str = trimToEmpty(str);
        if (!"".equals(str) && str.length() > lastIndex) {
            return str.subSequence(0, lastIndex).toString();
        }
        return str.toString();
    }

    public static void main(String[] args) throws Exception {

        System.out.println(substringTrim("123", 2));

        //System.out.println(caseWhen("abc"));
        //System.out.println(caseWhen("abc", "a", "A", "b", "B", "默认值"));
        //System.out.println(caseWhen("abc", "a", "A", "b", "B", "abc", "ABC"));
        //System.out.println(caseWhen("70", "90", "采购订单", "30", "手术订单", "70", "结算订单"));
        //System.out.println(contains(new String[]{"  ", " aB"}, "ab ", true, true));
        //System.out.println(nvl(null, "1", "2", "3", "4", "5", "6"));

        //System.out.println(fomart("{}，必须是{}位{}{}！1{}2", "发票代码", "8", "纯", "数字", null));

		/*System.out.println(LocalStringUtils.caseWhen(
				null,
				"全部", null,
				"-1", null,
				"01", "启用",
				"00", "停用",
				"默认"
		));*/
        /*System.out.println(concatString(new String[]{
                "70",
                "90",
                "采购订单",
                "30",
                "手术订单",
                "70",
                "结算订单"
        }, "[", null, null, ", ", "]", true));*/
        //System.out.println(isBlankAll(null, "", "  "));//true
        //System.out.println(isBlankAll(null, "", " 字符 "));//false
    }

}