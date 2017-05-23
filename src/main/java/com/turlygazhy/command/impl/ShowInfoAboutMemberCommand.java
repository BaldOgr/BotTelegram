package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.Member;
import com.turlygazhy.entity.Message;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by user on 1/22/17.
 */
public class ShowInfoAboutMemberCommand extends Command {
    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Member member = memberDao.selectByUserId(update.getMessage().getFrom().getId());
        Message message = messageDao.getMessage(messageId);
        String text = message.getSendMessage().getText();
        text = text.replaceAll("firstName", member.getFirstName());
        text = text.replaceAll("phoneNumber", member.getPhoneNumber());
        text = text.replaceAll("nisha", member.getNisha());
        text = text.replaceAll("naviki", member.getNaviki());
        bot.sendMessage(new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(text)
                .setReplyMarkup(keyboardMarkUpDao.select(message.getKeyboardMarkUpId()))
        );
        return true;
    }
}
