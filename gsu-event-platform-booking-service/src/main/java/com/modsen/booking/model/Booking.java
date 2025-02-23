package com.modsen.booking.model;

import com.modsen.booking.enums.BookingStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record Booking(
        UUID id,
        UUID userId,
        UUID eventId,
        BigDecimal amount,
        BookingStatus status,
        LocalDateTime createdTime,
        LocalDateTime updatedTime
) implements Serializable {
    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    public BookingBuilder toBuilder() {
        return new BookingBuilder()
                .id(this.id)
                .userId(this.userId)
                .eventId(this.eventId)
                .amount(this.amount)
                .status(this.status)
                .createdTime(this.createdTime)
                .updatedTime(this.updatedTime);
    }

    public static class BookingBuilder {
        private UUID id;
        private UUID userId;
        private UUID eventId;
        private BigDecimal amount;
        private BookingStatus status;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;

        BookingBuilder() {
        }

        public BookingBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public BookingBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public BookingBuilder eventId(UUID eventId) {
            this.eventId = eventId;
            return this;
        }

        public BookingBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public BookingBuilder status(BookingStatus status) {
            this.status = status;
            return this;
        }

        public BookingBuilder createdTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public BookingBuilder updatedTime(LocalDateTime updatedTime) {
            this.updatedTime = updatedTime;
            return this;
        }

        public Booking build() {
            return new Booking(this.id, this.userId, this.eventId, this.amount, this.status, this.createdTime, this.updatedTime);
        }

        public String toString() {
            return "Booking.BookingBuilder(id=" + this.id + ", userId=" + this.userId + ", eventId=" + this.eventId + ", amount=" + this.amount + ", status=" + this.status + ", createdTime=" + this.createdTime + ", updatedTime=" + this.updatedTime + ")";
        }
    }
}
