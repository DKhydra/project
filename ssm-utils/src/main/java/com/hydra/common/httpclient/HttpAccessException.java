package com.hydra.common.httpclient;

public class HttpAccessException extends Exception {

    private static final long serialVersionUID = 1L;

    private final int statusCode;

    public HttpAccessException(int statusCode) {
        super("Http Status Code: " + statusCode);
        this.statusCode = statusCode;
    }

    public HttpAccessException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
    }

    public HttpAccessException(Throwable cause) {
        super(cause);
        this.statusCode = -1;
    }

    public int getStatusCode() {
        return statusCode;
    }

}