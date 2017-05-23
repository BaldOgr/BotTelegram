package com.turlygazhy.service;

import com.turlygazhy.command.Command;
import com.turlygazhy.command.impl.*;
import com.turlygazhy.entity.Button;
import com.turlygazhy.entity.Const;
import com.turlygazhy.exception.CommandNotFoundException;

import java.sql.SQLException;

/**
 * Created by user on 1/2/17.
 */
public class CommandService extends Service {

    public Command getCommand(String text) throws SQLException, CommandNotFoundException {
        if (text != null) {
            String[] split = text.split(":");
            String s = split[0];
            System.out.println(text);
            if (text.contains(Const.ACCESS_TO_BOT)) {
                if (s.contains(buttonDao.getButtonText(22))) {
                    return new GiveAccessToUserCommand(Integer.parseInt(split[1]));
                }
                if (s.contains(buttonDao.getButtonText(23))) {
                    return new DeclineUserCommand(Integer.parseInt(split[1]));
                }
            }
            if (text.contains(Const.TO_GROUP)) {
                String[] split1 = text.split(Const.TO_GROUP);
                return new AddUserToGroupCommand(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]));
            }
            if (s.equals(buttonDao.getButtonText(22))) {
                return new GiveAccessToGroupCommand(Long.parseLong(split[1]));
            }
            if (s.equals(buttonDao.getButtonText(23))) {
                return new DeclineGroupCommand(Long.parseLong(split[1]));
            }
        }

        Button button = buttonDao.getButton(text);
        return commandDao.getCommand(button.getCommandId());
    }
}
