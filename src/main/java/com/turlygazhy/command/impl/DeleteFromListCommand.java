package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 1/4/17.
 */
public class DeleteFromListCommand extends Command {
    private boolean expectId;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
//        ListDao listDao = factory.getListDao(getListName());
//        Long chatId = update.getMessage().getChatId();
//        if (!expectId) {
//            sendMessage(64, chatId, bot);
//            expectId = true;
//            return false;
//        } else {
//            try {
//                long id = Long.parseLong(update.getMessage().getText());
//                boolean deleted = listDao.delete(id);
//                if (!deleted) {
//                    sendMessage(67, chatId, bot);
//                    return false;
//                }
//                sendMessage(65, chatId, bot);
//                expectId = false;
//            } catch (Exception e) {
//                Message message = messageDao.getMessage(66);
//                SendMessage sendMessage = message.getSendMessage();
//                sendMessage.setChatId(chatId);
//                sendMessage.setReplyMarkup(keyboardMarkUpDao.select(message.getKeyboardMarkUpId()));
//                bot.sendMessage(sendMessage);
//                expectId = false;
//            }
//
//            return true;
        throw new RuntimeException("Not realized class");

    }
}
