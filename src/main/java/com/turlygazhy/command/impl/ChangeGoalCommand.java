package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.dao.impl.KeyboardMarkUpDao;
import com.turlygazhy.entity.Goal;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Yerassyl_Turlygazhy on 07-Mar-17.
 */
public class ChangeGoalCommand extends Command {
    private WaitingType waitingType;
    private String goalName;
    private String startTime;
    private String endTime;
    private boolean isBook;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        String updateMessageText;
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (updateMessage == null) {
            updateMessage = callbackQuery.getMessage();
            updateMessageText = callbackQuery.getData();
        } else {
            updateMessageText = updateMessage.getText();
        }
        Long chatId = updateMessage.getChatId();
        if (waitingType == null) {
            bot.sendMessage(new SendMessage()
                    .setChatId(chatId)
                    .setText(messageDao.getMessageText(47))
                    .setReplyMarkup(getKeyboard())
            );
            waitingType = WaitingType.GOAL_NAME;
            return false;
        }
        switch (waitingType) {
            case GOAL_NAME:
                if (updateMessageText.contains(buttonDao.getButtonText(3))) {
                    isBook = true;
                }
                setGoalName(updateMessageText);
                ReplyKeyboardMarkup keyboardMarkup = (ReplyKeyboardMarkup) keyboardMarkUpDao.select(13);
                if (isBook) {
                    for (KeyboardRow keyboardButtons : keyboardMarkup.getKeyboard()) {
                        Iterator<KeyboardButton> iterator = keyboardButtons.iterator();
                        while (iterator.hasNext()) {
                            KeyboardButton button = iterator.next();
                            if (button.getText().equals(buttonDao.getButtonText(41))) {
                                iterator.remove();
                                break;
                            }
                        }
                    }
                }
                bot.sendMessage(new SendMessage()
                        .setChatId(chatId)
                        .setText(messageDao.getMessageText(51))
                        .setReplyMarkup(keyboardMarkup)
                );
                waitingType = WaitingType.CHANGE_TYPE;
                return false;
            case CHANGE_TYPE:
                if (updateMessageText.equals(buttonDao.getButtonText(39))) {
                    sendMessage(messageDao.getMessageText(52) + " " + goalName, chatId, bot);
                    waitingType = WaitingType.GOAL_NEW_NAME;
                    return false;
                }
                if (updateMessageText.equals(buttonDao.getButtonText(40))) {
                    sendMessage(messageDao.getMessageText(53) + " " + goalName, chatId, bot);
                    waitingType = WaitingType.GOAL_DIGIT;
                    return false;
                }
                if (updateMessageText.equals(buttonDao.getButtonText(41))) {
                    sendMessage(29, chatId, bot);
                    waitingType = WaitingType.AVAILABLE_TIME_START;
                    return false;
                }
                sendMessage(7, chatId, bot);
                return true;
            case GOAL_NEW_NAME:
                if (isBook) {
                    buttonDao.updateButtonText(3, updateMessageText);
                    setGoalName(updateMessageText);
                    sendMessage(54, chatId, bot);
                    waitingType = WaitingType.CHANGE_TYPE;
                    return false;
                }
                Goal goal = goalDao.select(goalName);
                setGoalName(updateMessageText);
                goal.setName(goalName);
                goalDao.update(goal);
                sendMessage(54, chatId, bot);
                waitingType = WaitingType.CHANGE_TYPE;
                return false;
            case GOAL_DIGIT:
                try {
                    int aim = Integer.parseInt(updateMessageText);
                    if (isBook) {
                        goalDao.changeBookAim(aim);
                        sendMessage(54, chatId, bot);
                        waitingType = WaitingType.CHANGE_TYPE;
                        return false;
                    }
                    Goal goal1 = goalDao.select(goalName);
                    goal1.setAim(aim);
                    goalDao.update(goal1);
                    sendMessage(54, chatId, bot);
                    waitingType = WaitingType.CHANGE_TYPE;
                    return false;
                } catch (NumberFormatException e) {
                    sendMessage(30, chatId, bot);
                    return false;
                }
            case AVAILABLE_TIME_START:
                if (updateMessageText.equals(buttonDao.getButtonText(25))) {
                    Goal goal1 = goalDao.select(goalName);
                    goal1.setTimeLimit(false);
                    goal1.setStartTime(null);
                    goal1.setEndTime(null);
                    goalDao.update(goal1);
                    sendMessage(54, chatId, bot);
                    return true;
                }
                boolean validateTime = validateTime(updateMessageText);
                if (validateTime) {
                    startTime = updateMessageText;
                    waitingType = WaitingType.AVAILABLE_TIME_END;
                    sendMessage(31, chatId, bot);
                    return false;
                } else {
                    sendMessage(30, chatId, bot);
                    return false;
                }
            case AVAILABLE_TIME_END:
                boolean validateEndTime = validateTime(updateMessageText);
                if (validateEndTime) {
                    endTime = updateMessageText;
                } else {
                    sendMessage(30, chatId, bot);
                    return false;
                }
                Goal goal1 = goalDao.select(goalName);
                goal1.setTimeLimit(true);
                goal1.setStartTime(startTime);
                goal1.setEndTime(endTime);
                goalDao.update(goal1);
                sendMessage(54, chatId, bot);
                return true;
        }
        return false;
    }

    private ReplyKeyboard getKeyboard() throws SQLException {
        String change = messageDao.getMessageText(50);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rowsList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        rowsList.add(keyboardRow);
        keyboardRow.add(new KeyboardButton(change + " " + buttonDao.getButtonText(3)));

        List<Goal> goals = goalDao.selectAllGoals();
        for (Goal goal : goals) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(change + " " + goal.getName()));
            rowsList.add(row);
        }

        KeyboardRow back = new KeyboardRow();
        back.add(new KeyboardButton(buttonDao.getButtonText(42)));

        rowsList.add(back);
        replyKeyboardMarkup.setKeyboard(rowsList);
        return replyKeyboardMarkup;
    }

    public void setGoalName(String goalName) throws SQLException {
        this.goalName = goalName.replaceAll(messageDao.getMessageText(50), "").trim();
    }
}
