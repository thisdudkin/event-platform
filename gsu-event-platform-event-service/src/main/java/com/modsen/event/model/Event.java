package com.modsen.event.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record Event(
        UUID id,
        String title,
        String description,
        LocalDateTime startDateTime,
        String location,
        UUID organizerId,
        UUID createdById,
        EventType type,
        EventStatus status,
        Integer capacity,
        LocalDateTime createdTime,
        LocalDateTime updatedTime,
        String contactInfo,
        boolean isPublic,
        String eligibilityCriteria,
        BigDecimal budget,
        String imageUrl,
        LocalDateTime registrationDeadline,
        List<String> tags,
        List<String> attachments
) implements Serializable { }
