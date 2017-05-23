package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/22/17.
 */
public class WorkWithGroupCommand extends Command {
    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        Long chatId = updateMessage.getChatId();
        String title = updateMessage.getChat().getTitle();
        boolean chatRegistered = groupDao.isChatRegistered(chatId);
        if (!chatRegistered) {
            groupDao.insert(chatId, title);
            bot.sendMessage(new SendMessage()
                    .setChatId(getAdminChatId())
                    .setText(messageDao.getMessageText(16) + title)
                    .setReplyMarkup(getKeyboard(chatId))
            );
        } else {
            groupDao.checkTitle(chatId, title);
        }
        return true;
    }

    private InlineKeyboardMarkup getKeyboard(Long chatId) throws SQLException {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        String buttonText = buttonDao.getButtonText(22);
        button.setText(buttonText);
        button.setCallbackData(buttonText + ":" + chatId);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        String buttonText1 = buttonDao.getButtonText(23);
        button1.setText(buttonText1);
        button1.setCallbackData(buttonText1 + ":" + chatId);

        row.add(button);
        row.add(button1);
        rows.add(row);

        keyboard.setKeyboard(rows);
        return keyboard;
    }
}
