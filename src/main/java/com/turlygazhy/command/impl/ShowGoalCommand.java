package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 2/24/17.
 */
public class ShowGoalCommand extends Command {
    private int userResultId;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        return false;
    }
}
