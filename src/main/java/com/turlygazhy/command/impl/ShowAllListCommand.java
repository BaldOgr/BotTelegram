package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 1/4/17.
 */
public class ShowAllListCommand extends Command {
    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
//        boolean admin = userDao.isAdmin(update.getMessage().getChatId());
//        ListDao listDao = factory.getListDao(getListName());
//        Long chatId = update.getMessage().getChatId();
//        List<Message> messages = listDao.readAll();
//        for (Message message : messages) {
//            if (admin) {
//                bot.sendMessage(new SendMessage().setChatId(chatId).setText("===== #" + message.getId() + " ====="));
//            }
//            try {
//                bot.sendPhoto(message.getSendPhoto().setChatId(chatId));
//            } catch (Exception ignored) {
//            }
//            bot.sendMessage(message.getSendMessage().setChatId(chatId));
//        }
//        return true;
        throw new RuntimeException("Not realized class");
    }
}
