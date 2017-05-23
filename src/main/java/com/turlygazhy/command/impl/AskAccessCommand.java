package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.turlygazhy.entity.Const.ACCESS_TO_BOT;

/**
 * Created by user on 2/23/17.
 */
public class AskAccessCommand extends Command {
    private WaitingType waitingType;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        Long chatId = updateMessage.getChatId();
        if (waitingType == null) {
            sendMessage(1, chatId, bot);
            waitingType = WaitingType.CONTACT;
            return false;
        }
        switch (waitingType) {
            case CONTACT:
                Contact contact = updateMessage.getContact();
                String userName = updateMessage.getFrom().getUserName();
                memberDao.insert(chatId, contact, userName);

                bot.sendMessage(new SendMessage()
                        .setChatId(chatId)
                        .setText(messageDao.getMessageText(20))
                        .setReplyMarkup(new ReplyKeyboardRemove())
                );

                String textToAdmin = messageDao.getMessageText(21) + "\nName: " + contact.getFirstName() +
                        "\nNumber: " + contact.getPhoneNumber();
                if (userName != null) {
                    textToAdmin = textToAdmin + "\nTelegram: @" + userName;
                }
                bot.sendMessage(new SendMessage()
                        .setChatId(getAdminChatId())
                        .setText(textToAdmin)
                        .setReplyMarkup(getKeyboard(contact.getUserID()))
                );
                return true;
        }
        return false;
    }

    private InlineKeyboardMarkup getKeyboard(Integer userID) throws SQLException {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        String buttonText = buttonDao.getButtonText(22);
        button.setText(buttonText);
        button.setCallbackData(buttonText + " " + ACCESS_TO_BOT + ":" + userID);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        String buttonText1 = buttonDao.getButtonText(23);
        button1.setText(buttonText1);
        button1.setCallbackData(buttonText1 + " " + ACCESS_TO_BOT + ":" + userID);

        row.add(button);
        row.add(button1);
        rows.add(row);

        keyboard.setKeyboard(rows);
        return keyboard;
    }
}
