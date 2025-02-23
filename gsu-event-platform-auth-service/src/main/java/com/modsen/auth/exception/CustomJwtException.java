package com.modsen.auth.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * @author Alexander Dudkin
 */
public class CustomJwtException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String message;
    private final HttpStatus httpStatus;

    public CustomJwtException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
