package com.dc.wibome.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
* 转化json返回的日期类型，
* 日期工具类
* */
public class DateUtils {

//	"created_at": "Wed Jun 17（日期） 14:26:24 +0800（时区） 2015"  需要处理的日期类型

    public static final long ONE_MINUTE_MILLIONS = 60 * 1000; //分钟的毫秒值
    public static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
    public static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;

    public static String getShortTime(String dateStr) {
        String str = "";

       //Locale.US 显示为英文格式
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
        try {
            Date date = sdf.parse(dateStr); //把传入的原字符串转化成Format

            Date curDate = new Date();  //当前时间

            long durTime = curDate.getTime() - date.getTime();  //2者时间的差值，毫秒
            int dayStatus = calculateDayStatus(date, curDate);  //2者之间的天数差值

            if(durTime <= 10 * ONE_MINUTE_MILLIONS) {   //2者时间的差值小于10分钟，显示刚刚
                str = "刚刚";
            } else if(durTime < ONE_HOUR_MILLIONS) {  //小于1小时，差值毫秒除以1小时毫秒
                str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
            } else if(dayStatus == 0) {  //今天
                str = durTime / ONE_HOUR_MILLIONS + "小时前"; ////小于24小时，差值毫秒除以24小时毫秒

            } else if(dayStatus == -1) {      //昨天
                str = "昨天" + DateFormat.format("HH:mm", date);  //显示时，分

            } else if(isSameYear(date, curDate) && dayStatus < -1) { //同一年且更早的日子
                str = DateFormat.format("MM-dd", date).toString(); //显示月，日
            } else {
                str = DateFormat.format("yyyy-MM", date).toString();  //显示年 ，月
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    /*
    * 用来判断是否同一年*/
    public static boolean isSameYear(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR); //获取年

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);

        return tarYear == comYear;
    }
    /*处理天的方法*/
    public static int calculateDayStatus(Date targetTime, Date compareTime) {

        Calendar tarCalendar = Calendar.getInstance();  //日历类的用法

        tarCalendar.setTime(targetTime);
        int tarDayOfYear = tarCalendar.get(Calendar.DAY_OF_YEAR); //获取当前天数

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comDayOfYear = compareCalendar.get(Calendar.DAY_OF_YEAR);

        return tarDayOfYear - comDayOfYear;   //返回天数差值
    }
}

