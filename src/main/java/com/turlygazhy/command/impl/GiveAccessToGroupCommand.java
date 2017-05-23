package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 2/22/17.
 */
public class GiveAccessToGroupCommand extends Command {
    private final Long chatId;

    public GiveAccessToGroupCommand(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        groupDao.giveAccess(chatId);
        Message updateMessage = update.getMessage();
        if (updateMessage == null) {
            updateMessage = update.getCallbackQuery().getMessage();
        }
        bot.sendMessage(new SendMessage()
                .setChatId(updateMessage.getChatId())
                .setText(messageDao.getMessageText(17))
        );
        bot.sendMessage(new SendMessage()
                .setChatId(chatId)
                .setText(messageDao.getMessageText(37))
        );
        return true;
    }
}
