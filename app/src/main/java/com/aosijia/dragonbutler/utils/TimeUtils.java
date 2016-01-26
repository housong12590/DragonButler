package com.aosijia.dragonbutler.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wanglj on 15/12/25.
 */
public class TimeUtils {


    /**
     * 将时间戳转为字符串
     * @param date
     * @return
     */
    public static String getDateToString(String date) {
        long parseLong = 1;
        if (!TextUtils.isEmpty(date) && TextUtils.isDigitsOnly(date)) {
            parseLong = Long.parseLong(date) * 1000;
            return new SimpleDateFormat("yyyy-MM-dd\u0020HH:mm").format(new Date(parseLong));
        }
        return date;
    }

    public static String getDateToString2(String date) {
        long parseLong = 1;
        if (!TextUtils.isEmpty(date) && TextUtils.isDigitsOnly(date)) {
            parseLong = Long.parseLong(date) * 1000;
            return new SimpleDateFormat("yyyy-MM-dd").format(new Date(parseLong));
        }
        return date;
    }


    /**
     * 将时间字符串转为时间戳
     * @param time
     * @return
     */
    public static String stringToTime(String time) {
        String re_tiem = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date;
        try {
            date = sdf.parse(time);
            long dateTime = date.getTime();
            String s = String.valueOf(dateTime);
            re_tiem = s.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_tiem;
    }

    /**
     * 当前时间是否小于开始时间
     * @param startTimeSeconds
     * @return
     */
    public static boolean isLessThanStartTime(String startTimeSeconds){
        if(TextUtils.isEmpty(startTimeSeconds)|| !TextUtils.isDigitsOnly(startTimeSeconds)){
            return true;
        }
        long now = System.currentTimeMillis()/1000L;
        long start = Long.parseLong(startTimeSeconds) ;
        if(now<start) {
            return true;
        }else {
            return false;
        }
    }


    /**
     * 当前时间是否大于结束时间
     * @param endTimeSeconds
     * @return
     */
    public static boolean isGreaterThanEndTime(String endTimeSeconds){
        if(TextUtils.isEmpty(endTimeSeconds)|| !TextUtils.isDigitsOnly(endTimeSeconds)){
            return true;
        }
        long now = System.currentTimeMillis()/1000L;
        long end = Long.parseLong(endTimeSeconds) ;
        if(now>end) {
            return true;
        }else {
            return false;
        }
    }


    /**
     * 是否为长期有效的活动
     * @param endTime
     * @return
     */
    public static boolean isPermanentEvent(String endTime){
        if(TextUtils.isEmpty(endTime)) {
            return true;
        }else {
            return false;
        }
    }

    public static String getFourteenFormatTime(long timeInMills) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(timeInMills));
    }

}
