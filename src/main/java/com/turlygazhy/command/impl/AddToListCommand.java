package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.MessageElement;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 1/3/17.
 */
public class AddToListCommand extends Command {
    private String photo;
    private boolean photoAsked = false;
    private String text;
    private MessageElement expectedMessageElement;
    private long keyboardId = 0;
    private boolean photoNeeded;
    private boolean askPhotoNeeded;
    private boolean contactNeeded;
    private boolean nisha;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
//        ListDao listDao = factory.getListDao(getListName());
//
//        if (keyboardId == 0) {
//            ListKeyboardDao listKeyboardDao = factory.getListKeyboardDao();
//            keyboardId = listKeyboardDao.selectKeyboardId(getListName());
//        }
//
//        if (expectedMessageElement != null) {
//            switch (expectedMessageElement) {
//                case PHOTO:
//                    photo = update.getMessage().getPhoto().get(update.getMessage().getPhoto().size() - 1).getFileId();
//                    expectedMessageElement = null;
//                    break;
//                case TEXT:
//                    text = update.getMessage().getText();
//                    expectedMessageElement = null;
//                    break;
//            }
//        }
//
//        Long chatId = update.getMessage().getChatId();
//        if (text == null) {
//            Message message = messageDao.getMessage(27);
//            SendMessage sendMessage = message.getSendMessage()
//                    .setChatId(chatId)
//                    .setReplyMarkup(keyboardMarkUpDao.select(message.getKeyboardMarkUpId()));
//
//            bot.sendMessage(sendMessage);
//            expectedMessageElement = MessageElement.TEXT;
//            return false;
//        }
//
//        if (photo == null) {
//            if (!photoAsked) {
//                photoAsked = true;
//                sendMessage(101, chatId, bot);
//                return false;
//            } else {
//                String text = update.getMessage().getText();
//                if (text.equals(constDao.select(1))) {
//                    Message message = messageDao.getMessage(133);
//                    SendMessage sendMessage = message.getSendMessage()
//                            .setChatId(chatId)
//                            .setReplyMarkup(keyboardMarkUpDao.select(keyboardId));
//
//                    bot.sendMessage(sendMessage);
//                    expectedMessageElement = MessageElement.PHOTO;
//                    return false;
//                }
//                if (text.equals(constDao.select(2))) {
//                    listDao.insert(null, this.text);
//                    Message message = messageDao.getMessage(28);
//                    SendMessage sendMessage = message.getSendMessage().setChatId(chatId).setReplyMarkup(keyboardMarkUpDao.select(keyboardId));
//                    bot.sendMessage(sendMessage);
//                    photo = null;
//                    text = null;
//                    expectedMessageElement = null;
//                    keyboardId = 0;
//                    return true;
//                }
//            }
//        }
//        listDao.insert(photo, text);
//        Message message = messageDao.getMessage(28);
//        SendMessage sendMessage = message.getSendMessage().setChatId(chatId).setReplyMarkup(keyboardMarkUpDao.select(keyboardId));
//        bot.sendMessage(sendMessage);
//
//        photo = null;
//        text = null;
//        expectedMessageElement = null;
//        keyboardId = 0;
//        return true;
        throw new RuntimeException("Not realized class");
    }

    public boolean isPhotoNeeded() {
        return photoNeeded;
    }

    public void setPhotoNeeded(boolean photoNeeded) {
        this.photoNeeded = photoNeeded;
    }

    public boolean isAskPhotoNeeded() {
        return askPhotoNeeded;
    }

    public void setAskPhotoNeeded(boolean askPhotoNeeded) {
        this.askPhotoNeeded = askPhotoNeeded;
    }

    public boolean isContactNeeded() {
        return contactNeeded;
    }

    public void setContactNeeded(boolean contactNeeded) {
        this.contactNeeded = contactNeeded;
    }

    public boolean isNisha() {
        return nisha;
    }

    public void setNisha(boolean nisha) {
        this.nisha = nisha;
    }
}
