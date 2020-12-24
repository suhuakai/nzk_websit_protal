package com.web.core.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {

	/**
     * 将字符串中的中文转化为拼音,其他字符不变
     * @param chinese
     * @return String
     */
    public static String getPinYin(String chinese){
        if(chinese==null){
            return null;
        }
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);//小写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//音标
        format.setVCharType(HanyuPinyinVCharType.WITH_V);//特使音标，用u表示v

        char[] input = chinese.trim().toCharArray();
        String output="";

        try {
            for(int i = 0; i < input.length; i++){
                if(java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")){
                        String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i],format);
                        output+=temp[0];
                }else{
                    output+=input[i];
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * 获取汉字串拼音，英文字符不变
     * @param chinese  汉字串
     * @return 汉语拼音
     */
    public static String getFullSpell(String chinese){
        if(chinese==null){
            return null;
        }
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        try {
            for(int i = 0;i<arr.length;i++){
                if(arr[i]>128){
                        pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
                }else{
                    pybf.append(arr[i]);
                }
            }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
            return pybf.toString();
    }

    /**
     * 获取汉字串拼音首字母，英文转换成字母大写，标点转换成英文标点半角
     * @param chinese  汉字串
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String chinese){
        if(chinese==null){
            return null;
        }
        StringBuffer pybf = new StringBuffer();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] arr = chinese.toCharArray();

        for(int i = 0;i<arr.length;i++){
            if(arr[i]>128){
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i],defaultFormat);
                    if(temp!=null){
                        pybf.append(temp[0].charAt(0));
                    }else{
                        pybf.append(arr[i]);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pybf.append(arr[i]);
            }
        }
        String output = pybf.toString().toUpperCase().trim();
        String[] regs = { "！", "，", "。", "；", "：", "【", "】", "？", "‘", "’", "“", "”", "（", "）", "《", "》", "……", "—", "～",
                          "!", ",", ".", ";", ":", "[", "]", "?", "'", "'", "\"", "\"", "(", ")", "<", ">", "…", "-", "~"};
        for ( int i = 0; i < regs.length / 2; i++ )
        {
            output = output.replaceAll (regs[i], regs[i + regs.length / 2]);
        }
        return output;
    }
    
    /* 获取汉字串拼音首字母，英文字符不变
     * 
     * public static String getFirstSpell(String chinese){
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
         
        for(int i = 0;i<arr.length;i++){
            if(arr[i]>128){
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i],defaultFormat);
                    if(temp!=null){
                        pybf.append(temp[0].charAt(0));
                    }else{
                        pybf.append(arr[i]);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }*/

    public static  void main(String[]args){
        String str = "小型远端锁定板【】？’“”‘’《》（）";
        str = getFirstSpell(str);
        System.out.println (str);
    }
}
