package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.Message;
import com.turlygazhy.entity.MessageElement;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 1/2/17.
 */
public class ChangeInfoCommand extends Command {
    private String photo;
    private String text;
    private MessageElement expectedMessageElement;
    private boolean needPhoto = true;


    @Override
    public boolean execute(Update update, Bot bot) throws TelegramApiException, SQLException {
        org.telegram.telegrambots.api.objects.Message updateMessage = update.getMessage();
        if (updateMessage == null) {
            updateMessage = update.getCallbackQuery().getMessage();
        }
        Long chatId = updateMessage.getChatId();
        if (expectedMessageElement != null) {
            switch (expectedMessageElement) {
                case PHOTO:
                    try {
                        photo = updateMessage.getPhoto().get(updateMessage.getPhoto().size() - 1).getFileId();
                    } catch (Exception e) {
                        if (update.getCallbackQuery().getData().equals(buttonDao.getButtonText(21))) {
                            needPhoto = false;
                        }
                    }
                    break;
                case TEXT:
                    text = updateMessage.getText();
                    break;
            }
        }
        if (photo == null && needPhoto) {
            Message message = messageDao.getMessage(12);
            SendMessage sendMessage = message.getSendMessage()
                    .setChatId(chatId)
                    .setReplyMarkup(keyboardMarkUpDao.select(message.getKeyboardMarkUpId()));

            bot.sendMessage(sendMessage);
            expectedMessageElement = MessageElement.PHOTO;
            return false;
        }
        if (text == null) {
            Message message = messageDao.getMessage(13);
            SendMessage sendMessage = message.getSendMessage()
                    .setChatId(chatId)
                    .setReplyMarkup(keyboardMarkUpDao.select(message.getKeyboardMarkUpId()));

            bot.sendMessage(sendMessage);
            expectedMessageElement = MessageElement.TEXT;
            return false;
        }

        messageDao.update(messageId, photo, text);
        Message message = messageDao.getMessage(14);
        SendMessage sendMessage = message.getSendMessage().setChatId(chatId)
                .setReplyMarkup(keyboardMarkUpDao.select(message.getKeyboardMarkUpId()));

        bot.sendMessage(sendMessage);

        photo = null;
        text = null;
        expectedMessageElement = null;
        return true;
    }
}