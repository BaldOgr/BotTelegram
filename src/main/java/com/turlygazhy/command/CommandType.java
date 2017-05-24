package com.turlygazhy.command;

import com.turlygazhy.exception.NotRealizedMethodException;

/**
 * Created by user on 1/1/17.
 */
public enum CommandType {
    HELLO(1),
    SHOW_ALL_GOALS(22),
    ADD_USER_TO_PRIVATE_GROUP(50),
    GET_USERS_INVITES(48),
    ADD_NEW_TASK(49),
    SHOW_TASKS(51);

    private final int id;

    CommandType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static CommandType getType(long id) {
        for (CommandType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new NotRealizedMethodException("There are no type for id: " + id);
    }
}
