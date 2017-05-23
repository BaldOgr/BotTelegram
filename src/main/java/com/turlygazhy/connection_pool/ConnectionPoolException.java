package com.turlygazhy.connection_pool;

/**
 * Created by user on 12/11/16.
 */
public class ConnectionPoolException extends RuntimeException {
    public ConnectionPoolException(Exception e) {
        super(e);
    }

    public ConnectionPoolException(String s) {
        super(s);
    }
}
