package com.turlygazhy.dao;

import com.turlygazhy.entity.Goal;
import com.turlygazhy.entity.UserReadingResult;
import com.turlygazhy.entity.UserResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by user on 2/24/17.
 */
public class GoalDao {
    private final Connection connection;

    public GoalDao(Connection connection) {
        this.connection = connection;
    }

    public void insert(String goalName, int goal, String startTime, String endTime) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO goal VALUES(default, ?, ?, ?, ?, ?)");
        ps.setString(1, goalName);
        ps.setInt(2, goal);
        if (startTime != null && endTime != null) {
            ps.setBoolean(3, true);
        } else {
            ps.setBoolean(3, false);
        }
        ps.setString(4, startTime);
        ps.setString(5, endTime);
        ps.execute();
    }

    public List<UserResult> getForUser(Integer userId) throws SQLException {
        List<UserResult> results = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("select * from USER_RESULT where user_id=?");
        ps.setInt(1, userId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            UserResult userResult = new UserResult();
            userResult.setId(rs.getInt(1));
            userResult.setGoalId(rs.getInt(2));
            userResult.setCompleted(rs.getInt(3));
            results.add(userResult);
        }

        List<Goal> goals = selectAllGoals();

        List<Integer> goalIds = new ArrayList<>();

        for (Goal goal : goals) {
            goalIds.add(goal.getId());
        }

        List<UserResult> additional = new ArrayList<>();

        for (UserResult result : results) {
            Integer goalId = result.getGoalId();
            goalIds.remove(goalId);
        }

        for (Integer goalId : goalIds) {
            additional.add(addGoalForUser(goalId, userId));
        }

        for (UserResult added : additional) {
            results.add(added);
        }

        Collections.sort(results, new Comparator<UserResult>() {
            @Override
            public int compare(UserResult o1, UserResult o2) {
                if (o1.getGoalId() == 2) {
                    return -100;
                }
                if (o2.getGoalId() == 2){
                    return 100;
                }
                return o1.getGoalId() - o2.getGoalId();
            }
        });

        return results;
    }

    private UserResult addGoalForUser(int goalId, Integer userId) throws SQLException {
        UserResult result = new UserResult();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO USER_RESULT VALUES(default, ?, 0, ?)");
        ps.setInt(1, goalId);
        ps.setInt(2, userId);
        ps.execute();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        result.setId(rs.getInt(1));
        result.setCompleted(0);
        result.setGoalId(goalId);
        return result;
    }

    public List<Goal> selectAllGoals() throws SQLException {
        List<Goal> goals = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM GOAL");
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            Goal goal = new Goal();
            goal.setId(rs.getInt(1));
            goal.setName(rs.getString(2));
            goal.setAim(rs.getInt(3));
            goals.add(goal);
        }
        return goals;
    }

    public Goal select(int goalId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("select * from GOAL where id=?");
        ps.setInt(1, goalId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();
        Goal goal = new Goal();
        goal.setId(goalId);
        goal.setName(rs.getString(2));
        goal.setAim(rs.getInt(3));
        goal.setTimeLimit(rs.getBoolean(4));
        goal.setStartTime(rs.getString(5));
        goal.setEndTime(rs.getString(6));
        return goal;
    }

    public void inputResult(int userResultId, int completed) throws SQLException {
        int completedDB = selectCompleted(userResultId);
        PreparedStatement ps = connection.prepareStatement("update USER_RESULT set COMPLETED=? where id=?");
        ps.setInt(1, completed + completedDB);
        ps.setInt(2, userResultId);
        ps.execute();
    }

    private int selectCompleted(int userResultId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM USER_RESULT where id=?");
        ps.setInt(1, userResultId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();
        return rs.getInt(3);
    }

    public String getGoalName(int goalId) throws SQLException {
        Goal goal = select(goalId);
        return goal.getName();
    }

    public UserReadingResult getReadingResultForUser(Integer userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("select * from USER_RESULT_READING where user_id=?");
        ps.setInt(1, userId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        boolean exist = rs.next();

        if (exist) {
            UserReadingResult reading = new UserReadingResult();
            reading.setId(rs.getInt(1));
            reading.setCompleted(rs.getInt(2));
            reading.setAim(rs.getInt(3));
            reading.setBookName(rs.getString(4));
            reading.setSummary(rs.getString(5));
            return reading;
        } else {
            return insertReading(userId);
        }
    }

    private UserReadingResult insertReading(Integer userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO USER_RESULT_READING VALUES(default, 0, 100, null, null, ?)");
        ps.setInt(1, userId);
        ps.execute();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();

        UserReadingResult reading = new UserReadingResult();
        reading.setId(rs.getInt(1));
        reading.setCompleted(0);
        reading.setAim(100);
        return reading;
    }

    public void insert(String goalName, int goal) throws SQLException {
        insert(goalName, goal, null, null);
    }

    public void setBookName(Integer userId, String bookName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("update USER_RESULT_READING set book_name=? where user_id=?");
        ps.setString(1, bookName);
        ps.setInt(2, userId);
        ps.execute();
    }

    public UserReadingResult inputReadingResult(Integer userId, int completed) throws SQLException {
        int completedDB = selectReadingCompleted(userId);
        PreparedStatement ps = connection.prepareStatement("update USER_RESULT_READING set COMPLETED=? where user_id=?");
        ps.setInt(1, completed + completedDB);
        ps.setInt(2, userId);
        ps.execute();
        return getReadingResultForUser(userId);
    }

    private int selectReadingCompleted(Integer userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM USER_RESULT_READING where user_id=?");
        ps.setInt(1, userId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();
        return rs.getInt(2);
    }

    public void resetReading(Integer userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("update USER_RESULT_READING set book_name=?, SUMMARY=? where user_id=?");
        ps.setString(1, "");
        ps.setString(2, "");
        ps.setInt(3, userId);
        ps.execute();
    }

    /**
     * reset also read count
     */
    public void resetReadingCompleted(Integer userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("update USER_RESULT_READING set book_name=?, COMPLETED=?, SUMMARY=?  where user_id=?");
        ps.setString(1, "");
        ps.setInt(2, 0);
        ps.setString(3, "");
        ps.setInt(4, userId);
        ps.execute();
    }

    public void addThesis(String thesis, Integer userId) throws SQLException {
        UserReadingResult readingResult = getReadingResultForUser(userId);
        String thesisDB = readingResult.getThesis();
        PreparedStatement ps = connection.prepareStatement("update USER_RESULT_READING set SUMMARY=? where user_id=?");
        ps.setString(1, thesisDB + "\n\n" + thesis);
        ps.setInt(2, userId);
        ps.execute();
    }

    public Goal select(String goalName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("select * from GOAL where name=?");
        ps.setString(1, goalName);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        rs.next();
        return select(rs.getInt(1));
    }

    public void update(Goal goal) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("update GOAL set NAME=?, AIM=?, WITH_TIME_LIMIT=?, START_TIME=?, END_TIME=? where id=?");
        ps.setString(1, goal.getName());
        ps.setInt(2, goal.getAim());
        ps.setBoolean(3, goal.isTimeLimit());
        ps.setString(4, goal.getStartTime());
        ps.setString(5, goal.getEndTime());
        ps.setInt(6, goal.getId());
        ps.execute();
    }

    public void changeBookAim(int newAim) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("update USER_RESULT_READING set aim=?");
        ps.setInt(1, newAim);
        ps.execute();
    }

    public void resetResults(Integer userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("update USER_RESULT set completed=? where user_id=?");
        ps.setInt(1, 0);
        ps.setInt(2, userId);
        ps.execute();
    }
}
