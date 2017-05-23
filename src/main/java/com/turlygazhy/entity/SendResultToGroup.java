package com.turlygazhy.entity;

import com.turlygazhy.Bot;
import com.turlygazhy.dao.DaoFactory;
import com.turlygazhy.dao.impl.MessageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yerassyl_Turlygazhy on 02-Mar-17.
 */
public class SendResultToGroup {
    private static final Logger logger = LoggerFactory.getLogger(SendResultToGroup.class);

    private Map<Long, String> resultMap = new HashMap<>();
    private DaoFactory factory = new DaoFactory();
    private MessageDao messageDao = factory.getMessageDao();

    public void addResult(Long groupChatId, String resultText, Bot bot) throws TelegramApiException {
        logger.info("Adding new result:'" + resultText + "'");
        bot.sendMessage(new SendMessage()
                .setChatId(groupChatId)
                .setText(resultText)
                .setParseMode("HTML")
        );
    }

//    public void send(Bot bot) throws TelegramApiException, SQLException {
//        logger.info("Start send");
//        for (Map.Entry<Long, String> entry : resultMap.entrySet()) {
//            Long chatId = entry.getKey();
//            String text = entry.getValue();
//            bot.sendMessage(new SendMessage()
//                    .setChatId(293188753L)
//                    .setText(messageDao.getMessageText(41) + "\n" + text)
//                    .setParseMode("HTML")
//            );
//        }
//    }
}
