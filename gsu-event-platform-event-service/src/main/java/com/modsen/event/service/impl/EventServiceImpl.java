package com.modsen.event.service.impl;

import com.modsen.event.dto.request.EventCreateRequest;
import com.modsen.event.dto.request.EventUpdateRequest;
import com.modsen.event.dto.response.EventResponse;
import com.modsen.event.model.EventStatus;
import com.modsen.event.repository.EventRepository;
import com.modsen.event.repository.Page;
import com.modsen.event.service.EventService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void deleteEvent(UUID id) {

    }

    @Override
    public EventResponse createEvent(EventCreateRequest request) {
        return null;
    }

    @Override
    public EventResponse updateEvent(UUID id, EventUpdateRequest request) {
        return null;
    }

    @Override
    public EventResponse getEventById(UUID id) {
        return null;
    }

    @Override
    public Page<EventResponse> getAllEvents(int page, int size) {
        return null;
    }

    @Override
    public Page<EventResponse> getEventsByOrganizer(UUID organizerId, int page, int size) {
        return null;
    }

    @Override
    public Page<EventResponse> getEventsByStatus(EventStatus status, int page, int size) {
        return null;
    }

    @Override
    public Page<EventResponse> getEventsBetweenDates(LocalDateTime start, LocalDateTime end, int page, int size) {
        return null;
    }

    @Override
    public Page<EventResponse> searchEvents(String query, int page, int size) {
        return null;
    }

    @Override
    public void addTagToEvent(UUID eventId, String tag) {

    }

    @Override
    public void removeTagFromEvent(UUID eventId, String tag) {

    }

    @Override
    public void addAttachmentToEvent(UUID eventId, String url) {

    }

    @Override
    public void removeAttachmentFromEvent(UUID eventId, String url) {

    }

    @Override
    public void checkEventCapacity(UUID eventId) {

    }

    @Override
    public void closeRegistration(UUID eventId) {

    }

    @Override
    public void publishEvent(UUID eventId) {

    }

    @Override
    public void cancelEvent(UUID eventId, String reason) {

    }

    @Override
    public int getEventParticipantsCount(UUID eventId) {
        return 0;
    }

    @Override
    public Map<EventStatus, Long> getEventStatistics() {
        return Map.of();
    }

}
