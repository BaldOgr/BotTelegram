package com.turlygazhy.reminder.timer_task;

import com.turlygazhy.Bot;
import com.turlygazhy.entity.*;
import com.turlygazhy.reminder.Reminder;
import com.turlygazhy.tool.Chart;
import com.turlygazhy.tool.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yerassyl_Turlygazhy on 02-Mar-17.
 */
public class EveryNightTask extends AbstractTask {
    private static final Logger logger = LoggerFactory.getLogger(EveryNightTask.class);


    public EveryNightTask(Bot bot, Reminder reminder) {
        super(bot, reminder);
    }

    @Override
    public void run() {
        logger.info("Start run");
        reminder.setNextNightTask();
        try {
            List<Member> members = memberDao.selectAll();
            SendResultToGroup sendResultToGroup = new SendResultToGroup();
            WeekResult weekResult = new WeekResult();
            MonthResult monthResult = new MonthResult();
            for (Member member : members) {
                try {
                    int groupId = member.getGroupId();
                    Group group = groupDao.select(groupId);
                    long groupChatId = group.getChatId();
                    Integer userId = member.getUserId();
                    List<UserResult> results = goalDao.getForUser(userId);
                    String firstName = member.getFirstName();
                    String resultText = getResultText(results);

                    UserReadingResult reading = goalDao.getReadingResultForUser(userId);
                    resultText = "<b>" + firstName + "</b>" + "\n" + buttonDao.getButtonText(3) + ": " + reading.getCompleted() + "/" + reading.getAim() + "\n" + resultText.trim()
                            + "\n========================";

                    sendResultToGroup.addResult(groupChatId, resultText, bot);
                    savedResultsDao.insert(userId, results, reading);
                    if (DateUtil.isNewWeek()) {
                        int readingResult = weekResult.analyze(reading);
                        weekResult.analyze(member, results, goalDao, readingResult);
                        goalDao.resetResults(userId);
                        goalDao.resetReadingCompleted(userId);
                    }
                    if (DateUtil.isNewMonth()) {
                        monthResult.analyze(member, savedResultsDao, goalDao);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (DateUtil.isNewWeek()) {
                weekResult.send(bot, messageDao, groupDao);
            }
            if (DateUtil.isNewMonth()) {
                monthResult.send(bot, messageDao, groupDao);
                sendMonthChart(bot);
            }
        } catch (SQLException | TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMonthChart(Bot bot) throws SQLException {
        Chart chart = new Chart();

        List<Goal> goals = goalDao.selectAllGoals();
        File file;
        FileInputStream fileInputStream;
        for (Goal goal : goals) {
            List<SavedResult> savedResults = savedResultsDao.selectForGoalLastMonth(goal.getId());
            String filePath = chart.getPieChart(goal.getName(), parseResults(savedResults));
            file = new File(filePath);
            try {
                fileInputStream = new FileInputStream(file + ".jpg");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                bot.sendPhoto(new SendPhoto()
                        .setChatId(293188753L)
                        .setNewPhoto("photo", fileInputStream)
                );
            } catch (TelegramApiException ignored) {
            }
        }


        //todo не забыть чтение
//        chart.getChart()
    }

    private Map<String, List<SavedResult>> parseResults(List<SavedResult> savedResults) {
        Map<Integer, List<SavedResult>> userResults = new HashMap<>();
        for (SavedResult savedResult : savedResults) {
            Integer userId = savedResult.getUserId();
            List<SavedResult> results = userResults.get(userId);
            if (results == null) {
                results = new ArrayList<>();
                userResults.put(userId, results);
            }
            results.add(savedResult);
        }
        Map<String, List<SavedResult>> userNameAndResults = new HashMap<>();
        for (Map.Entry<Integer, List<SavedResult>> entry : userResults.entrySet()) {
            try {
                userNameAndResults.put(memberDao.getName(entry.getKey()), entry.getValue());
            } catch (SQLException ignored) {
            }
        }
        return userNameAndResults;
    }

    private String getResultText(List<UserResult> results) throws SQLException {
        String result = "";
        for (UserResult userResult : results) {
            Goal goal = goalDao.select(userResult.getGoalId());
            result = result + "\n" + goal.getName() + ": " + userResult.getCompleted() + "/" + goal.getAim();
        }
        return result;
    }
}
