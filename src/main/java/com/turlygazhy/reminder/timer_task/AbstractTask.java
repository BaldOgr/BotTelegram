package com.turlygazhy.reminder.timer_task;

import com.turlygazhy.Bot;
import com.turlygazhy.dao.DaoFactory;
import com.turlygazhy.dao.GoalDao;
import com.turlygazhy.dao.impl.*;
import com.turlygazhy.reminder.Reminder;

import java.util.TimerTask;

/**
 * Created by Yerassyl_Turlygazhy on 02-Mar-17.
 */
public abstract class AbstractTask extends TimerTask {
    protected Bot bot;
    protected Reminder reminder;

    protected DaoFactory factory = new DaoFactory();
    protected MemberDao memberDao = factory.getMemberDao();
    protected GroupDao groupDao = factory.getGroupDao();
    protected GoalDao goalDao = factory.getGoalDao();
    protected ButtonDao buttonDao = factory.getButtonDao();
    protected SavedResultsDao savedResultsDao = factory.getSavedResultsDao();
    protected MessageDao messageDao = factory.getMessageDao();

    public AbstractTask(Bot bot, Reminder reminder) {
        this.bot = bot;
        this.reminder = reminder;
    }
}
