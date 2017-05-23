package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by Yerassyl_Turlygazhy on 2/15/2017.
 */
public class AddPlanCommand extends Command {
    private WaitingType waitingType;
    private String planName;
    private int goal;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        Long chatId = updateMessage.getChatId();
        String updateMessageText = updateMessage.getText();
        if (waitingType == null) {
            sendMessage(3, chatId, bot);
            waitingType = WaitingType.PLAN_NAME;
            return false;
        }

        switch (waitingType) {
            case PLAN_NAME:
                planName = updateMessageText;
                sendMessage(4, chatId, bot);
                waitingType = WaitingType.PLAN_GOAL;
                return false;
            case PLAN_GOAL:
                try {
                    goal = Integer.parseInt(updateMessageText);
                } catch (Exception e) {
                    //если это не число
                }
        }
        return false;
    }
}
