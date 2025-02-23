package com.modsen.event.service;

import com.modsen.event.dto.request.EventCreateRequest;
import com.modsen.event.dto.request.EventUpdateRequest;
import com.modsen.event.dto.response.EventResponse;
import com.modsen.event.enums.EventStatus;
import com.modsen.event.repository.Page;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface EventService {

    void deleteEvent(UUID id);
    EventResponse createEvent(EventCreateRequest request);
    EventResponse updateEvent(UUID id, EventUpdateRequest request);
    EventResponse getEventById(UUID id);
    Page<EventResponse> getAllEvents(UUID lastFetchedId, int size);

    Page<EventResponse> getEventsByOrganizer(UUID organizerId, UUID lastFetchedId, int size);
    Page<EventResponse> getEventsByStatus(EventStatus status, UUID lastFetchedId, int size);
    Page<EventResponse> getEventsBetweenDates(LocalDateTime start, LocalDateTime end, UUID lastFetchedId, int size);
    Page<EventResponse> searchEvents(String query, UUID lastFetchedId, int size);

    void addTagToEvent(UUID eventId, String tag);
    void removeTagFromEvent(UUID eventId, String tag);
    void addAttachmentToEvent(UUID eventId, String url);
    void removeAttachmentFromEvent(UUID eventId, String url);

    void checkEventCapacity(UUID eventId);
    void closeRegistration(UUID eventId);
    void publishEvent(UUID eventId);
    void cancelEvent(UUID eventId, String reason);

    int getEventParticipantsCount(UUID eventId);
    Map<EventStatus, Long> getEventStatistics();

}
