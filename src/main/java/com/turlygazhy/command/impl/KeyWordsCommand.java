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
 * Created by user on 1/22/17.
 */
public class KeyWordsCommand extends Command {
    private boolean expectNewKeyword;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        if (expectNewKeyword) {
            String newKeyword = update.getMessage().getText();
            keyWordDao.insert(newKeyword);
            com.turlygazhy.entity.Message message = messageDao.getMessage(26);
            String text = message.getSendMessage().getText();
            text = text.replaceAll("keyWord", newKeyword);
            bot.sendMessage(new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText(text)
            );
            return true;
        }

        Message message = update.getMessage();
        if (message == null) {
            message = update.getCallbackQuery().getMessage();
            String data = update.getCallbackQuery().getData();
            if (data.contains(constDao.select(1))) {
                String keyWord = data.split(":")[1];
                keyWordDao.delete(keyWord);
                String text = messageDao.getMessage(27).getSendMessage().getText();
                text = text.replaceAll("keyWord", keyWord);
                bot.sendMessage(new SendMessage()
                        .setChatId(message.getChatId())
                        .setText(text)
                );
                return true;
            }
            if (data.equals(constDao.select(2))) {
                expectNewKeyword = true;
                sendMessage(25, message.getChatId(), bot);
                return false;
            }
        }

        Long chatId = message.getChatId();
        sendMessage(24, chatId, bot);

        List<String> keyWords = keyWordDao.selectAll();


        for (int i = 0; i < keyWords.size(); i++) {
            String keyWord = keyWords.get(i);
            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(constDao.select(1) + ":" + keyWord);
            button.setCallbackData(constDao.select(1) + ":" + keyWord);
            row.add(button);
            rows.add(row);

            if (i == keyWords.size() - 1) {
                List<InlineKeyboardButton> addRow = new ArrayList<>();
                InlineKeyboardButton addButton = new InlineKeyboardButton();
                addButton.setText(constDao.select(2));
                addButton.setCallbackData(constDao.select(2));
                addRow.add(addButton);
                rows.add(addRow);
            }

            keyboard.setKeyboard(rows);

            bot.sendMessage(new SendMessage()
                    .setChatId(chatId)
                    .setText(keyWord)
                    .setReplyMarkup(keyboard)
            );
        }

        return false;
    }
}
