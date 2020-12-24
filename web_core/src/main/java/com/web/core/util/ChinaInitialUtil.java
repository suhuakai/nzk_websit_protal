package com.web.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * 为了满足通用简码的需要特此开发此工具
 * @author
 * @version 1.0
 */
public class ChinaInitialUtil {
	public static final Logger logger = LoggerFactory.getLogger(ChinaInitialUtil.class);
	private static final String regEx = "[ `%^&*()=+{}[]\\<>,.\"|~!@#$%^&*()-+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

	/**
	 * 返回首字母
	 * @param strChinese
	 * @param bUpCase
	 * @return
	 */
	public static String getPYIndexStr(String strChinese, boolean bUpCase) {
		try {
			StringBuffer buffer = new StringBuffer();
			strChinese = tozf(strChinese);
			byte b[] = strChinese.getBytes("GBK");//把中文转化成byte数组
			for (int i = 0; i < b.length; i++) {
				if ((b[i] & 255) > 128) {
					int char1 = b[i++] & 255;
					//左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n位，就相当于乘上2的n次方
					char1 <<= 8;
					int chart = char1 + (b[i] & 255);
					buffer.append(getPYIndexChar((char) chart, bUpCase));
					continue;
				}

				char c = (char) b[i];
				if (regEx.contains(String.valueOf(c))) {//指定字符不用转译
					buffer.append(c);
					continue;
				}
				if (!Character.isJavaIdentifierPart(c)) {//确定指定字符是否可以是 Java,标识符中首字符以外的部分。
					c = 'A';
				}
				buffer.append(c);
			}
			return buffer.toString();
		} catch (Exception e) {
			logger.error("获取字符串的拼音首字母拼接串的时候抛出异常!", e);
		}
		return null;
	}

	/**
	 * 得到首字母
	 * @param	strChinese
	 * @param	toUpCase
	 * @return	char
	 */
	private static char getPYIndexChar(char strChinese, boolean toUpCase) {
		int charGBK = strChinese;
		char result;
		if (charGBK >= 45217 && charGBK <= 45252){
			result = 'A';
		}else if (charGBK >= 45253 && charGBK <= 45760){
			result = 'B';
		}else if (charGBK >= 45761 && charGBK <= 46317){
			result = 'C';
		}else if (charGBK >= 46318 && charGBK <= 46825){
			result = 'D';
		}else if (charGBK >= 46826 && charGBK <= 47009) {
            result = 'E';
        } else if (charGBK >= 47010 && charGBK <= 47296) {
            result = 'F';
        } else if (charGBK >= 47297 && charGBK <= 47613) {
            result = 'G';
        } else if (charGBK >= 47614 && charGBK <= 48118) {
            result = 'H';
        } else if (charGBK >= 48119 && charGBK <= 49061) {
            result = 'J';
        } else if (charGBK >= 49062 && charGBK <= 49323) {
            result = 'K';
        } else if (charGBK >= 49324 && charGBK <= 49895) {
            result = 'L';
        } else if (charGBK >= 49896 && charGBK <= 50370) {
            result = 'M';
        } else if (charGBK >= 50371 && charGBK <= 50613) {
            result = 'N';
        } else if (charGBK >= 50614 && charGBK <= 50621) {
            result = 'O';
        } else if (charGBK >= 50622 && charGBK <= 50905) {
            result = 'P';
        } else if (charGBK >= 50906 && charGBK <= 51386) {
            result = 'Q';
        } else if (charGBK >= 51387 && charGBK <= 51445) {
            result = 'R';
        } else if (charGBK >= 51446 && charGBK <= 52217) {
            result = 'S';
        } else if (charGBK >= 52218 && charGBK <= 52697) {
            result = 'T';
        } else if (charGBK >= 52698 && charGBK <= 52979) {
            result = 'W';
        } else if (charGBK >= 52980 && charGBK <= 53688) {
            result = 'X';
        } else if (charGBK >= 53689 && charGBK <= 54480) {
            result = 'Y';
        } else if (charGBK >= 54481 && charGBK <= 55289) {
            result = 'Z';
        } else {
            result = (char) (65 + new Random().nextInt(25));
        }

		if (!toUpCase) {
            result = Character.toLowerCase(result);
        }

		return result;
	}

	/**
	 * 将中文字符装换成英文字符
	 *
	 * @param	str
	 * @return	String
	 */
	private static String tozf(String str) {
		if (str.contains("，")) {
			str = str.replaceAll("，", ",");
		}
		if (str.contains("！")) {
			str = str.replaceAll("！", "!");
		}
		if (str.contains("（")) {
			str = str.replaceAll("（", "(");
		}
		if (str.contains("）")) {
			str = str.replaceAll("）", ")");
		}
		if (str.contains("￥")) {
			str = str.replaceAll("￥", "$");
		}
		if (str.contains("·")) {
			str = str.replaceAll("·", "`");
		}
		if (str.contains("【")) {
			str = str.replaceAll("【", "[");
		}
		if (str.contains("】")) {
			str = str.replaceAll("】", "]");
		}
		if (str.contains("‘")) {
			str = str.replaceAll("‘", "'");
		}
		if (str.contains("；")) {
			str = str.replaceAll("；", ";");
		}
		if (str.contains("：")) {
			str = str.replaceAll("：", ":");
		}
		if (str.contains("”")) {
			str = str.replaceAll("”", "'");
		}
		if (str.contains("“")) {
			str = str.replaceAll("“", "'");
		}
		if (str.contains("。")) {
			str = str.replaceAll("。", ".");
		}
		if (str.contains("，")) {
			str = str.replaceAll("，", ",");
		}
		if (str.contains("、")) {
			str = str.replaceAll("、", "/");
		}
		if (str.contains("？")) {
			str = str.replaceAll("？", "?");
		}
		if (str.contains("—")) {
			str = str.replaceAll("—", "-");
		}
		return str;
	}
	
	public static void main(String[] args) {
		String str = "我是中国人—\"\"：&abc123";
		System.out.println("中文首字母：" + getPYIndexStr(str, true));
	}
}