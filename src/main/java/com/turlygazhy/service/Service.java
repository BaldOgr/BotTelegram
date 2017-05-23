package com.turlygazhy.service;

import com.turlygazhy.dao.DaoFactory;
import com.turlygazhy.dao.impl.ButtonDao;
import com.turlygazhy.dao.impl.CommandDao;
import com.turlygazhy.dao.impl.KeyboardMarkUpDao;
import com.turlygazhy.dao.impl.MessageDao;

/**
 * Created by user on 1/2/17.
 */
public class Service {
    DaoFactory factory = DaoFactory.getFactory();
    MessageDao messageDao = factory.getMessageDao();
    KeyboardMarkUpDao keyboardMarkUpDao = factory.getKeyboardMarkUpDao();
    ButtonDao buttonDao = factory.getButtonDao();
    CommandDao commandDao = factory.getCommandDao();

}
