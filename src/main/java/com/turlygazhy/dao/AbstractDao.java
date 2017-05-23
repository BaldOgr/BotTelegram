package com.turlygazhy.dao;

/**
 * Created by user on 12/16/16.
 */
public abstract class AbstractDao {
    protected static final int ID_INDEX = 1;
    protected static DaoFactory factory;

    static {
        factory = DaoFactory.getFactory();
    }
}
