package com.modsen.booking.dto.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record BookingEvent(
        UUID bookingId,
        UUID userId,
        UUID eventId,
        BigDecimal amount
) implements Serializable { }
