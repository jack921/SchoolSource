package com.example.DateUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by jack on 2015/6/16.
 */
public class CalendarUtil {

    private static int getMondayPlus(){
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1-dayOfWeek;
        }
    }

    //今天是这个星期的第几天
    public static int getDayOfWeek() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    //得到一个星期的第一天
    public static String getMondayOfWeek(){
        int weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = df.format(monday);

        return preMonday;
    }

    //获取两天间隔的天数
    public static int getTwoDay(String date1,String date2){
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(date1);
            java.util.Date mydate = myFormatter.parse(date2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return 0;
        }
        return (int)day;
    }

}
