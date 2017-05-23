package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.connection_pool.ConnectionPool;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lol on 22.05.2017.
 */
public class GetUsersInvitesCommand extends Command {
    private WaitingType waitingType;

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {
        Message updateMessage = update.getMessage();
        Long chatId = updateMessage.getChatId();
        Long adminChatId = Long.valueOf(constDao.select(2));
        String updateMessageText = updateMessage.getText();

        if (chatId.equals(adminChatId)){

            if (waitingType == null) {
                PreparedStatement pr = ConnectionPool.getConnection().prepareStatement("SELECT * FROM USERS_TO_GROUP WHERE STATUS = 0");
                pr.execute();
                ResultSet rs = pr.getResultSet();

                rs.next();

                StringBuilder sb = new StringBuilder();
                sb.append(rs.getString("ID")).append(" ");
                sb.append(rs.getString("NAME")).append(" ");
                sb.append(rs.getString("PHONENUMBER")).append(" ");
                sb.append(rs.getString("BIRTHDAY"));

                sendMessage(sb.toString(), chatId, bot);
                sendMessage(74, chatId, bot);
                waitingType = WaitingType.ANSWER_FOR_USER;
                return false;
            }

            switch (waitingType) {
                case ANSWER_FOR_USER:
                    String[] s = updateMessageText.split(" ");
                    int id = Integer.valueOf(s[1]);
                    int invited = s[2].equals("yes") ? 1 : 2;

                    PreparedStatement pr = ConnectionPool.getConnection().prepareStatement("UPDATE USERS_TO_GROUP SET STATUS = ? WHERE ID = ?");
                    pr.setInt(1, invited);
                    pr.setInt(2, id);
                    pr.execute();

                    if (invited == 1){
                        pr = ConnectionPool.getConnection().prepareStatement("SELECT * FROM USERS_TO_GROUP WHERE ID = ?");
                        pr.setInt(1, id);
                        pr.execute();
                        ResultSet rs = pr.getResultSet();
                        rs.next();
                        sendMessage(75, rs.getLong("CHAT_ID"), bot);
                        sendMessage( 24, adminChatId, bot);
                    } else {
                        sendMessage( 25, adminChatId, bot);
                    }


                    return true;
            }

        } else {
            sendMessage(73, chatId, bot);
            return true;
        }

        return false;
    }
}
