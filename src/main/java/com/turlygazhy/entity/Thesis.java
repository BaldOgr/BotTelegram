package com.turlygazhy.entity;

/**
 * Created by Yerassyl_Turlygazhy on 01-Mar-17.
 */
public class Thesis {
    private int id;
    private String bookName;
    private String userName;
    private String thesis;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setThesis(String thesis) {
        this.thesis = thesis;
    }

    public String getThesis() {
        return thesis;
    }
}
