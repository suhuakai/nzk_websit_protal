package com.web.core.util;

import java.util.UUID;

/**
 * 生成ID、流水号等标识串常用工具类 <br/>
 * @author
 * @version 1.0
 */
public class IdentifieUtil {
	
	/** 
	 * 获取GuId
	 * @return String
	 */
	public static String getGuId(){
		  UUID uuid = UUID.randomUUID();
	      // 得到对象产生的ID
	      String guId = uuid.toString();
	      // 转换为大写
	      guId = guId.toUpperCase();
	      // 替换 -
	      guId = guId.replaceAll("-", "");
		return guId;
	}
	
	private final static char[] DIGITS64 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_"
			.toCharArray();

	public static String get26GuId() {
		UUID u = UUID.randomUUID();
		// 转换为大写
	    String  guId = toIDString(u.getMostSignificantBits()) + toIDString(u.getLeastSignificantBits());
	    guId = guId.toUpperCase();
	    if(guId.contains("-") || guId.contains("_")){
	    	guId = get26GuId();
	    }
	    return	guId;
	}

	private static String toIDString(long l) {
		char[] buf = "0000000000000".toCharArray(); // 限定13位长度
		int length = 11;
		long least = 63L; // 0x0000003FL
		do {
			buf[--length] = DIGITS64[(int) (l & least)]; // l & least取低6位
			l >>>= 6;
		} while (l != 0);
		return new String(buf);
	}
	
	/**
	 * 测试
	 */
	public static void main(String[] args) {
		System.out.println(getGuId());
	}
}
  