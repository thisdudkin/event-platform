package com.modsen.booking.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Alexander Dudkin
 */
public record OutboxEvent(
        String id,
        byte[] eventData,
        LocalDateTime createdAt,
        boolean isSent
) implements Serializable { }
