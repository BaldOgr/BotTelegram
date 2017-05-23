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
public class ChangeNishaCommand extends Command {
    private String nisha;
    private WaitingType waitingType;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        if (waitingType != null) {
            switch (waitingType) {
                case NISHA:
                    nisha = update.getMessage().getText();
                    break;
            }
        }
        Integer userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        if (nisha == null) {
            sendMessage(20, chatId, bot);
            waitingType = WaitingType.NISHA;
            return false;
        }
        memberDao.updateNishaByUserId(userId, nisha);
        sendMessage(19, chatId, bot);
        return true;
    }
}
