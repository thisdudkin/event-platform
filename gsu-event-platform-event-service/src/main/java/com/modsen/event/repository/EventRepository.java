package com.modsen.event.repository;

import com.modsen.event.enums.EventStatus;
import com.modsen.event.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface EventRepository extends CrudRepository<Event, UUID> {

    Iterable<Event> findByStatus(EventStatus status, UUID lastFetchedId, int size);

    Iterable<Event> findByOrganizer(UUID organizerId, UUID lastFetchedId, int size);

    Iterable<Event> findByCreator(UUID creatorId, UUID lastFetchedId, int size);

    Iterable<Event> findBetweenDates(LocalDateTime start, LocalDateTime end, UUID lastFetchedId, int size);

    Iterable<Event> findByTags(Collection<String> tags, UUID lastFetchedId, int size);

    Iterable<Event> findByRegistrationDeadlineApproaching(LocalDateTime deadline, UUID lastFetchedId, int size);

    Iterable<Event> findPublicEvents(UUID lastFetchedId, int size);

}
