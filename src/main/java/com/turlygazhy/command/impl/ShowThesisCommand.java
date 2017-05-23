package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.UserReadingResult;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 3/1/17.
 */
public class ShowThesisCommand extends Command {
    private WaitingType waitingType;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        Integer userId = updateMessage.getFrom().getId();
        Long chatId = updateMessage.getChatId();
        String updateMessageText = updateMessage.getText();
        if (waitingType == null) {
            showThesis(bot, userId, chatId);
            waitingType = WaitingType.CHANGE_THESIS;
            return false;
        }
        switch (waitingType) {
            case CHANGE_THESIS:
                if (updateMessageText.equals(buttonDao.getButtonText(27))) {
                    sendMessage(38, chatId, bot);
                    waitingType = WaitingType.NEW_THESIS;
                    return false;
                }
            case NEW_THESIS:
                goalDao.addThesis(updateMessageText, userId);
                showThesis(bot, userId, chatId);
                waitingType = WaitingType.CHANGE_THESIS;
                return false;
        }
        return false;
    }

    private void showThesis(Bot bot, Integer userId, Long chatId) throws SQLException, TelegramApiException {
        UserReadingResult readingResult = goalDao.getReadingResultForUser(userId);
        bot.sendMessage(new SendMessage()
                .setText(memberDao.getName(userId) + ", '" + readingResult.getBookName() + "':\n\n" + readingResult.getThesis().trim())
                .setChatId(chatId)
                .setReplyMarkup(keyboardMarkUpDao.select(10))
        );
    }
}
