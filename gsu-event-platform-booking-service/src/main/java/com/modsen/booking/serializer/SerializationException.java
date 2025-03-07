package com.modsen.booking.serializer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SerializationException extends RuntimeException {
    public SerializationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
