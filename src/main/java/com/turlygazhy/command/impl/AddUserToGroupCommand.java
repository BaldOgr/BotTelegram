package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 2/23/17.
 */
public class AddUserToGroupCommand extends Command {
    private final Integer userId;
    private final Integer groupId;

    public AddUserToGroupCommand(Integer userId, Integer groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        memberDao.giveAccess(userId, groupId);
        sendMessageToAdmin(36, bot);
        sendMessage(35, userId, bot);
        return true;
    }
}
