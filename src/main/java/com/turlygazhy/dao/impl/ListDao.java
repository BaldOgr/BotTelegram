package com.turlygazhy.dao.impl;

import com.turlygazhy.entity.Message;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/26/16.
 */
public class ListDao {
    private final Connection connection;
    private final String listName;

    public ListDao(Connection connection, String listName) {
        this.connection = connection;
        this.listName = listName;
    }

    public void insert(String photo, String text) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO PUBLIC." + listName + "(ID, PHOTO, TEXT) VALUES (DEFAULT, ?,? )");
        ps.setString(1, photo);
        ps.setString(2, text);
        ps.execute();
    }

    public List<Message> readAll() throws SQLException {
        List<Message> messages = new ArrayList<>();
        Message message;

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM PUBLIC." + listName);
        ps.execute();
        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()) {
            message = new Message();
            message.setId(resultSet.getLong(1));
            message.setSendMessage(new SendMessage().setText(resultSet.getString(3)));
            message.setSendPhoto(new SendPhoto().setPhoto(resultSet.getString(2)));
            messages.add(message);
        }
        return messages;
    }

    public boolean delete(long id) {
        try {
            read(id);
            PreparedStatement ps = connection.prepareStatement("DELETE FROM PUBLIC." + listName + " WHERE ID=?");
            ps.setLong(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private Message read(long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM PUBLIC." + listName + " WHERE id=?");
        ps.setLong(1, id);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();

        Message message = new Message();
        message.setId(rs.getLong(1));
        message.setSendMessage(new SendMessage().setText(rs.getString(3)));
        message.setSendPhoto(new SendPhoto().setPhoto(rs.getString(2)));
        return message;
    }
}
