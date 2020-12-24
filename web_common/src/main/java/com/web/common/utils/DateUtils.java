/**
 */
package com.web.common.utils;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
			"yyyy.MM.dd HH:mm", "yyyy.MM" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	public static Date getFromToEndDate(Date date) {
		Date time = null;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(DateFormatUtils.format(date, "yyyy-MM-dd 23:59:59"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
	 * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	public static String format(Date date, String datePattern) {
		return date != null ? DateFormatUtils.format(date, datePattern) : null;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 * @throws ParseException
	 */
	public static int getDiffDate(Date before, Date after) {
		try {
			String beforeDate = formatDate(before, "yyyy-MM-dd");
			String afterDate = formatDate(after, "yyyy-MM-dd");

			before = DateUtils.parseDate(beforeDate, "yyyy-MM-dd");
			after = DateUtils.parseDate(afterDate, "yyyy-MM-dd");

			long beforeTime = before.getTime();
			long afterTime = after.getTime();
			return (int) ((afterTime - beforeTime) / (1000 * 60 * 60 * 24));
		} catch (Exception e) {
			return 1;
		}

	}

	/**
	 * 得到日期字符串，转换格式（yyyy-MM-dd）
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = System.currentTimeMillis()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
		Date d1 = new Date();

		String ss = "2020-10-30";

		Date d2 = DateUtils.parseDate(ss, "yyyy-MM-dd");

		DateUtils.formatDate(d2,"yyyy-MM-dd HH:mm:ss");

		System.out.println(getDiffDate(d2, d1));
		System.out.println(localDateTime2DateStr(LocalDateTime.now()));

	}

	/**
	 * date转LocalDateTime
	 * @param date
	 * @return
	 */
	public static LocalDateTime fromLocalDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
		return localDateTime;
	}

	/**
	 * LocalDateTime转date
	 * @param localDateTime
	 * @return
	 */
	public static Date localDateTime2Date(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		Date date = Date.from(zdt.toInstant());
		return date;
	}
	
	public static String localDateTime2DatetTimeStr(LocalDateTime localDateTime) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return df.format(localDateTime);
	}
	
	public static String localDateTime2DateStr(LocalDateTime localDateTime) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return df.format(localDateTime);
	}

	/**
	 * str转localDate
	 * @param date
	 * @return
	 */
	public static LocalDate str2LocalDate(String date){
		LocalDate dateTime = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return dateTime;
	}

	public static String parseTimestampToStr(Timestamp time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.format(time);
		return formatter.format(time);
	}

	/**
	 * 将字符串日期转换为日期格式
	 *
	 *
	 * @param datestr
	 * @return
	 *
	 */
	public static Date stringToDate(String datestr) {

		if(datestr ==null || "".equals(datestr)){
			return null;
		}
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = df.parse(datestr);
		} catch (ParseException e) {
			date=DateUtils.stringToDate(datestr,"yyyy-MM-dd");
		}
		return date;
	}

	/**
	 * 将字符串日期转换为日期格式
	 * 自定義格式
	 *
	 * @param datestr
	 * @return
	 *
	 */
	public static Date stringToDate(String datestr, String dateformat) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat(dateformat);
		try {
			date = df.parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
