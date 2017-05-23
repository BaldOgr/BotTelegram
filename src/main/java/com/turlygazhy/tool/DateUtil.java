package com.turlygazhy.tool;

import com.turlygazhy.entity.Week;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yerassyl_Turlygazhy on 06-Mar-17.
 */
public class DateUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");

    public static Date getThisMonday() {
        Date date = new Date();
        while (!date.toString().contains("Mon")) {
            date.setDate(date.getDate() - 1);
        }
        return date;
    }

    public static Date getThisSunday() {
        Date date = new Date();
        while (!date.toString().contains("Sun")) {
            date.setDate(date.getDate() + 1);
        }
        return date;
    }

    public static Date getNextMonth() {
        Date date = new Date();
        date.setDate(date.getDate() + 1);
        while (date.getDate() != 1) {
            date.setDate(date.getDate() + 1);
        }
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(1);
        return date;
    }

    public static Date getNextWeek() {
        Date date = new Date();
        date.setDate(date.getDate() + 1);
        while (!date.toString().contains("Mon")) {
            date.setDate(date.getDate() + 1);
        }
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(1);
        return date;
    }

    public static Date getNextNight() {
        Date date = new Date();
        date.setDate(date.getDate() + 1);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(1);
        return date;
    }

    public static boolean checkHour(int hour) {
        Date date = new Date();
        return date.getHours() == hour;
    }

    public static Date getHour(int hour) {
        Date date = new Date();
        if (date.getHours() >= hour) {
            date.setDate(date.getDate() + 1);
        }
        date.setHours(hour);
        date.setMinutes(0);
        date.setSeconds(1);
        return date;
    }

    public static boolean isNewWeek() {
        Date date = new Date();
        return date.toString().contains("Mon");
    }

    public static boolean isNewMonth() {
        return new Date().getDate() == 1;
    }

    public static String getPastDay() {
        Date date = new Date();
        date.setDate(date.getDate() - 1);
        return format.format(date);
    }

    public static List<String> getLastMonthSundaysListAsString() {
        List<String> result = new ArrayList<>();
        Date date = new Date();
        date.setDate(date.getDate() - 1);
        int month = date.getMonth();
        while (true) {
            if (month > date.getMonth()) {
                break;
            }

            String dateAsString = date.toString();
            if (dateAsString.contains("Sun")) {
                result.add(format.format(date));
            }
            date.setDate(date.getDate() - 1);
        }
        return result;
    }

    public static Date getThisMonthStartDay() {
        return null;
    }

    public static List<Week> getLastMonthWeeks() {
        List<Week> weeks = new ArrayList<>();
        Date date = new Date();
        date.setDate(date.getDate() - 1);
        int month = date.getMonth();
        boolean monthEnded = false;
        Week week = null;
        List<Date> days = new ArrayList<>();
        while (true) {
            String dateAsString = date.toString();
            if (month > date.getMonth()) {
                monthEnded = true;
            }
            if (dateAsString.contains("Sun")) {
                if (week != null) {
                    week.setDays(days);
                    weeks.add(week);
                    days = new ArrayList<>();
                }
                if (monthEnded) {
                    break;
                }
                week = new Week();
            }
            if (week != null) {
                days.add((Date) date.clone());
            }
            date.setDate(date.getDate() - 1);
        }
        return weeks;
    }

    public static Date getLastMonthFirstDay() {
        Date date = new Date();
        date.setMonth(date.getMonth() - 1);
        date.setDate(1);
        return date;
    }

    public static Date getLastMonthLastDay() {
        Date date = new Date();
        int month = date.getMonth();//todo а что если январь
        while (true) {
            date.setDate(date.getDate() - 1);
            if (month > date.getMonth()) {
                break;
            }
        }
        return date;
    }
}
