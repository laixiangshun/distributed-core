package com.htwz.proxy.exception;

public class ProxyException extends RuntimeException {
    private static final long serialVersionUID = -5563106933655728813L;

    public ProxyException() {
        super();
    }

    public ProxyException(String message) {
        super(message);
    }

    public ProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyException(Throwable cause) {
        super(cause);
    }
}