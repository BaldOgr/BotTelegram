package com.turlygazhy.reminder;

import com.turlygazhy.Bot;
import com.turlygazhy.reminder.timer_task.*;
import com.turlygazhy.tool.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;

/**
 * Created by Yerassyl_Turlygazhy on 02-Mar-17.
 */
public class Reminder {
    private static final Logger logger = LoggerFactory.getLogger(Reminder.class);

    private Bot bot;
    private Timer timer = new Timer(true);

    public Reminder(Bot bot) {
        this.bot = bot;
        setNextNightTask();
        setPushUpTask(8);
        setPushUpTask(11);
        setEndDayTask(20);
        setEndDayTask(21);
        setEndDayTask(22);
        setEndDayTask(23);
    }

    public void setEndDayTask(int hour) {
        Date date = DateUtil.getHour(hour);
        logger.info("Next end day task set to " + date);

        EndDayTask endDayTask = new EndDayTask(bot, this);
        timer.schedule(endDayTask, date);
    }

    private void setPushUpTask(int hour) {
        Date date = DateUtil.getHour(hour);
        logger.info("Next 8MorningTask set to " + date);

        PushUpTask pushUpTask = new PushUpTask(bot, this);
        timer.schedule(pushUpTask, date);
    }

    public void setNextNightTask() {
        Date date = DateUtil.getNextNight();

        logger.info("new reminder time: " + date);
        EveryNightTask everyNightTask = new EveryNightTask(bot, this);
        timer.schedule(everyNightTask, date);
    }

    public void setNextPushUpTask() {
        setPushUpTask(new Date().getHours());
    }
}
