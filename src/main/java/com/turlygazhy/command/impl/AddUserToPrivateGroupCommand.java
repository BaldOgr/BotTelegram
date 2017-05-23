package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.connection_pool.ConnectionPool;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by lol on 22.05.2017.
 */
public class AddUserToPrivateGroupCommand extends Command {
    private WaitingType waitingType;
    private String birthday;
    private String name;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {

        Message updateMessage = update.getMessage();
        Long chatId = updateMessage.getChatId();
        Long adminChatId = Long.valueOf(constDao.select(2));
        String updateMessageText = updateMessage.getText();

        if (waitingType == null) {
            sendMessage(68, chatId, bot);
            waitingType = WaitingType.NAME;
            return false;
        }

        switch (waitingType) {
            case NAME:
                sendMessage(71, chatId, bot);
                name = updateMessageText;
                waitingType = WaitingType.BIRTHDAY;
                return false;

            case BIRTHDAY:
                sendMessage(60, chatId, bot);
                birthday = updateMessageText;
                waitingType = WaitingType.CONTACT;
                return false;

            case CONTACT:
                sendMessage(72, adminChatId, bot);
                Contact contact = updateMessage.getContact();

                PreparedStatement ps = ConnectionPool.getConnection().prepareStatement("INSERT INTO USERS_TO_GROUP VALUES(default, ?, ?, ?, ?, 0)");
                ps.setLong(1, updateMessage.getChatId());
                ps.setString(2, name);
                ps.setString(3, birthday);
                if (contact == null){
                    ps.setString(4, updateMessageText);
                } else {
                    ps.setString(4, contact.getPhoneNumber());
                }
                ps.execute();
                sendMessage(62, chatId, bot);
                return true;
        }

        return false;
    }
}
