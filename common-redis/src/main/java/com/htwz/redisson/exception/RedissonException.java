package com.htwz.redisson.exception;



public class RedissonException extends RuntimeException {
    private static final long serialVersionUID = 4550515832057492430L;

    public RedissonException() {
        super();
    }

    public RedissonException(String message) {
        super(message);
    }

    public RedissonException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedissonException(Throwable cause) {
        super(cause);
    }
}