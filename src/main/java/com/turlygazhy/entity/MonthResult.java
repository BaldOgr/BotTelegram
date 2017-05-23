package com.turlygazhy.entity;

import com.turlygazhy.Bot;
import com.turlygazhy.dao.GoalDao;
import com.turlygazhy.dao.impl.GroupDao;
import com.turlygazhy.dao.impl.MessageDao;
import com.turlygazhy.dao.impl.SavedResultsDao;
import com.turlygazhy.tool.DateUtil;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yerassyl_Turlygazhy on 29-Mar-17.
 */
public class MonthResult {
    private List<Member> fall = new ArrayList<>();

    public void analyze(Member member, SavedResultsDao savedResultsDao, GoalDao goalDao) {
        List<Week> lastMonthWeeks = DateUtil.getLastMonthWeeks();
        int fallCountForMonth = 0;
        for (Week week : lastMonthWeeks) {
            try {
                List<SavedResult> results = savedResultsDao.select(member.getUserId(), week.getMonday(), week.getSunday());
                int fallCount = 0;
                for (SavedResult result : results) {
                    Goal goal = goalDao.select(result.getGoalId());
                    if (!(result.getResult() >= goal.getAim())) {
                        fallCount++;
                    }
                    if (fallCount >= 3) {
                        fallCountForMonth++;
                        break;
                    }
                }
            } catch (SQLException ignored) {
            }
        }
        if (fallCountForMonth > 0) {
            member.setFallsInThisMonth(fallCountForMonth);
            fall.add(member);
        }
    }

    public void send(Bot bot, MessageDao messageDao, GroupDao groupDao) {
        String result = "";
        try {
            result = messageDao.getMessageText(58);
        } catch (SQLException ignored) {
        }
        if (fall.size() == 0) {
            return;
        }
        Long groupChatId = 0L;
        for (Member member : fall) {
            result = result + "\n" + member.getFirstName() + " - " + member.getFallsInThisMonth();
            if (groupChatId == 0) {
                try {
                    groupChatId = groupDao.select(member.getGroupId()).getChatId();
                } catch (SQLException ignored) {
                }
            }
        }
        try {
            bot.sendMessage(new SendMessage()
                    .setChatId(groupChatId)
                    .setText(result)
            );

        } catch (TelegramApiException ignored) {
        }
    }
}
