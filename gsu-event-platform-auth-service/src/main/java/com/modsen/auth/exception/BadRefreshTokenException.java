package com.modsen.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Alexander Dudkin
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BadRefreshTokenException extends RuntimeException {
    public BadRefreshTokenException(String message) {
        super(message);
    }
}
