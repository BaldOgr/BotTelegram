package com.turlygazhy.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/22/17.
 */
public class KeyWordDao {
    private final Connection connection;

    public KeyWordDao(Connection connection) {
        this.connection = connection;
    }

    public List<String> selectAll() throws SQLException {
        List<String> result = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM KEY_WORDS");
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            result.add(rs.getString(2));
        }
        return result;
    }

    public void delete(String keyWord) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("delete from key_words where keyword=?");
        ps.setString(1, keyWord);
        ps.execute();
    }

    public void insert(String newKeyword) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO key_words VALUES(default, ?)");
        ps.setString(1, newKeyword);
        ps.execute();
    }
}
