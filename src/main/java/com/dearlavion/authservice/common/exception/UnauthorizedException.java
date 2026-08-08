package com.dearlavion.authservice.common.exception;

public class UnauthorizedException extends RuntimeException {
    /** Some callers (verify/login) need a non-string body shape, e.g. {valid:false} — stored
     * here and used verbatim as the response body when present, instead of the default
     * {statusCode,message,error} envelope. */
    private final Object body;

    public UnauthorizedException(String message) {
        super(message);
        this.body = null;
    }

    public UnauthorizedException(String message, Object body) {
        super(message);
        this.body = body;
    }

    public Object getBody() {
        return body;
    }
}
