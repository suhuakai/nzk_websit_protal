package com.web.core.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Sql工具类
 *
 * @author
 * @version 1.0
 */
public class SqlUtils {

    /**
     * 正则模糊匹配特殊字符转义（regexp_like）
     *
     * @param paramValue 值
     * @return String
     * @author
     */
    public static String replaceForRegexpLike(String paramValue) {
        String newParamValue = null;
        if (paramValue == null) {
            return null;
        } else if (StringUtils.isNotBlank(paramValue)) {
            StringBuffer string = new StringBuffer();
            char[] chars = paramValue.toCharArray();
            for (char c : chars) {
                char[] arr = {'^', '$', '.', '?', '+', 92, '*', '|', '(', ')', '[', ']', '{', '}', '\\', ',', '-'};
                if (ArrayUtils.contains(arr, c)) {
                    string.append("\\").append(c);
                    continue;
                } else {
                    string.append(c);
                    continue;
                }
            }
            newParamValue = string.toString();
        } else {
            newParamValue = paramValue;
        }
        return newParamValue.replaceAll("\\s+", ".*");
    }

    /**
     * 模糊匹配特殊字符转义（mysql：like）
     *
     * @param paramValue 值
     * @return String
     * @author
     */
    public static String replaceForLike(String paramValue) {
        paramValue = StringUtils.trimToNull(paramValue);
        if (paramValue == null) {
            return null;
        }
        return paramValue.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_").replaceAll("\\s+", "%");
    }

    /**
     * 去掉空格字符
     * @param params
     * @return
     */
    public static String trimReplace(String params) {
        if (params == null) {
            return null;
        }
        String str = params.replaceAll("\\s+", " ");
        return str;
    }

    /**
     * 获取汉字串拼音首字母，英文字符不变  
     *
     * @param chinese 汉字串  
     * @return 汉语拼音首字母
     */
    public static String cn2FirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (_t != null) {
                        pybf.append(_t[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim().toUpperCase();
    }
    /**
     * 获取汉字串拼音，英文字符不变  
     *
     * @param chinese 汉字串  
     * @return 汉语拼音
     */
    public static String cn2Spell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString();
    }
   /* public static void main(String[] args)
    {
        String cnStr = "柳松";
        System.out.println(cn2FirstSpell(cnStr));
        System.out.println(cn2Spell(cnStr));
    }*/

}
