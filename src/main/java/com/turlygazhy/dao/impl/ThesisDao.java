package com.turlygazhy.dao.impl;

import com.turlygazhy.entity.Thesis;
import com.turlygazhy.entity.UserReadingResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yerassyl_Turlygazhy on 3/1/2017.
 */
public class ThesisDao {
    private final Connection connection;

    public ThesisDao(Connection connection) {
        this.connection = connection;
    }

    public void insert(UserReadingResult readingResult, String userName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO Thesis VALUES(default, ?, ?, ?)");
        ps.setString(1, readingResult.getBookName());
        ps.setString(2, userName);
        ps.setString(3, readingResult.getThesis());
        ps.execute();
    }

    public List<Thesis> selectAll() throws SQLException {
        List<Thesis> thesisList = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("select * from thesis");
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            Thesis thesis = new Thesis();
            thesis.setId(rs.getInt(1));
            thesis.setBookName(rs.getString(2));
            thesis.setUserName(rs.getString(3));
            thesis.setThesis(rs.getString(4));
            thesisList.add(thesis);
        }
        return thesisList;
    }

    public Thesis select(int thesisId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("select * from thesis where id=?");
        ps.setInt(1, thesisId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();

        Thesis thesis = new Thesis();
        thesis.setId(rs.getInt(1));
        thesis.setBookName(rs.getString(2));
        thesis.setUserName(rs.getString(3));
        thesis.setThesis(rs.getString(4));

        return thesis;
    }
}
