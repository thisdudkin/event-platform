package com.modsen.event.model;

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
) implements Serializable {
    public Event withCreatedByAndTime(UUID createdById, LocalDateTime createdTime) {
        return this.createdById == createdById && this.createdTime == createdTime
                ? this
                : new Event(this.id, this.title, this.description, this.startDateTime, this.location, this.organizerId, createdById, this.type,
                this.status, this.capacity, createdTime, this.updatedTime, this.contactInfo, this.isPublic, this.eligibilityCriteria, this.budget,
                this.imageUrl, this.registrationDeadline, this.tags, this.attachments);
    }
}
