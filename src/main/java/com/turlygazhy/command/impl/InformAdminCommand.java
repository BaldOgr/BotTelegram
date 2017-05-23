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
 * Created by user on 1/5/17.
 */
public class InformAdminCommand extends Command {
    private String photo;
    private boolean photoAsked = false;
    private String text;
    private MessageElement expectedMessageElement;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        org.telegram.telegrambots.api.objects.Message updateMessage = update.getMessage();
        if (updateMessage == null) {
            updateMessage = update.getCallbackQuery().getMessage();
        }
        if (expectedMessageElement != null) {
            switch (expectedMessageElement) {
                case PHOTO:
                    photo = updateMessage.getPhoto().get(updateMessage.getPhoto().size() - 1).getFileId();
                    expectedMessageElement = null;
                    break;
                case TEXT:
                    text = updateMessage.getText();
                    expectedMessageElement = null;
                    break;
            }
        }

        Long chatId = updateMessage.getChatId();
        if (text == null) {
            Message message = messageDao.getMessage(12);
            SendMessage sendMessage = message.getSendMessage()
                    .setChatId(chatId)
                    .setReplyMarkup(keyboardMarkUpDao.select(message.getKeyboardMarkUpId()));

            bot.sendMessage(sendMessage);
            expectedMessageElement = MessageElement.TEXT;
            return false;
        }

        if (photo == null) {
            if (!photoAsked) {
                photoAsked = true;
                sendMessage(13, chatId, bot);
                return false;
            } else {
                String text = update.getCallbackQuery().getData();
                if (text.equals(buttonDao.getButtonText(17))) {
                    Message message = messageDao.getMessage(14);
                    SendMessage sendMessage = message.getSendMessage()
                            .setChatId(chatId)
                            .setReplyMarkup(keyboardMarkUpDao.select(message.getKeyboardMarkUpId()));

                    bot.sendMessage(sendMessage);
                    expectedMessageElement = MessageElement.PHOTO;
                    return false;
                }
                if (text.equals(buttonDao.getButtonText(18))) {
                    sendMessageToAdmin(15, bot);
                    sendMessageToAdmin(this.text, bot);
                    sendMessage(16, chatId, bot);
                    photo = null;
                    text = null;
                    expectedMessageElement = null;
                    return true;
                }
            }
        }

        sendMessageToAdmin(15, bot);
        sendPhotoToAdmin(photo, bot);
        sendMessageToAdmin(text, bot);
        sendMessage(16, chatId, bot);

        photo = null;
        text = null;
        expectedMessageElement = null;
        return true;
    }
}
