package com.modsen.event.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }
}
