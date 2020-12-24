/**
 */

package com.web.common.utils.print;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceConfig {
	
	private static final Properties	properties	= new Properties();
	
	static {
		try {
			InputStream inStream = ResourceConfig.class.getClassLoader().getResourceAsStream("resource.properties");
			properties.load(inStream);;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 根据key找出value值
	 * @param key 键
	 * @return String
	 * @author
	 */
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

}
