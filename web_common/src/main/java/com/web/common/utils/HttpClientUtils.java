package com.web.common.utils;

/**
 * @author
 * @version 1.0
 *
 * @since JDK 1.8
 */
public class HttpClientUtils {

    /**
     * 获取浏览器
     * @param userAgent
     * @return
     */
    public  static String getLogBowser(String userAgent) {
        String logBowser = null;
        String firefox = "FIREFOX";
        String chrome = "CHROME";
        String safari = "SAFARI";
        String ie11 = "LIKE GECKO";
        String ie = "MSIE";
        String edge = "EDGE";
        userAgent = userAgent.toUpperCase();
        if (userAgent == null || "".equals(userAgent.trim())) {
            logBowser = "未知浏览器";
        } else if (userAgent.contains(firefox)) {
            logBowser = userAgent.substring(userAgent.indexOf(firefox));
        } else if (userAgent.contains(chrome)) {
            logBowser = userAgent.substring(userAgent.indexOf(chrome));
            if (logBowser.contains(edge)) {
                logBowser = "Microsoft " + userAgent.substring(userAgent.indexOf(edge));
            }
        } else if (userAgent.contains(safari)) {
            logBowser = userAgent.substring(userAgent.indexOf(safari));
        }else if (userAgent.contains(ie11)) {
            logBowser = "Internet Explorer 11";
        } else if (userAgent.contains(ie)) {
            logBowser = "Internet Explorer" + userAgent.substring(userAgent.indexOf(ie) + 4, userAgent.indexOf(ie) + 8);
        } else {
            logBowser = "未知浏览器";
        }
        return logBowser;
    }

    /**
     * 获取当前使用系统
     * @param userAgent
     * @return
     */
    public static String getLogOS(String userAgent) {
        String logOS = null;
        String windows_xp = "WINDOWS NT 5.1";
        String windows_7 = "WINDOWS NT 6.1";
        String windows_8 = "WINDOWS NT 6.2";
        String windows_8_1 = "WINDOWS NT 6.3";
        String windows_10 = "WINDOWS NT 10";
        String mac_os = "MAC OS";
        userAgent = userAgent.toUpperCase();

        if (userAgent == null || "".equals(userAgent.trim())) {
            logOS = "未知系统";
        } else if (userAgent.contains(windows_xp)) {
            logOS = "windows xp";
        } else if (userAgent.contains(windows_7)) {
            logOS = "windows 7";
        } else if (userAgent.contains(windows_8)) {
            logOS = "windows 8";
        } else if (userAgent.contains(windows_8_1)) {
            logOS = "windows 8.1";
        } else if (userAgent.contains(windows_10)) {
            logOS = "windows 10";
        } else if (userAgent.contains(mac_os)) {
            logOS = userAgent.substring(userAgent.indexOf(mac_os),userAgent.indexOf(mac_os)+16);
        }else {
            logOS = "未知系统";
        }
        return logOS;
    }
}
