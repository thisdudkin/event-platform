package com.modsen.event.dto.request;

import com.modsen.event.enums.EventStatus;
import com.modsen.event.enums.EventType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record EventCreateRequest(
        String title,
        String description,
        LocalDateTime startDateTime,
        String location,
        UUID organizerId,
        UUID createdById,
        EventType type,
        EventStatus status,
        Integer capacity,
        String contactInfo,
        boolean isPublic,
        String eligibilityCriteria,
        BigDecimal budget,
        String imageUrl,
        LocalDateTime registrationDeadline,
        List<String> tags,
        List<String> attachments
) implements Serializable { }
