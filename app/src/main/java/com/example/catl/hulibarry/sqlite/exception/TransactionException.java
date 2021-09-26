package com.example.catl.hulibarry.sqlite.exception;

/**
 * Created by tanqimin on 2015/11/3.
 */
public class TransactionException extends RuntimeException {
    public TransactionException() {
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
