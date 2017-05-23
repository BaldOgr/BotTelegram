package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.Goal;
import com.turlygazhy.entity.SavedResult;
import com.turlygazhy.entity.WaitingType;
import com.turlygazhy.tool.Chart;
import com.turlygazhy.tool.DateUtil;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yerassyl_Turlygazhy on 03-Mar-17.
 */
public class ShowChartCommand extends Command {
    private WaitingType waitingType;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy"); //HH = 24h format
    private Date periodStart;
    private Date periodEnd;
    private List<SavedResult> results = null;
    private List<SavedResult> readingResults = null;
    private List<Goal> goals;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        String updateMessageText;
        Integer userId;
        if (updateMessage == null) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            updateMessage = callbackQuery.getMessage();
            updateMessageText = callbackQuery.getData();
            userId = callbackQuery.getFrom().getId();
        } else {
            updateMessageText = updateMessage.getText();
            userId = updateMessage.getFrom().getId();
        }
        Long chatId = updateMessage.getChatId();
        if (waitingType == null) {
            sendMessage(42, chatId, bot);
            waitingType = WaitingType.PERIOD;
            return false;
        }
        switch (waitingType) {
            case PERIOD:
                if (updateMessageText.equals(buttonDao.getButtonText(35))) {
                    results = savedResultsDao.selectThisWeek(userId);
                    readingResults = savedResultsDao.selectThisWeekForReading(userId);
                    if (results.size() == 0 || results.size() == 1) {
                        sendMessage(44, chatId, bot);
                        return false;
                    }
                    waitingType = WaitingType.CHOOSE_GOAL;
                    sendGoals(chatId, bot);
                    return false;
                }
                if (updateMessageText.equals(buttonDao.getButtonText(36))) {
                    results = savedResultsDao.select(userId, getThisMonthFirstDay(), getThisMonthLastDay());
                    readingResults = savedResultsDao.selectForReading(userId, getThisMonthFirstDay(), getThisMonthLastDay());
                    if (results.size() == 0 || results.size() == 1) {
                        sendMessage(44, chatId, bot);
                        return false;
                    }
                    waitingType = WaitingType.CHOOSE_GOAL;
                    sendGoals(chatId, bot);
                    return false;
                }
                if (updateMessageText.equals(buttonDao.getButtonText(37))) {
                    sendMessage(43, chatId, bot);
                    waitingType = WaitingType.PERIOD_START;
                    return false;
                }
                if (results == null) {
                    sendMessage(7, chatId, bot);
                    return true;
                }
                if (results.size() == 0 || results.size() == 1) {
                    sendMessage(44, chatId, bot);
                    return false;
                }
                return false;
            case PERIOD_START:
                try {
                    periodStart = parse(updateMessageText);
                    sendMessage(45, chatId, bot);
                    waitingType = WaitingType.PERIOD_END;
                    return false;
                } catch (ParseException e) {
                    sendMessage(46, chatId, bot);
                    return false;
                }
            case PERIOD_END:
                try {
                    periodEnd = parse(updateMessageText);
                    results = savedResultsDao.select(userId, periodStart, periodEnd);
                    readingResults = savedResultsDao.selectForReading(userId, periodStart, periodEnd);

                    if (results.size() == 0 || results.size() == 1) {
                        sendMessage(44, chatId, bot);
                        waitingType = WaitingType.PERIOD;
                        return false;
                    }
                    waitingType = WaitingType.CHOOSE_GOAL;
                    sendGoals(chatId, bot);
                    return false;
                } catch (ParseException e) {
                    sendMessage(46, chatId, bot);
                    return false;
                }
            case CHOOSE_GOAL:
                for (Goal goal : goals) {
                    if (updateMessageText.contains(goal.getName())) {
                        if (updateMessageText.contains(buttonDao.getButtonText(3))) {
                            sendReadingChart(bot, chatId, readingResults, goal);
                            return true;
                        }
                        sendChart(bot, chatId, results, goal);
                        return false;
                    }
                }
                sendMessage(7, chatId, bot);
                return true;
        }
        return false;
    }

    private void sendReadingChart(Bot bot, Long chatId, List<SavedResult> results, Goal goal) throws TelegramApiException {
        Chart chart = new Chart();
        chart.setFileName(String.valueOf(results.get(0).getUserId()));
        for (SavedResult result : results) {
            chart.addPair(result.getDate(), result.getResult());
        }
        String filePath = chart.getChart(goal.getName());
        File file = new File(filePath);
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file + ".jpg");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        bot.sendPhoto(new SendPhoto()
                .setChatId(chatId)
                .setNewPhoto("photo", fileInputStream)
        );
    }

    private void sendGoals(Long chatId, Bot bot) throws SQLException, TelegramApiException {
        goals = goalDao.selectAllGoals();
        Goal reading = new Goal();
        reading.setName(buttonDao.getButtonText(3));
        goals.add(0, reading);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        int i = 1;
        for (Goal goal : goals) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            String buttonText = i + ") " + goal.getName();
            button.setText(buttonText);
            button.setCallbackData(buttonText);
            row.add(button);
            rows.add(row);
            i++;
        }
        keyboard.setKeyboard(rows);

        bot.sendMessage(new SendMessage()
                .setChatId(chatId)
                .setText(messageDao.getMessageText(47))
                .setReplyMarkup(keyboard)
        );
    }

    private Date parse(String text) throws ParseException {
        dateFormat.setLenient(false); //this will not enable 25:67 for example
        return dateFormat.parse(text);
    }

    private Date getThisMonthLastDay() {
        Date date = new Date();
        int month = date.getMonth();

        while (month == date.getMonth()) {
            date.setDate(date.getDate() + 1);
        }
        date.setDate(date.getDate() - 1);

        return date;
    }

    private Date getThisMonthFirstDay() {
        Date date = new Date();
        date.setDate(1);
        return date;
    }

    private void sendChart(Bot bot, Long chatId, List<SavedResult> results, Goal goal) throws SQLException, TelegramApiException {
        Chart chart = new Chart();
        chart.setFileName(String.valueOf(results.get(0).getUserId()));
        for (SavedResult result : results) {
            if (result.getGoalId() == goal.getId()) {
                chart.addPair(result.getDate(), result.getResult());
            }
        }
        String filePath = chart.getChart(goal.getName());
        File file = new File(filePath);
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file + ".jpg");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        bot.sendPhoto(new SendPhoto()
                .setChatId(chatId)
                .setNewPhoto("photo", fileInputStream)
        );
    }
}
