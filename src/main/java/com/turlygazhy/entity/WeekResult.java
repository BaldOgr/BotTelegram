package com.turlygazhy.entity;

import com.turlygazhy.Bot;
import com.turlygazhy.dao.GoalDao;
import com.turlygazhy.dao.impl.GroupDao;
import com.turlygazhy.dao.impl.MessageDao;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yerassyl_Turlygazhy on 10-Mar-17.
 */
public class WeekResult {
    private List<Member> fall = new ArrayList<>();
    private List<Member> closeToFall = new ArrayList<>();


    public void analyze(Member member, List<UserResult> results, GoalDao goalDao, int readingResult) throws SQLException {
        int fallCount = readingResult;
        for (UserResult result : results) {
            Goal goal = goalDao.select(result.getGoalId());
            if (!(result.getCompleted() >= goal.getAim())) {
                fallCount++;
            }
            if (fallCount >= 3) {
                fall.add(member);
                return;
            }
        }
        if (fallCount > 0) {
            closeToFall.add(member);
        }
    }

    public int analyze(UserReadingResult reading) {
        if (reading.getCompleted() >= reading.getAim()) {
            return 0;
        }
        return 1;
    }

    public void send(Bot bot, MessageDao messageDao, GroupDao groupDao) throws TelegramApiException, SQLException {
        if (fall.size() == 0) {
            for (Group group : groupDao.selectAll()) {
                bot.sendMessage(new SendMessage()
                        .setChatId(group.getChatId())
                        .setText(messageDao.getMessageText(55))
                );
            }
        } else {
            for (Member member : fall) {
                bot.sendMessage(new SendMessage()
                        .setChatId(groupDao.select(member.getGroupId()).getChatId())
                        .setText(member.getFirstName() + " " + messageDao.getMessageText(56))
                );
            }
        }
        if (closeToFall.size() > 0) {
            for (Member member : closeToFall) {
                bot.sendMessage(new SendMessage()
                        .setChatId(groupDao.select(member.getGroupId()).getChatId())
                        .setText(member.getFirstName() + " " + messageDao.getMessageText(57))
                );
            }
        }
    }
}
