package com.turlygazhy.dao.impl;

import com.turlygazhy.entity.Member;
import org.telegram.telegrambots.api.objects.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 1/21/17.
 */
public class MemberDao {
    private final Connection connection;

    public MemberDao(Connection connection) {
        this.connection = connection;
    }

    public void insert(String nisha, String naviki, Contact contact, long chatId, String userName) throws SQLException {
        boolean userRegistered = isUserRegistered(contact.getUserID());
        if (userRegistered) {
            PreparedStatement updatePS = connection.prepareStatement("UPDATE member SET first_NAME=?, last_name=?, phone_number=?, naviki=?, nisha=?, user_name=? WHERE user_ID=?");
            updatePS.setString(1, contact.getFirstName());
            updatePS.setString(2, contact.getLastName());
            updatePS.setString(3, contact.getPhoneNumber());
            updatePS.setString(4, naviki);
            updatePS.setString(5, nisha);
            updatePS.setString(6, userName);
            updatePS.setLong(7, contact.getUserID());
            updatePS.execute();
            return;
        }
        PreparedStatement ps = connection.prepareStatement("INSERT INTO member(ID, user_id, first_name, last_name, phone_number, chat_id, naviki, nisha, user_name) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setLong(1, contact.getUserID());
        ps.setString(2, contact.getFirstName());
        ps.setString(3, contact.getLastName());
        ps.setString(4, contact.getPhoneNumber());
        ps.setLong(5, chatId);
        ps.setString(6, naviki);
        ps.setString(7, nisha);
        ps.setString(8, userName);
        ps.execute();
    }

    public boolean isUserRegistered(Integer userID) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM MEMBER where user_id=?");
            ps.setInt(1, userID);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Member selectByUserId(Integer userId) throws SQLException {
        Member member = new Member();

        PreparedStatement ps = connection.prepareStatement("select * from member where user_id=?");
        ps.setInt(1, userId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();
        member.setId(rs.getInt(1));
        member.setUserId(userId);
        member.setFirstName(rs.getString(3));
        member.setLastName(rs.getString(4));
        member.setPhoneNumber(rs.getString(5));
        member.setChatId(rs.getLong(6));
        member.setNaviki(rs.getString(7));
        member.setNisha(rs.getString(8));

        return member;
    }

    public void updateNishaByUserId(Integer userId, String nisha) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("update member set nisha = ? where user_id=?");
        ps.setString(1, nisha);
        ps.setInt(2, userId);
        ps.execute();
    }

    public void updateNavikiByUserId(Integer userId, String naviki) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("update member set naviki = ? where user_id=?");
        ps.setString(1, naviki);
        ps.setInt(2, userId);
        ps.execute();
    }

    public List<Member> search(String searchString) throws SQLException {
        Set<Member> result = new HashSet<>();
        List<Member> allMembers = selectAll();
        for (Member member : allMembers) {
            String[] keyWords = searchString.split(" ");
            String nisha = member.getNisha();
            for (String keyWord : keyWords) {
                if (nisha.contains(keyWord)) {
                    result.add(member);
                    break;
                }
            }
        }
        List<Member> memberList = new ArrayList<>();
        for (Member member : result) {
            memberList.add(member);
        }
        return memberList;
    }

    public List<Member> selectAll() throws SQLException {
        List<Member> result = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("select * from member");
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            boolean access = rs.getBoolean(5);
            if (access) {
                Member member = new Member();
                member.setId(rs.getInt(1));
                member.setFirstName(rs.getString(2));
                member.setUserId(rs.getInt(3));
                member.setChatId(rs.getLong(4));
                member.setAccess(true);
                member.setPhoneNumber(rs.getString(6));
                member.setUserName(rs.getString(7));
                member.setGroupId(rs.getInt(8));
                result.add(member);
            }
        }
        return result;
    }

    public String selectByChatId(long chatId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("select * from member where chat_id=?");
        ps.setLong(1, chatId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        return rs.getString(5);
    }

    public void insert(Long chatId, Contact contact, String userName) throws SQLException {
        Integer userID = contact.getUserID();
        boolean userRegistered = isUserRegistered(userID);
        if (userRegistered) {
            return;
        }
        PreparedStatement ps = connection.prepareStatement("INSERT INTO member VALUES(default, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, contact.getFirstName());
        ps.setInt(2, userID);
        ps.setLong(3, chatId);
        ps.setBoolean(4, false);
        ps.setString(5, contact.getPhoneNumber());
        ps.setString(6, userName);
        ps.setInt(7, 0);
        ps.execute();
    }

    public long getChatId(Integer userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("select * from member where user_id=?");
        ps.setInt(1, userId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();
        return rs.getLong(4);
    }

    public void giveAccess(Integer userId, Integer groupId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("update member set has_access=?, group_id=? where user_id=?");
        ps.setBoolean(1, true);
        ps.setInt(2, groupId);
        ps.setInt(3, userId);
        ps.execute();
    }

    public String getName(Integer userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("select * from member where user_id=?");
        ps.setInt(1, userId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();
        return rs.getString(2);
    }
}
