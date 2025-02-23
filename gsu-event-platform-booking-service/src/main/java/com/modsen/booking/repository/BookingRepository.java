package com.modsen.booking.repository;

import com.modsen.booking.model.Booking;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface BookingRepository extends CrudRepository<Booking, UUID> {
    Iterable<Booking> findByUserId(UUID userId, UUID lastFetchedId, int size);

    Iterable<Booking> findByEventId(UUID eventId, UUID lastFetchedId, int size);
}
