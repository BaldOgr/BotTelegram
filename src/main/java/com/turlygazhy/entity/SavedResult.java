package com.turlygazhy.entity;

import java.util.Date;

/**
 * Created by Yerassyl_Turlygazhy on 03-Mar-17.
 */
public class SavedResult {
    private int id;
    private Integer userId;
    private Date date;
    private int result;
    private int goalId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getGoalId() {
        return goalId;
    }
}
