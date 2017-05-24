package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.connection_pool.ConnectionPool;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by lol on 24.05.2017.
 */
public class ShowTasksCommand extends Command {
    WaitingType waitingType;
    Connection connection = ConnectionPool.getConnection();
    Long userId;
    ResultSet rs;
    ArrayList<String> undoneTasks;
    ArrayList<Integer> undoneTasksId = new ArrayList<>();
    int taskNumber = 0;
    private final String SELECT_UNDONE_TASKS = "SELECT * FROM TASK WHERE USER_ID = ? AND STATUS = 2";
    private final String SELECT_USER = "SELECT * FROM USER WHERE CHAT_ID = ?";

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        String updateMessageText = updateMessage.getText();
        Long chatId = updateMessage.getChatId();

        if (waitingType == null){

            userId = getUserId(chatId);
            rs = getTasks(userId);

            if (rs == null){
                sendMessage(82, chatId, bot);
                return true;
            }
            undoneTasks = getUndoneTasks();
            sendMessage(81, chatId, bot);
            sendMessage(undoneTasks.get(taskNumber), chatId, bot);

            waitingType = WaitingType.COMMAND;

            return false;
        }

        if(undoneTasks.size() == taskNumber){
            sendMessage(82, chatId, bot);
            return true;
        }

        switch (waitingType){
            case COMMAND:
                PreparedStatement ps;

                if (updateMessageText.equals(buttonDao.getButtonText(51))) {
                    ps = connection.prepareStatement("UPDATE TASK SET STATUS = ? WHERE ID = ?");
                    ps.setInt(1, 0);
                    ps.setLong(2, undoneTasksId.get(taskNumber));
                    ps.execute();
                    undoneTasks.remove(taskNumber);
                    taskNumber--;
                    if (!sendTask(chatId, bot)){
                        sendMessage(82, chatId, bot);
                    }
                } else
                if (updateMessageText.equals(buttonDao.getButtonText(52))) {
                    ps = connection.prepareStatement("UPDATE TASK SET STATUS = ? WHERE ID = ?");
                    ps.setInt(1, 3);
                    ps.setLong(2, undoneTasksId.get(taskNumber));
                    ps.execute();
                    undoneTasks.remove(taskNumber);
                    taskNumber--;
                    if (!sendTask(chatId, bot)){
                        sendMessage(82, chatId, bot);
                    }


                } else
                if (updateMessageText.equals(buttonDao.getButtonText(53))) {
                    ps = connection.prepareStatement("SELECT * FROM USER");
                    ps.execute();
                    ResultSet rs = ps.getResultSet();
                    rs.next();
                    StringBuilder sb = new StringBuilder();
                    while (!rs.isAfterLast()){
                        sb.append("/id");
                        sb.append(rs.getInt("ID"));
                        sb.append(" ").append(rs.getString("NAME")).append("\n");
                        rs.next();
                    }
                    sendMessage(sb.toString(), chatId, bot);
                    waitingType = WaitingType.TASK_WORKER;
                } else

                if (updateMessageText.equals(buttonDao.getButtonText(54))) {
                    if (++taskNumber == undoneTasks.size()){
                        sendMessage(82, chatId, bot);
                        taskNumber--;
                    }
                    sendMessage(undoneTasks.get(taskNumber), chatId, bot);
                } else

                if (updateMessageText.equals(buttonDao.getButtonText(55))) {
                    if (--taskNumber < 0){
                        sendMessage(82, chatId, bot);
                        taskNumber++;
                    }
                    sendMessage(undoneTasks.get(taskNumber), chatId, bot);
                }

                return false;

            case TASK_WORKER:
                Long taskWorker = Long.valueOf(updateMessageText.substring(3));
                ps = connection.prepareStatement("UPDATE TASK SET USER_ID = ? WHERE ID = ?");
                ps.setLong(1, taskWorker);
                ps.setInt(2, undoneTasksId.get(taskNumber));
                ps.execute();
                sendMessage(79, chatId, bot);

                ps = connection.prepareStatement("SELECT * FROM USER WHERE ID = ?");
                ps.setLong(1, taskWorker);
                ps.execute();

                ResultSet rs = ps.getResultSet();
                rs.next();
                sendMessage(80, rs.getLong("CHAT_ID"), bot);

                taskNumber++;
                if (undoneTasks.size() == taskNumber){
                    sendMessage(82, chatId, bot);
                    return true;
                }
                sendMessage(undoneTasks.get(taskNumber), chatId, bot);
                waitingType = WaitingType.COMMAND;
                return false;
        }

        return false;
    }

    private ResultSet getTasks(Long userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_UNDONE_TASKS);
        ps.setLong(1, userId);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        if(rs.next()){
            return rs;
        }
        return null;
    }

    private Long getUserId(Long chatId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_USER);
        ps.setLong(1, chatId);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        rs.next();
        return rs.getLong("ID");
    }

    private ArrayList<String> getUndoneTasks() throws SQLException {
        ArrayList<String> undoneTasks = new ArrayList<>();

        while (!rs.isAfterLast()){
            StringBuilder sb = new StringBuilder();
            sb.append("Task: ").append(rs.getString("TEXT")).append("\n");
            sb.append("Deadline: ").append(rs.getString("DEADLINE")).append("\n");
            switch (rs.getInt("STATUS")){
                case 0:
                    sb.append("Undone");
                    break;
                case 2:
                    sb.append("Waiting for confirmation");
                    break;
            }
            undoneTasks.add(sb.toString());
            undoneTasksId.add(rs.getInt("ID"));
            rs.next();

        }
        return undoneTasks;
    }

    private boolean sendTask(Long chatId, Bot bot) throws SQLException, TelegramApiException {
        if (taskNumber < undoneTasks.size() && taskNumber > 0) {
            sendMessage(undoneTasks.get(taskNumber), chatId, bot);
            return true;
        }
        return false;
    }
}
