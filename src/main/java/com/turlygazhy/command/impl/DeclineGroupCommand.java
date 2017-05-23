package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import org.telegram.telegrambots.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 2/22/17.
 */
public class DeclineGroupCommand extends Command {
    private final long chatId;

    public DeclineGroupCommand(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        if (updateMessage == null) {
            updateMessage = update.getCallbackQuery().getMessage();
        }
        sendMessage(18, chatId, bot);
        bot.leaveChat(new LeaveChat()
                .setChatId(chatId)
        );
        sendMessage(19, updateMessage.getChatId(), bot);
        return false;
    }
}
