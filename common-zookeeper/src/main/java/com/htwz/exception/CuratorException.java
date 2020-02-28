package com.htwz.exception;



public class CuratorException extends RuntimeException {
    private static final long serialVersionUID = 851864048447611118L;

    public CuratorException() {
        super();
    }

    public CuratorException(String message) {
        super(message);
    }

    public CuratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CuratorException(Throwable cause) {
        super(cause);
    }
}