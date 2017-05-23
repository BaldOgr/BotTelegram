package com.turlygazhy.entity;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;

/**
 * Created by user on 12/14/16.
 */
public class Message {
    private long id;
    private SendPhoto sendPhoto;
    private SendMessage sendMessage;
    private long keyboardMarkUpId;

    public Message(SendPhoto sendPhoto, SendMessage message) {
        this.sendMessage = message;
        this.sendPhoto = sendPhoto;
    }

    public Message() {

    }

    public SendPhoto getSendPhoto() {
        return sendPhoto;
    }

    public void setSendPhoto(SendPhoto sendPhoto) {
        this.sendPhoto = sendPhoto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SendMessage getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    public long getKeyboardMarkUpId() {
        return keyboardMarkUpId;
    }

    public void setKeyboardMarkUpId(long keyboardMarkUpId) {
        this.keyboardMarkUpId = keyboardMarkUpId;
    }
}
