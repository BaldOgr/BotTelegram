package com.turlygazhy.dao.impl;

import com.turlygazhy.dao.AbstractDao;
import com.turlygazhy.entity.Button;
import com.turlygazhy.exception.CommandNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by user on 1/2/17.
 * ';' - separator for rows
 * ',' - separator for buttons
 */
public class ButtonDao extends AbstractDao {
    public static final int COMMAND_ID_COLUMN_INDEX = 3;
    private final Connection connection;

    public ButtonDao(Connection connection) {
        this.connection = connection;
    }

    public Button getButton(String text) throws CommandNotFoundException, SQLException {
        try {
            String selectButtonByText = "SELECT * FROM BUTTON WHERE TEXT = ?";
            int textParameterIndex = 1;

            PreparedStatement ps;
            ps = connection.prepareStatement(selectButtonByText);
            ps.setString(textParameterIndex, text);

            ps.execute();
            ResultSet rs = ps.getResultSet();
            rs.next();


            Button button = new Button();
            button.setId(rs.getInt(ID_INDEX));
            button.setText(text);
            button.setCommandId(rs.getInt(3));
            button.setUrl(rs.getString(4));
            button.setRequestContact(rs.getBoolean(5));
            return button;
        } catch (SQLException e) {
            if (e.getMessage().contains("No data is available")) {
                throw new CommandNotFoundException(e);
            }
            throw new SQLException(e);
        }
    }

    public String getButtonText(int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT text FROM BUTTON where id=?");
        ps.setInt(1, id);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();
        return rs.getString(1);
    }

    public Button getButton(int id) throws SQLException {
        try {
            return getButton(getButtonText(id));
        } catch (CommandNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateButtonText(int buttonId, String newText) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("update button set text=? where id=?");
        ps.setString(1, newText);
        ps.setInt(2, buttonId);
        ps.execute();
    }
}
