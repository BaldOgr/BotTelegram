package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by user on 2/24/17.
 */
public class AddGoalCommand extends Command {
    private WaitingType waitingType;
    private String goalName;
    private int goal;
    private String startTime;
    private String endTime;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        if (updateMessage == null) {
            updateMessage = update.getCallbackQuery().getMessage();
        }
        Long chatId = updateMessage.getChatId();
        if (waitingType == null) {
            sendMessage(26, chatId, bot);
            waitingType = WaitingType.GOAL_NAME;
            return false;
        }
        String text = updateMessage.getText();
        switch (waitingType) {
            case GOAL_NAME:
                goalName = text;
                sendMessage(27, chatId, bot);
                waitingType = WaitingType.GOAL_DIGIT;
                return false;
            case GOAL_DIGIT:
                try {
                    goal = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    sendMessage(30, chatId, bot);
                    return false;
                }
                sendMessage(29, chatId, bot);
                waitingType = WaitingType.AVAILABLE_TIME_START;
                return false;
            case AVAILABLE_TIME_START:
                CallbackQuery callbackQuery = update.getCallbackQuery();
                if (callbackQuery != null) {
                    if (callbackQuery.getData().equals(buttonDao.getButtonText(25))) {
                        goalDao.insert(goalName, goal);
                        sendMessage(28, chatId, bot);
                        return true;
                    }
                }
                boolean validateTime = validateTime(text);
                if (validateTime) {
                    startTime = text;
                    waitingType = WaitingType.AVAILABLE_TIME_END;
                    sendMessage(31, chatId, bot);
                    return false;
                } else {
                    sendMessage(30, chatId, bot);
                    return false;
                }
            case AVAILABLE_TIME_END:
                boolean validateEndTime = validateTime(text);
                if (validateEndTime) {
                    endTime = text;
                } else {
                    sendMessage(30, chatId, bot);
                    return false;
                }
                goalDao.insert(goalName, goal, startTime, endTime);
                sendMessage(28, chatId, bot);
                return true;
        }
        return false;
    }
}
