package com.turlygazhy.command;

import com.turlygazhy.command.impl.*;
import com.turlygazhy.exception.NotRealizedMethodException;

/**
 * Created by user on 1/2/17.
 */
public class CommandFactory {
    public static Command getCommand(long id) {
        CommandType type = CommandType.getType(id);
        switch (type) {
            case ADD_USER_TO_PRIVATE_GROUP:
                return new AddUserToPrivateGroupCommand();
            case SHOW_ALL_GOALS:
                return new ShowAllGoalsCommand();
            case GET_USERS_INVITES:
                return new GetUsersInvitesCommand();
            default:
                throw new NotRealizedMethodException("Not realized for type: " + type);
        }
    }
}
