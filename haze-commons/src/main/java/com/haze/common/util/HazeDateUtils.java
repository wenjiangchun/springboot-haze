package com.haze.common.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * 日期时间辅助功能类
 * 
 * @author sofar
 * @see DateUtils
 */
public class HazeDateUtils extends DateUtils {

	public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static  Calendar cal = Calendar.getInstance();

	/**
	 * 获取当前日期时间
	 * @return Date 当前日期时间
	 */
	public static Date getCurrentDate() {
		return new Date();
	}

    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        return sdf.format(date);
    }
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static int getYear(Date date) {
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getWeek(Date date) {
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getDay(Date date) {
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 判断日期为周几
     * @param date 日期时间
     * @return 周日为1，以此类推
     */
    public static int getDayInWeek(Date date) {
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 计算两个日期相隔天数
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期天数差
     */
    public static int getDiffDay(Date startDate, Date endDate) {
        return Days.daysBetween(new DateTime(startDate), new DateTime(endDate)).getDays();
    }

    /**
     * 获取两个日期差天数. 排除周六周日
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return 排除周六周日后的天数
     */
    public static int getDiff(Date startTime, Date endTime) {
        long diff = endTime.getTime() - startTime.getTime();
        int hours = (int) (diff/(1000*60*60));
        int days = 0;
        if (hours % 24 != 0) {
            days = hours/24 + 1;
        } else {
            days = hours/24;
        }
        int newDays = 0;
        for (int i=0; i < days; i++) {
            Date newDate = addDays(startTime, i);
            //判断该天是周几
            int weekDay = getDayInWeek(newDate);
            //如果不是周日和周六
            if (weekDay != 1 && weekDay != 7) {
                newDays += 1;
            }
        }
        return newDays != 0 ? newDays : 1;
    }

    public static LocalDate toLocalDate(Date d) {
        if (d != null) {
            return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            return LocalDate.now();
        }
    }

    public static LocalDateTime toLocalDateTime(Date d) {
        if (d != null) {
            return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else {
            return LocalDateTime.now();
        }
    }

    public static Date fromLocalDate(LocalDate d) {
        if (d != null) {
            return Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    public static Date fromLocalDateTime(LocalDateTime d) {
        if (d != null) {
            return Date.from(d.atZone(ZoneId.systemDefault()).toInstant());
        } else {
            return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }
}
