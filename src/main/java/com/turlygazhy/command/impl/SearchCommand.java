package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.Member;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by user on 1/25/17.
 */
public class SearchCommand extends Command {
    private String searchString;
    private boolean waitInput;
    private int i = 0;
    private List<Member> members;


    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message message = update.getMessage();
        if (message == null) {
            String data = update.getCallbackQuery().getData();
            if (data.equals(buttonDao.getButtonText(30))) {
                i = i + 2;
                showMembers(bot, update.getCallbackQuery().getMessage().getChatId());
                return false;
            }
        }
        String text = message.getText();
        Long chatId = message.getChatId();


        if (waitInput) {
            searchString = text;
            members = memberDao.search(searchString);
            if (members.isEmpty()) {
                sendMessage(38, chatId, bot);
                return true;
            }
            showMembers(bot, chatId);
            return false;
        }


        if (searchString == null) {
            sendMessage(36, chatId, bot);
            waitInput = true;
            return false;
        }

        return false;
    }

    private void showMembers(Bot bot, Long chatId) throws SQLException, TelegramApiException {
        int membersSize = members.size();
        String template = messageDao.getMessage(37).getSendMessage().getText();


        Member first = members.get(i);
        String firstData = template.replace("userName", first.getFirstName()).replace("nisha", first.getNisha()).replace("naviki", first.getNaviki()).replace("phoneNumber", first.getPhoneNumber());
        if (first.getUserName() != null) {
            firstData = firstData + "\n Telegram: @" + first.getUserName();
        }

        int secondIndex = i + 1;
        if (membersSize > secondIndex) {
            Member second = members.get(secondIndex);
            String secondData = template.replace("userName", second.getFirstName()).replace("nisha", second.getNisha()).replace("naviki", second.getNaviki()).replace("phoneNumber", second.getPhoneNumber());
            if (second.getUserName() != null) {
                secondData = secondData + "\n Telegram: @" + second.getUserName();
            }

            SendMessage sendMessage = new SendMessage()
                    .setChatId(chatId)
                    .setText(firstData + "\n\n" + secondData);
            if (membersSize > secondIndex + 1) {
                sendMessage = sendMessage.setReplyMarkup(keyboardMarkUpDao.select(13));
            }
            bot.sendMessage(sendMessage);
        } else {
            bot.sendMessage(new SendMessage()
                    .setChatId(chatId)
                    .setText(firstData)
            );
        }
    }
}
