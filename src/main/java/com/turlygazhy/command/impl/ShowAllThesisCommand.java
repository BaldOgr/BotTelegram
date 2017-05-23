package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.entity.Button;
import com.turlygazhy.entity.Thesis;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yerassyl_Turlygazhy on 01-Mar-17.
 */
public class ShowAllThesisCommand extends Command {
    public static final String NEXT_PAGE = "nextPage";
    public static final String PREV_PAGE = "prevPage";
    public static final String THESIS_PREFIX = "/thesis";
    private WaitingType waitingType;
    private List<String> textPages = new ArrayList<>();
    private int shownPageNumber = 0;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        String updateMessageText;
        if (updateMessage == null) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            updateMessage = callbackQuery.getMessage();
            updateMessageText = callbackQuery.getData();
        } else {
            updateMessageText = updateMessage.getText();
        }
        Long chatId = updateMessage.getChatId();
        if (waitingType == null) {
            List<Thesis> thesisList = thesisDao.selectAll();
            if (thesisList.size() == 0) {
                sendMessage(39, chatId, bot);
                return true;
            }
            String text = messageDao.getMessageText(40);
            initTextPages(text, thesisList);
            showPage(bot, chatId, shownPageNumber, null);
            return false;
        }
        switch (waitingType) {
            case CHANGE_PAGE:
                if (updateMessageText.equals(NEXT_PAGE)) {
                    shownPageNumber++;
                    showPage(bot, chatId, shownPageNumber, updateMessage.getMessageId());
                    return false;
                }
                if (updateMessageText.equals(PREV_PAGE)) {
                    shownPageNumber--;
                    showPage(bot, chatId, shownPageNumber, updateMessage.getMessageId());
                    return false;
                }
                if (updateMessageText.startsWith(THESIS_PREFIX)) {
                    int thesisId = Integer.parseInt(updateMessageText.replace(THESIS_PREFIX, ""));
                    showThesis(bot, chatId, thesisId);
                    return false;
                }
                sendMessage(7, chatId, bot);
                return true;
        }

        return false;
    }

    private void showThesis(Bot bot, Long chatId, int thesisId) throws SQLException, TelegramApiException {
        Thesis thesis = thesisDao.select(thesisId);
        bot.sendMessage(new SendMessage()
                .setChatId(chatId)
                .setText(thesis.getBookName() + " (" + thesis.getUserName() + ")\n\n" + thesis.getThesis().trim())
        );
    }

    private void showPage(Bot bot, Long chatId, int pageNumber, Integer messageId) throws TelegramApiException {
        if (messageId == null) {
            bot.sendMessage(new SendMessage()
                    .setChatId(chatId)
                    .setText(textPages.get(pageNumber))
                    .setReplyMarkup(getKeyboard(pageNumber))
            );
        } else {
            bot.editMessageText(new EditMessageText()
                    .setMessageId(messageId)
                    .setChatId(chatId)
                    .setText(textPages.get(pageNumber))
                    .setReplyMarkup((InlineKeyboardMarkup) getKeyboard(pageNumber))
            );
        }
    }


    private void initTextPages(String text, List<Thesis> thesisList) {
        for (int i = 0; i < thesisList.size(); i++) {
            Thesis thesis = thesisList.get(i);
            text = text + "\n" + THESIS_PREFIX + thesis.getId() + " - " + thesis.getBookName() + " (" + thesis.getUserName() + ")";
            if ((i + 1) % 5 == 0 || (i == (thesisList.size() - 1))) {
                textPages.add(text.trim());
                text = "";
            }
        }
        waitingType = WaitingType.CHANGE_PAGE;
    }

    private ReplyKeyboard getKeyboard(int pageNumber) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton nextPageButton = new InlineKeyboardButton();
        nextPageButton.setText(">>");
        nextPageButton.setCallbackData(NEXT_PAGE);

        InlineKeyboardButton prevPageButton = new InlineKeyboardButton();
        prevPageButton.setText("<<");
        prevPageButton.setCallbackData(PREV_PAGE);

        if (pageNumber != 0) {
            row.add(prevPageButton);
        }

        if (pageNumber != (textPages.size() - 1)) {
            row.add(nextPageButton);
        }

        rows.add(row);
        keyboard.setKeyboard(rows);
        return keyboard;
    }
}
