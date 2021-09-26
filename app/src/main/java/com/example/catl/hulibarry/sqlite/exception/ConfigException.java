package com.example.catl.hulibarry.sqlite.exception;

/**
 * Created by tanqimin on 2016/1/20.
 */
public class ConfigException
        extends RuntimeException {
    public ConfigException() {
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }
}
