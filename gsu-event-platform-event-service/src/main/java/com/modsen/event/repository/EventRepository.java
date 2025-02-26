package com.modsen.event.repository;

import com.modsen.event.model.Event;
import com.modsen.event.model.EventStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface EventRepository extends CrudRepository<Event, UUID> {

    Iterable<Event> findByStatus(EventStatus status, int page, int size);

    Iterable<Event> findByOrganizer(UUID organizerId, int page, int size);

    Iterable<Event> findByCreator(UUID creatorId, int page, int size);

    Iterable<Event> findBetweenDates(LocalDateTime start, LocalDateTime end, int page, int size);

    Iterable<Event> findByTags(Collection<String> tags, int page, int size);

    Iterable<Event> findByRegistrationDeadlineApproaching(LocalDateTime deadline, int page, int size);

    Iterable<Event> findPublicEvents(int page, int size);

}
