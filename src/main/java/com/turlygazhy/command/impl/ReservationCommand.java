package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/26/17.
 */
public class ReservationCommand extends Command {

    private static final int TODAY = 0;
    private static final int TOMORROW = 1;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message message = update.getMessage();
        if (message == null) {
            //перед тем как засетать в бд нужно проверить не сделал ли это то кто то быстрее, если да то извиняй ты не успел
        }
        String text = message.getText();
        Long chatId = message.getChatId();
        if (text.equals(buttonDao.getButtonText(4))) {
            boolean chooseTimeFor = chooseTimeFor(TODAY, bot, chatId);
            if (!chooseTimeFor) {
                sendMessage(5, chatId, bot);
                return true;
            }
            return false;
        }
        if (text.equals(buttonDao.getButtonText(5))) {
            boolean chooseTimeFor = chooseTimeFor(TOMORROW, bot, chatId);
            if (!chooseTimeFor) {
                sendMessage(5, chatId, bot);
                return true;
            }
            return false;
        }

        sendMessage(2, chatId, bot);
        return false;
    }

    /**
     * @return is there available time
     */
    private boolean chooseTimeFor(int day, Bot bot, long chatId) throws SQLException, TelegramApiException {
        List<String> availableTime = reservationDao.selectAvailableTime(day);

        if (availableTime.size() == 0) {
            return false;
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rowsList = new ArrayList<>();


        for (int i = 0; i < availableTime.size(); ) {

            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardButton button = new KeyboardButton();
            KeyboardButton button2 = new KeyboardButton();
            button.setText(availableTime.get(i));
            if (i + 1 < availableTime.size()) {
                i++;
            }
            button2.setText(availableTime.get(i));
            if (i + 1 < availableTime.size()) {
                i++;
            }
            keyboardRow.add(button);
            keyboardRow.add(button2);
            rowsList.add(keyboardRow);
            if (i == 25) {
                break;
            }
        }
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton(buttonDao.getButtonText(6)));
        rowsList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rowsList);


        bot.sendMessage(new SendMessage()
                .setChatId(chatId)
                .setText(messageDao.getMessage(4).getSendMessage().getText())
                .setReplyMarkup(replyKeyboardMarkup)
        );
        return true;
    }

}
