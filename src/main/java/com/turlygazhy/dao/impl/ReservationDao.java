package com.turlygazhy.dao.impl;

import com.turlygazhy.entity.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/26/17.
 */
public class ReservationDao {
    private final Connection connection;

    public ReservationDao(Connection connection) {
        this.connection = connection;
    }

    public List<String> selectAvailableTime(int day) throws SQLException {
        List<Reservation> reservations = selectAll();
        List<String> result = new ArrayList<>();
        for (Reservation reservation : reservations) {
            String time = reservation.getTime();
            if (time.contains(day + "-") && reservation.getClientChatId() == 0) {
                result.add(time.replaceAll(day + "-", ""));
            }
        }

        return result;
    }

    public List<Reservation> selectAll() throws SQLException {
        List<Reservation> result = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("select * from reservation");
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            Reservation reservation = new Reservation();
            reservation.setId(rs.getInt(1));
            reservation.setTime(rs.getString(2));
            reservation.setClientChatId(rs.getLong(3));
            reservation.setClientInfo(rs.getString(4));
            result.add(reservation);
        }
        return result;
    }
}