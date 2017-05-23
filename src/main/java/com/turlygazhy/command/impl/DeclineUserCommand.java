package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 2/23/17.
 */
public class DeclineUserCommand extends Command {
    private final Integer userId;

    public DeclineUserCommand(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        sendMessageToAdmin(25, bot);
        sendMessage(25, memberDao.getChatId(userId), bot);
        return true;
    }
}
