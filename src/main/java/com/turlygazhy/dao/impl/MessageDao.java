package com.turlygazhy.dao.impl;

import com.turlygazhy.dao.AbstractDao;
import com.turlygazhy.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by user on 12/11/16.
 */
public class MessageDao extends AbstractDao {
    private static final Logger logger = LoggerFactory.getLogger(MessageDao.class);

    public static final String INSERT_MESSAGE = "INSERT INTO MESSAGE(ID,TEXT) VALUES (DEFAULT, ?)";
    private static final String SELECT_FROM_MESSAGE_BY_ID = "SELECT * FROM MESSAGE WHERE ID = ?";
    private static final int MESSAGE_TEXT_COLUMN_INDEX = 2;
    private static final int KEYBOARD_ID_COLUMN_INDEX = 4;
    private static final String UPDATE_PHOTO = "UPDATE PUBLIC.MESSAGE SET PHOTO = ? WHERE ID = ?";
    private static final String UPDATE_TEXT = "UPDATE PUBLIC.MESSAGE SET TEXT = ? WHERE ID = ?";


    private Connection connection;

    public MessageDao(Connection connection) {
        this.connection = connection;
    }

//    public Message insert(Message message) throws SQLException {
//        PreparedStatement ps = connection.prepareStatement(INSERT_MESSAGE);
//        String text = message.getSendMessage().getText();
//        ps.setString(PARAMETER_TEXT, text);
//        ps.execute();
//        ResultSet generatedKeys = ps.getGeneratedKeys();
//        generatedKeys.next();
//        long messageId = generatedKeys.getLong(ID_INDEX);
//        message.setId(messageId);
//        return message;
//    }

    public Message getMessage(long messageId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_FROM_MESSAGE_BY_ID);
        ps.setLong(ID_INDEX, messageId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();
        Message message = new Message();
        message.setId(messageId);
        message.setSendMessage(new SendMessage()
                .setText(rs.getString(MESSAGE_TEXT_COLUMN_INDEX))
        );
        message.setKeyboardMarkUpId(rs.getLong(KEYBOARD_ID_COLUMN_INDEX));
        String photo = rs.getString(3);
        if (photo != null) {
            message.setSendPhoto(new SendPhoto().setPhoto(photo));
        }
        return message;
    }

    public void updatePhoto(String photo, long messageId) {
        try {
            PreparedStatement ps = connection.prepareStatement(UPDATE_PHOTO);
            ps.setString(1, photo);
            ps.setLong(2, messageId);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateText(String text, long messageId) {
        try {
            PreparedStatement ps = connection.prepareStatement(UPDATE_TEXT);
            ps.setString(1, text);
            ps.setLong(2, messageId);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(long messageId, String photo, String text) {
        updatePhoto(photo, messageId);
        updateText(text, messageId);
    }

    public String getMessageText(long id) throws SQLException {
        return getMessage(id).getSendMessage().getText();
    }
}
