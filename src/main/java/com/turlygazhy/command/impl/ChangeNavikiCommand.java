package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 1/22/17.
 */
public class ChangeNavikiCommand extends Command {
    private String naviki;
    private WaitingType waitingType;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        if (waitingType != null) {
            switch (waitingType) {
                case NAVIKI:
                    naviki = update.getMessage().getText();
                    break;
            }
        }
        Integer userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        if (naviki == null) {
            sendMessage(21, chatId, bot);
            waitingType = WaitingType.NAVIKI;
            return false;
        }
        memberDao.updateNavikiByUserId(userId, naviki);
        sendMessage(22, chatId, bot);
        return true;
    }
}
