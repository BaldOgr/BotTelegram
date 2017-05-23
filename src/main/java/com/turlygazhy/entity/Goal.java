package com.turlygazhy.entity;

/**
 * Created by user on 2/28/17.
 */
public class Goal {
    private int id;
    private String name;
    private int aim;
    private boolean timeLimit;
    private String startTime;
    private String endTime;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAim(int aim) {
        this.aim = aim;
    }

    public int getAim() {
        return aim;
    }

    public void setTimeLimit(boolean timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isTimeLimit() {
        return timeLimit;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
