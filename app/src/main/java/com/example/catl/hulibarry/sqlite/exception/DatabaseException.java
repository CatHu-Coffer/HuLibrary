package com.example.catl.hulibarry.sqlite.exception;

/**
 * Created by tanqimin on 2015/11/3.
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException() {
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
