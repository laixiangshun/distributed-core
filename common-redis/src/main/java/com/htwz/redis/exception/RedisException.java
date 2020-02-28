package com.htwz.redis.exception;

public class RedisException extends RuntimeException {
    private static final long serialVersionUID = -1137413793056992202L;

    public RedisException() {
        super();
    }

    public RedisException(String message) {
        super(message);
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }
}