package com.turlygazhy.entity;

/**
 * Created by user on 1/22/17.
 */
public class Member {
    private int id;
    private Integer userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private long chatId;
    private String naviki;
    private String nisha;
    private String userName;
    private int groupId;
    private boolean access;
    private int fallsInThisMonth;

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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setNaviki(String naviki) {
        this.naviki = naviki;
    }

    public String getNaviki() {
        return naviki;
    }

    public void setNisha(String nisha) {
        this.nisha = nisha;
    }

    public String getNisha() {
        return nisha;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public boolean isAccess() {
        return access;
    }

    public void setFallsInThisMonth(int fallsInThisMonth) {
        this.fallsInThisMonth = fallsInThisMonth;
    }

    public int getFallsInThisMonth() {
        return fallsInThisMonth;
    }
}
