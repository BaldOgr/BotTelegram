package com.turlygazhy.command;

import com.turlygazhy.command.impl.*;
import com.turlygazhy.exception.NotRealizedMethodException;

import static com.turlygazhy.command.CommandType.HELLO;

/**
 * Created by user on 1/2/17.
 */
public class CommandFactory {
    public static Command getCommand(long id) {
        CommandType type = CommandType.getType(id);
        switch (type) {
            case HELLO:
                return new HELLO_COMMAND();
            case ADD_USER_TO_PRIVATE_GROUP:
                return new AddUserToPrivateGroupCommand();
            case SHOW_ALL_GOALS:
                return new ShowAllGoalsCommand();
            case GET_USERS_INVITES:
                return new GetUsersInvitesCommand();
            case ADD_NEW_TASK:
                return new AddNewTaskCommand();
            case SHOW_TASKS:
                return new ShowTasksCommand();
            default:
                throw new NotRealizedMethodException("Not realized for type: " + type);
        }
    }
}
