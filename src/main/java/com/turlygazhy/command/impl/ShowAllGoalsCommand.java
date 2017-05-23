package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.Goal;
import com.turlygazhy.entity.UserReadingResult;
import com.turlygazhy.entity.UserResult;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2/24/17.
 */
public class ShowAllGoalsCommand extends Command {
    private WaitingType waitingType;
    private int userResultId = 0;
    private int goalId;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        Long chatId = updateMessage.getChatId();
        User user = updateMessage.getFrom();
        Integer userId = user.getId();
        String updateMessageText = updateMessage.getText();
        if (waitingType == null) {
            List<UserResult> userResults = goalDao.getForUser(userId);
            String text = messageDao.getMessageText(2);
            UserReadingResult reading = goalDao.getReadingResultForUser(userId);

            ReplyKeyboardMarkup keyboardMarkup = getKeyboardForGoals();

            for (UserResult result : userResults) {
                Goal goal = goalDao.select(result.getGoalId());
                text = text + "\n" + goal.getName() + ": " + result.getCompleted() + "/" + goal.getAim();
                addToKeyboard(keyboardMarkup, goal.getName());
            }
            text = text + "\n" + buttonDao.getButtonText(3) + ": " + reading.getCompleted() + "/" + reading.getAim();
            addToKeyboardReading(keyboardMarkup);
            bot.sendMessage(new SendMessage()
                    .setChatId(chatId)
                    .setReplyMarkup(keyboardMarkup)
                    .setText(text)
            );
            waitingType = WaitingType.GOAL_NAME;
            return false;
        }

        switch (waitingType) {
            case GOAL_NAME:
                showGoal(bot, chatId, userId, updateMessageText);
                return false;
            case CHANGE_GOAL:
                bot.sendMessage(new SendMessage()
                        .setText(messageDao.getMessageText(8))
                        .setChatId(chatId)
                );
                waitingType = WaitingType.GOAL_COMPLETED;
                return false;
            case GOAL_COMPLETED:
                try {
                    goalDao.inputResult(userResultId, Integer.parseInt(updateMessageText));
                    showGoal(bot, chatId, userId, goalDao.getGoalName(goalId));
                    return false;
                } catch (NumberFormatException e) {
                    sendMessage(30, chatId, bot);
                    return false;
                }
        }

        return false;
    }

    private void addToKeyboardReading(ReplyKeyboardMarkup keyboardMarkup) throws SQLException {
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        KeyboardRow keyboardButtons = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton(buttonDao.getButtonText(3));
        keyboardButtons.add(keyboardButton);
        keyboard.add(1, keyboardButtons);
    }

    /**
     * @return false if not in time limit
     */
    private boolean showGoal(Bot bot, Long chatId, Integer userId, String updateMessageText) throws SQLException, TelegramApiException {
        List<UserResult> userResults = goalDao.getForUser(userId);
        for (UserResult userResult : userResults) {
            int goalId = userResult.getGoalId();
            Goal goal = goalDao.select(goalId);
            if (goal.getName().equals(updateMessageText)) {
                if (goal.isTimeLimit()) {
                    String startTime = goal.getStartTime();
                    String endTime = goal.getEndTime();
                    boolean checkTime = checkTime(startTime, endTime);
                    if (!checkTime) {
                        bot.sendMessage(new SendMessage()
                                .setChatId(chatId)
                                .setText(messageDao.getMessageText(32) + startTime + "-" + endTime)
                        );
                        return false;
                    }
                }
                bot.sendMessage(new SendMessage()
                        .setText(goal.getName() + ": " + userResult.getCompleted() + "/" + goal.getAim())
                        .setChatId(chatId)
                        .setReplyMarkup(keyboardMarkUpDao.select(3))
                );
                userResultId = userResult.getId();
                waitingType = WaitingType.CHANGE_GOAL;
                this.goalId = goalId;
                return true;
            }
        }
        return true;
    }

    private boolean checkTime(String startTime, String endTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); //HH = 24h format
            dateFormat.setLenient(false); //this will not enable 25:67 for example

            Date now = new Date();
            int nowHours = now.getHours();
            int nowMinutes = now.getMinutes();

            Date start = dateFormat.parse(startTime);
            int startHours = start.getHours();
            int startMinutes = start.getMinutes();

            Date end = dateFormat.parse(endTime);
            int endHours = end.getHours();
            int endMinutes = end.getMinutes();

            if (startHours < nowHours) {
                if (endHours > nowHours) {
                    return true;
                } else {
                    if (endHours == nowHours) {
                        if (endMinutes >= nowMinutes) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                if (startHours == nowHours) {
                    if (startMinutes <= nowMinutes) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void addToKeyboard(ReplyKeyboardMarkup keyboardMarkup, String name) {
        List<KeyboardRow> keyboard = keyboardMarkup.getKeyboard();
        KeyboardRow keyboardButtons = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton(name);
        keyboardButtons.add(keyboardButton);
        keyboard.add(keyboard.size() - 1, keyboardButtons);
    }

    private ReplyKeyboardMarkup getKeyboardForGoals() throws SQLException {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rowsList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText(buttonDao.getButtonText(10));
        keyboardRow.add(button);
        rowsList.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(rowsList);
        return replyKeyboardMarkup;
    }
}
