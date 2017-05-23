package com.turlygazhy.dao.impl;

import com.turlygazhy.command.Command;
import com.turlygazhy.command.CommandFactory;
import com.turlygazhy.dao.AbstractDao;
import com.turlygazhy.exception.CommandNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by user on 12/14/16.
 */
public class CommandDao extends AbstractDao {
    private Connection connection;

    public CommandDao(Connection connection) {
        this.connection = connection;
    }

    public Command getCommand(long commandId) throws CommandNotFoundException, SQLException {
        try {
            String selectCommandById = "SELECT * FROM PUBLIC.COMMAND WHERE ID = ?";
            int idParameterIndex = 1;
            int commandTypeIdColumnIndex = 2;
            int messageToUserColumnIndex = 3;
            int messageIdForChangingColumnIndex = 4;
            int listNameColumnIndex = 5;

            PreparedStatement ps = connection.prepareStatement(selectCommandById);
            ps.setLong(idParameterIndex, commandId);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            rs.next();

            Command command = CommandFactory.getCommand(rs.getLong(commandTypeIdColumnIndex));
            command.setId(rs.getLong(ID_INDEX));
            command.setMessageId(rs.getLong(messageToUserColumnIndex));
//            command.setMessageIdForChanging(rs.getLong(messageIdForChangingColumnIndex));
            return command;
        } catch (SQLException e) {
            if (e.getMessage().contains("No data is available")) {
                throw new CommandNotFoundException(e);
            }
            throw new SQLException(e);
        }
    }

//    private Command setAdditionalInfo(Command command) throws SQLException {
//        if (command instanceof AddToListCommand) {
//            PreparedStatement ps = connection.prepareStatement("select * from ADD_TO_LIST_COMMAND_TYPE_REQUIRED_DATA where id=?");
//            ps.setInt(1, command.getCommandInfoId());
//            ps.execute();
//            ResultSet rs = ps.getResultSet();
//            rs.next();
//            AddToListCommand result = (AddToListCommand) command;
//            result.setPhotoNeeded(rs.getBoolean(3));
//            result.setAskPhotoNeeded(rs.getBoolean(4));
//            result.setContactNeeded(rs.getBoolean(5));
//            return result;
//        }
//        return command;
//    }
}
