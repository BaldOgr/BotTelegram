package com.turlygazhy.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by user on 1/5/17.
 */
public class ListKeyboardDao {
    private final Connection connection;

    public ListKeyboardDao(Connection connection) {
        this.connection = connection;
    }

    public long selectKeyboardId(String listName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM PUBLIC.LIST_KEYBOARD WHERE LIST_NAME=?");
        ps.setString(1, listName);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();
        return rs.getLong(3);
    }
}
