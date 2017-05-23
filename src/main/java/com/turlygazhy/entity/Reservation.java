package com.turlygazhy.entity;

/**
 * Created by user on 1/26/17.
 */
public class Reservation {
    private int id;
    private String time;
    private long clientChatId;
    private String clientInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getClientChatId() {
        return clientChatId;
    }

    public void setClientChatId(long clientChatId) {
        this.clientChatId = clientChatId;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }
}
