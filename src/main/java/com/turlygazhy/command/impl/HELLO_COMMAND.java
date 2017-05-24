package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by lol on 24.05.2017.
 */
public class HELLO_COMMAND extends Command {

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        sendMessage("Hello!", update.getMessage().getChatId(), bot);
        return true;
    }
}
