package com.dearlavion.authservice.common.exception;

public class BadRequestException extends RuntimeException {
    private final Object body;

    public BadRequestException(String message) {
        super(message);
        this.body = null;
    }

    public BadRequestException(String message, Object body) {
        super(message);
        this.body = body;
    }

    public Object getBody() {
        return body;
    }
}
