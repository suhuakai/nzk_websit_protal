package com.web.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
public class HisLnitialUtils {

    /**
     * 得到中文首字母
     *
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    /**
     * 常规流水号
     * @return
     */
    public static String getFlowCode(){
        return System.currentTimeMillis()+"1";
    }

}
