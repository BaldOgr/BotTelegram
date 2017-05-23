package com.turlygazhy.entity;

/**
 * Created by user on 2/23/17.
 */
public class Group {
    private int id;
    private String title;
    private long chatId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }
}
