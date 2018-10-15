package com.hydra.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author Javen
 *
 */
public final class GetUTCTimeUtil {

    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;

    /**
     * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm"<br />
     * 如果获取失败，返回null
     * @return
     */
    public static String getUTCTimeStr() {
        StringBuffer UTCTimeBuffer = new StringBuffer();
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance() ;
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        int year = cal.get(Calendar.YEAR);
        String monthStr = "";
        String dayStr = "";
        int month = cal.get(Calendar.MONTH)+1;
        if(month < 10 ){ monthStr  = "0"+month;}else {monthStr = String.valueOf(month);}
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if(day < 10 ){ dayStr  = "0"+day;}else {dayStr = String.valueOf(day);}
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String hourStr = "";
        String secondStr = "";
        String minuteStr = "";
        if(hour < 10 ){ hourStr  = "0"+hour;}else {hourStr = String.valueOf(hour);}
        int minute = cal.get(Calendar.MINUTE);
        if(minute < 10 ){ minuteStr  = "0"+minute;}else {minuteStr = String.valueOf(minute);}
        int second = cal.get(Calendar.SECOND);
        if(second < 10 ){ secondStr  = "0"+second;}else {secondStr = String.valueOf(second);}
        UTCTimeBuffer.append(year).append("-").append(monthStr).append("-").append(dayStr) ;
       StringBuffer timeStr =  UTCTimeBuffer.append("T").append(hourStr).append(":").append(minuteStr).append(":").append(secondStr).append("Z");
        try{
            return timeStr.toString() ;
        }catch(Exception e)
        {
            e.printStackTrace() ;
        }
        return null ;
    }

    /**
     * 将UTC时间转换为东八区时间
     * @param UTCTime
     * @return
     */
    public static String getLocalTimeFromUTC(String UTCTime){
        java.util.Date UTCDate = null ;
        String localTimeStr = null ;
        try {
            UTCDate = format.parse(UTCTime);
            format.setTimeZone(TimeZone.getTimeZone("GMT-8")) ;
            localTimeStr = format.format(UTCDate) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return localTimeStr ;
    }


    public static String getTimeZoneByNumExpress(){
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        int rawOffset = timeZone.getRawOffset();
        int timeZoneByNumExpress = rawOffset/3600/1000;
        String timeZoneByNumExpressStr = "";
        if(timeZoneByNumExpress > 0 && timeZoneByNumExpress < 10){
            timeZoneByNumExpressStr = "+" + "0" + timeZoneByNumExpress + ":" + "00";
        }
        else if(timeZoneByNumExpress >= 10){
            timeZoneByNumExpressStr = "+" + timeZoneByNumExpress + ":" + "00";
        }
        else if(timeZoneByNumExpress > -10 && timeZoneByNumExpress < 0){
            timeZoneByNumExpress = Math.abs(timeZoneByNumExpress);
            timeZoneByNumExpressStr = "-" + "0" + timeZoneByNumExpress + ":" + "00";
        }else if(timeZoneByNumExpress <= -10){
            timeZoneByNumExpress = Math.abs(timeZoneByNumExpress);
            timeZoneByNumExpressStr = "-" + timeZoneByNumExpress + ":" + "00";
        }else{
            timeZoneByNumExpressStr = "Z";
        }
        return timeZoneByNumExpressStr;
    }


    public static void main(String[] args) {
        String UTCTimeStr = getUTCTimeStr() ;
        System.out.println(UTCTimeStr);
        System.out.println(getLocalTimeFromUTC(UTCTimeStr));
    }

}
