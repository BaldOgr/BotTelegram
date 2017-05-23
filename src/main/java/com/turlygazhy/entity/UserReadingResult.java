package com.turlygazhy.entity;

/**
 * Created by Yerassyl_Turlygazhy on 2/28/2017.
 */
public class UserReadingResult {
    private int id;
    private int completed;
    private int aim;
    private String bookName;
    private String summary;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getCompleted() {
        return completed;
    }

    public void setAim(int aim) {
        this.aim = aim;
    }

    public int getAim() {
        return aim;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getThesis() {
        return summary;
    }
}
