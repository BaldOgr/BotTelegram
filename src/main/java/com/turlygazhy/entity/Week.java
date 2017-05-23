package com.turlygazhy.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by Yerassyl_Turlygazhy on 31-Mar-17.
 */
public class Week {

    private List<Date> days;

    public void setDays(List<Date> days) {
        this.days = days;
    }

    public List<Date> getDays() {
        return days;
    }


    public Date getMonday() {
        for (Date day : days) {
            if (day.toString().contains("Mon")) {
                return day;
            }
        }
        throw new RuntimeException("This week does not contain monday");
    }

    public Date getSunday() {
        for (Date day : days) {
            if (day.toString().contains("Sun")) {
                return day;
            }
        }
        throw new RuntimeException("This week does not contain sunday");
    }
}
