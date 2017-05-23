package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.Message;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 1/21/17.
 */
public class CollectInfoCommand extends Command {
    private String nisha;
    private String naviki;
    private Contact contact;
    private WaitingType waitingType;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        org.telegram.telegrambots.api.objects.Message updateMessage = update.getMessage();
        String text;
        if (updateMessage == null) {
            updateMessage = update.getCallbackQuery().getMessage();
            text = update.getCallbackQuery().getData();
        } else {
            text = updateMessage.getText();
        }

        if (waitingType == null) {
//            sendMessage();
        }

        switch (waitingType) {
            case NISHA:
                if (text.equals(buttonDao.getButtonText(3))) {
                    nisha = "no info";
                    sendMessage(4, updateMessage.getChatId(), bot);
                    waitingType = WaitingType.NAVIKI;
                    return false;
                }
                if (text.equals(buttonDao.getButtonText(4))) {
                    CallbackQuery callback = update.getCallbackQuery();
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setChatId(callback.getMessage().getChatId());
                    editMessageText.setMessageId(callback.getMessage().getMessageId());
                    Message message = messageDao.getMessage(2);
                    editMessageText.setText(callback.getMessage().getText() + "\n\n" + message.getSendMessage().getText());
                    editMessageText.setReplyMarkup((InlineKeyboardMarkup) keyboardMarkUpDao.select(message.getKeyboardMarkUpId()));
                    bot.editMessageText(editMessageText);
                    return false;
                }
                nisha = updateMessage.getText();
                sendMessage(4, updateMessage.getChatId(), bot);
                waitingType = WaitingType.NAVIKI;
                return false;
            case NAVIKI:
                if (text.equals(buttonDao.getButtonText(5))) {
                    CallbackQuery callback = update.getCallbackQuery();
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setChatId(callback.getMessage().getChatId());
                    editMessageText.setMessageId(callback.getMessage().getMessageId());
                    Message message = messageDao.getMessage(5);
                    editMessageText.setText(callback.getMessage().getText() + "\n\n" + message.getSendMessage().getText());
                    editMessageText.setReplyMarkup((InlineKeyboardMarkup) keyboardMarkUpDao.select(message.getKeyboardMarkUpId()));
                    bot.editMessageText(editMessageText);
                    return false;
                }
                if (text.equals(buttonDao.getButtonText(6))) {
                    naviki = "no info";
                    sendMessage(6, updateMessage.getChatId(), bot);
                    waitingType = WaitingType.CONTACT;
                    return false;
                }
                naviki = updateMessage.getText();
                sendMessage(6, updateMessage.getChatId(), bot);
                waitingType = WaitingType.CONTACT;
                return false;
            case CONTACT:
                contact = updateMessage.getContact();
                memberDao.insert(nisha, naviki, contact, updateMessage.getChatId(), updateMessage.getFrom().getUserName());
                sendMessage(7, updateMessage.getChatId(), bot);
                return true;
        }

        if (text.equals(buttonDao.getButtonText(2))) {
            Message message = messageDao.getMessage(3);
            bot.sendMessage(message.getSendMessage()
                    .setChatId(updateMessage.getChatId())
                    .setReplyMarkup(keyboardMarkUpDao.select(message.getKeyboardMarkUpId()))
            );
            waitingType = WaitingType.NISHA;
            return false;
        }

        return false;
    }
}
