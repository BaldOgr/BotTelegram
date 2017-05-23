package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 1/5/17.
 */
public class RequestCallCommand extends Command {
    private WaitingType waitingType;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        Long chatId = updateMessage.getChatId();
        if (waitingType == null) {
            sendMessage(60, chatId, bot);
            waitingType = WaitingType.CONTACT;
            return false;
        }
        switch (waitingType) {
            case CONTACT:
                Contact contact = updateMessage.getContact();
                Long adminChatId = Long.valueOf(constDao.select(2));
                if (contact != null) {
                    sendMessage(61, adminChatId, bot, contact);
                } else {
                    sendMessage(61, adminChatId, bot);
                    sendMessage(updateMessage.getText(), adminChatId, bot);
                }
                sendMessage(62, chatId, bot);
                return true;
        }
        return false;
    }
}
