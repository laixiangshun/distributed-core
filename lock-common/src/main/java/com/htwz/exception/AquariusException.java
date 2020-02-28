package com.htwz.exception;

public class AquariusException extends RuntimeException {
    private static final long serialVersionUID = 7895884193269203187L;

    public AquariusException() {
        super();
    }

    public AquariusException(String message) {
        super(message);
    }

    public AquariusException(String message, Throwable cause) {
        super(message, cause);
    }

    public AquariusException(Throwable cause) {
        super(cause);
    }
}