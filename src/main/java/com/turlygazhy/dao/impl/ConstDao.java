package com.turlygazhy.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by user on 1/5/17.
 */
public class ConstDao {
    private final Connection connection;

    public ConstDao(Connection connection) {
        this.connection = connection;
    }

    public String select(long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM PUBLIC.CONST WHERE ID=?");
        ps.setLong(1, id);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();
        return rs.getString(2);
    }

    public void update(int id, String text) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("update const set text = ? where id = ?");
        ps.setString(1, text);
        ps.setInt(2, id);
        ps.execute();
    }
}
