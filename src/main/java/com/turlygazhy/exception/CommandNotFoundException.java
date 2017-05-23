package com.turlygazhy.exception;

/**
 * Created by user on 1/4/17.
 */
public class CommandNotFoundException extends Exception {
    public CommandNotFoundException(Exception e) {
        super(e);
    }
}
