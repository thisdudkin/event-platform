package com.modsen.booking.dto.request;

import com.modsen.booking.enums.BookingStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record BookingRequest(
        UUID userId, // FIXME
        UUID eventId,
        BigDecimal amount,
        BookingStatus status
) implements Serializable { }
