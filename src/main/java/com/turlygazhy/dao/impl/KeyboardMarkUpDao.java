package com.turlygazhy.dao.impl;

import com.turlygazhy.dao.AbstractDao;
import com.turlygazhy.entity.Button;
import org.h2.jdbc.JdbcSQLException;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/22/16.
 */
public class KeyboardMarkUpDao extends AbstractDao {
    private static final String SELECT_KEYBOARD_BY_ID = "SELECT * FROM PUBLIC.KEYBOARD WHERE ID=?";
    private static final String SELECT_ROW_BY_ID = "SELECT * FROM PUBLIC.ROW WHERE ID=?";
    private static final String SELECT_BUTTON_BY_ID = "SELECT * FROM PUBLIC.BUTTON WHERE ID=?";
    private static final int PARAMETER_ID = 1;
    private static final int ROW_IDS_COLUMN_INDEX = 2;
    private static final int BUTTON_IDS_COLUMN_INDEX = 2;
    private static final int TEXT_COLUMN_INDEX = 2;
    public static final int INLINE_COLUMN_INDEX = 3;
    private final Connection connection;
    ButtonDao buttonDao = factory.getButtonDao();

    public KeyboardMarkUpDao(Connection connection) {
        this.connection = connection;
    }

    public ReplyKeyboard select(long keyboardMarkUpId) throws SQLException {
        if (keyboardMarkUpId == 0) {
            return null;
        }
        PreparedStatement ps = connection.prepareStatement(SELECT_KEYBOARD_BY_ID);
        ps.setLong(PARAMETER_ID, keyboardMarkUpId);
        ps.execute();
        ResultSet resultSet = ps.getResultSet();
        resultSet.next();

        return getKeyboard(resultSet);
    }

    private ReplyKeyboard getKeyboard(ResultSet rs) throws SQLException {

        String buttonIds = rs.getString(2);

        if (buttonIds == null) {
            return null;
        }

        boolean inline = rs.getBoolean(3);
        String[] rows = buttonIds.split(";");
        if (inline) {
            return getInlineKeyboard(rows);
        } else {
            return getReplyKeyboard(rows);
        }
    }

    private ReplyKeyboard getReplyKeyboard(String[] rows) throws SQLException {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rowsList = new ArrayList<>();

        for (String buttonIdsString : rows) {
            KeyboardRow keyboardRow = new KeyboardRow();
            String[] buttonIds = buttonIdsString.split(",");
            for (String buttonId : buttonIds) {
                Button buttonFromDb = buttonDao.getButton(Integer.parseInt(buttonId));
                KeyboardButton button = new KeyboardButton();
                String buttonText = buttonFromDb.getText();
                button.setText(buttonText);
                button.setRequestContact(buttonFromDb.isRequestContact());
                keyboardRow.add(button);
            }
            rowsList.add(keyboardRow);
        }

        replyKeyboardMarkup.setKeyboard(rowsList);
        return replyKeyboardMarkup;
    }

    private InlineKeyboardMarkup getInlineKeyboard(String[] rowIds) throws SQLException {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (String buttonIdsString : rowIds) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            String[] buttonIds = buttonIdsString.split(",");
            for (String buttonId : buttonIds) {
                Button buttonFromDb = buttonDao.getButton(Integer.parseInt(buttonId));
                InlineKeyboardButton button = new InlineKeyboardButton();
                String buttonText = buttonFromDb.getText();
                button.setText(buttonText);
                String url = buttonFromDb.getUrl();
                if (url != null) {
                    button.setUrl(url);
                } else {
                    button.setCallbackData(buttonText);
                }
                row.add(button);
            }
            rows.add(row);
        }

        keyboard.setKeyboard(rows);
        return keyboard;
    }
}
