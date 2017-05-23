package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by Yerassyl_Turlygazhy on 10-Apr-17.
 */
public class OrderCakeCommand extends Command {
    private WaitingType waitingType;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        Long chatId = updateMessage.getChatId();
        String updateMessageText = updateMessage.getText();
        if (waitingType == null) {
            sendMessage(64, chatId, bot);
            waitingType = WaitingType.CAKE;
            return false;
        }
        switch (waitingType) {
            case CAKE:
                if (updateMessageText.equals("/id1")) {
                    sendMessage(65, chatId, bot);
                    waitingType = WaitingType.ADDRESS;
                    return false;
                } else {
                    sendMessage(7, chatId, bot);
                    return true;
                }
            case ADDRESS:
                sendMessage(66, chatId, bot);
                bot.sendMessage(new SendMessage()
                        .setChatId(constDao.select(2))
                        .setText(messageDao.getMessageText(67) + updateMessageText)
                );
                return true;
        }
        return false;
    }
}
