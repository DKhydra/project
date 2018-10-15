package com.hydra.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static enum DateFormatStr{
		yyyyMMdd("yyyy-MM-dd"),yyyyMMddHH("yyyy-MM-dd HH"),yyyyMMddHHmm("yyyy-MM-dd HH:mm"),yyyyMMddHHmmss("yyyy-MM-dd HH:mm:ss");
		private String formatStr = null ;
		DateFormatStr(String str) {
			formatStr = str;
		}
		public String get() {
			return formatStr;
		}
		
	}
	public static void main(String[] args) {
		System.err.println(DateUtil.getTomorrow(new Date(), DateFormatStr.yyyyMMdd));
	}
	public static Date getTomorrow(Date date,DateFormatStr format) {
		SimpleDateFormat sdf=new SimpleDateFormat(format.get()); 
		try {
			date = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        cal.add(Calendar.DAY_OF_YEAR,1);  
        return cal.getTime();  
	}
	public static Date getNow(DateFormatStr format) {
		Date date = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat(format.get()); 
		try {
			date = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public static Date getThisWeekMonday(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        // 获得当前日期是一个星期的第几天  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);  
        if (1 == dayWeek) {  
            cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);  
        // 获得当前日期是一个星期的第几天  
        int day = cal.get(Calendar.DAY_OF_WEEK);  
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
        return cal.getTime();  
    }  
	public static Date getThisMonthFirstDay(Date date){  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.set(Calendar.DAY_OF_MONTH, 1);  
        calendar.set(Calendar.HOUR_OF_DAY, 0);  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.SECOND, 0);  
        return calendar.getTime();  
    }
}
