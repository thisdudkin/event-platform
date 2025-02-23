package com.modsen.booking.dto.response;

import com.modsen.booking.enums.BookingStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record BookingResponse(
        UUID id,
        UUID userId,
        UUID eventId,
        BigDecimal amount,
        BookingStatus status,
        LocalDateTime createdTime,
        LocalDateTime updatedTime
) implements Serializable { }
