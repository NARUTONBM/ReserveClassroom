package com.naruto.reserveclassroom.utils;
/*
 * Created by NARUTO on 2016-12-01.
 */

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeFormatUtil {

	/**
	 * Java将Unix时间戳转换成指定格式日期字符串
	 * 
	 * @param timestampString
	 *            时间戳 如："1473048265";
	 * @param formats
	 *            要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
	 *
	 * @return 返回结果 如："2016-09-05 16:06:42";
	 */
	public static String TimeStamp2Date(String timestampString, String formats) {

		if (TextUtils.isEmpty(formats))
			formats = "yyyy-MM-dd HH:mm:ss";
		Long timestamp = Long.parseLong(timestampString) * 1000;

		return new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
	}

	/**
	 * 日期格式字符串转换成时间戳
	 *
	 * @param dateStr
	 *            字符串日期
	 * @param format
	 *            如：yyyy-MM-dd HH:mm:ss
	 *
	 * @return 返回结果 如："1473048265";
	 */
	public static String Date2TimeStamp(String dateStr, String format) {

		try {

			if (TextUtils.isEmpty(format))
				format = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(format);

			return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return "";
	}
}
