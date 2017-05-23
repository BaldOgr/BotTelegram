package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.UserReadingResult;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 2/18/17.
 */
public class ReadingCommand extends Command {
    private WaitingType waitingType;
    private String bookName;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        String updateMessageText;
        Integer userId;
        if (updateMessage == null) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            updateMessage = callbackQuery.getMessage();
            updateMessageText = callbackQuery.getData();
            userId = callbackQuery.getFrom().getId();
        } else {
            updateMessageText = updateMessage.getText();
            userId = updateMessage.getFrom().getId();
        }
        Long chatId = updateMessage.getChatId();
        if (waitingType == null) {
            UserReadingResult readingResult = goalDao.getReadingResultForUser(userId);
            String bookName = readingResult.getBookName();
            if (bookName == null || bookName.equals("")) {
                sendMessage(33, chatId, bot);
                waitingType = WaitingType.BOOK_NAME;
                return false;
            }
            showRead(bot, chatId, readingResult, bookName);
            waitingType = WaitingType.CHANGE_GOAL;
            return false;
        }
        switch (waitingType) {
            case BOOK_NAME:
                bot.sendMessage(new SendMessage()
                        .setChatId(chatId)
                        .setText(messageDao.getMessageText(34) + "\n'" + updateMessageText + "'")
                        .setReplyMarkup(keyboardMarkUpDao.select(11))
                );
                bookName = updateMessageText;
                waitingType = WaitingType.CHECK_BOOK_NAME;
                return false;
            case CHECK_BOOK_NAME:
                if (updateMessageText.equals(buttonDao.getButtonText(32))) {
                    goalDao.setBookName(userId, bookName);

                    UserReadingResult readingResult = goalDao.getReadingResultForUser(userId);
                    showRead(bot, chatId, readingResult, bookName);

                    waitingType = WaitingType.CHANGE_GOAL;
                    return false;
                }
                if (updateMessageText.equals(buttonDao.getButtonText(33))) {
                    sendMessage(33, chatId, bot);
                    waitingType = WaitingType.BOOK_NAME;
                    return false;
                }
            case CHANGE_GOAL:
                if (updateMessageText.equals(buttonDao.getButtonText(14))) {
                    bot.sendMessage(new SendMessage()
                            .setChatId(chatId)
                            .setText(messageDao.getMessageText(8))
                    );
                    waitingType = WaitingType.GOAL_COMPLETED;
                    return false;
                }
                if (updateMessageText.equals(buttonDao.getButtonText(29))) {
                    UserReadingResult readingResult = goalDao.getReadingResultForUser(userId);
                    thesisDao.insert(readingResult, memberDao.getName(userId));
                    goalDao.resetReading(userId);
                    sendMessage(33, chatId, bot);
                    waitingType = WaitingType.BOOK_NAME;
                    return false;
                }
                sendMessage(7, chatId, bot);
                return true;
            case GOAL_COMPLETED:
                try {
                    UserReadingResult readingResult = goalDao.inputReadingResult(userId, Integer.parseInt(updateMessageText));
                    showRead(bot, chatId, readingResult, readingResult.getBookName());
                    waitingType = WaitingType.CHANGE_GOAL;
                    return false;
                } catch (NumberFormatException e) {
                    sendMessage(30, chatId, bot);
                    return false;
                }
        }
        return false;
    }

    private void showRead(Bot bot, Long chatId, UserReadingResult readingResult, String bookName) throws SQLException, TelegramApiException {
        String text = bookName + "\n" + buttonDao.getButtonText(3) + ": " + readingResult.getCompleted() + "/" + readingResult.getAim();
        bot.sendMessage(new SendMessage()
                .setChatId(chatId)
                .setText(text)
                .setReplyMarkup(keyboardMarkUpDao.select(9))
        );
    }
}
