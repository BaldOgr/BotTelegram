package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.Const;
import com.turlygazhy.entity.Group;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/23/17.
 */
public class GiveAccessToUserCommand extends Command {
    private final Integer userId;

    public GiveAccessToUserCommand(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        bot.sendMessage(new SendMessage()
                .setChatId(getAdminChatId())
                .setText(messageDao.getMessageText(23) + memberDao.getName(userId))
                .setReplyMarkup(getKeyboard(userId))
        );

        return true;
    }

    private InlineKeyboardMarkup getKeyboard(Integer userId) throws SQLException {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        List<Group> groups = groupDao.selectAll();

        if (groups.size() == 0) {
            return null;
        }

        for (Group group : groups) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(group.getTitle());
            button.setCallbackData(userId + Const.TO_GROUP + group.getId());
            row.add(button);
        }

        rows.add(row);

        keyboard.setKeyboard(rows);
        return keyboard;
    }
}
