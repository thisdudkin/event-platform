package com.modsen.event.service.impl;

import com.modsen.event.dto.request.EventCreateRequest;
import com.modsen.event.dto.request.EventUpdateRequest;
import com.modsen.event.dto.response.EventResponse;
import com.modsen.event.exception.EventNotFoundException;
import com.modsen.event.mapper.EventMapper;
import com.modsen.event.model.Event;
import com.modsen.event.enums.EventStatus;
import com.modsen.event.repository.EventRepository;
import com.modsen.event.repository.Page;
import com.modsen.event.service.EventService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * @author Alexander Dudkin
 */
@Service
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    public EventServiceImpl(EventMapper eventMapper, EventRepository eventRepository) {
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
    }

    @Override
    public void deleteEvent(UUID eventId) {
        eventRepository.delete(getOrThrow(eventId));
    }

    @Override
    public EventResponse createEvent(EventCreateRequest request) {
        Event event = eventMapper.toEvent(request);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Override
    public EventResponse updateEvent(UUID eventId, EventUpdateRequest request) {
        Event event = getOrThrow(eventId);
        Event updated = eventRepository.update(eventId, eventMapper.toEvent(request));
        return eventMapper.toResponse(updated.withCreatedByAndTime(event.createdById(), event.createdTime()));
    }

    @Override
    public EventResponse getEventById(UUID eventId) {
        return eventMapper.toResponse(getOrThrow(eventId));
    }

    @Override
    public Page<EventResponse> getAllEvents(UUID lastFetchedId, int size) {
        Iterable<Event> events = eventRepository.findAll(lastFetchedId, size);
        List<EventResponse> content = StreamSupport.stream(events.spliterator(), false)
                .map(eventMapper::toResponse)
                .toList();
        UUID lastId = content.isEmpty() ? null : content.getLast().id();
        return new Page<>(content, lastId, size);
    }

    @Override
    public Page<EventResponse> getEventsByOrganizer(UUID organizerId, UUID lastFetchedId, int size) {
        Iterable<Event> events = eventRepository.findByOrganizer(organizerId, lastFetchedId, size);
        List<EventResponse> content = StreamSupport.stream(events.spliterator(), false)
                .map(eventMapper::toResponse)
                .toList();
        UUID lastId = content.isEmpty() ? null : content.getLast().id();
        return new Page<>(content, lastId, size);
    }

    @Override
    public Page<EventResponse> getEventsByStatus(EventStatus status, UUID lastFetchedId, int size) {
        Iterable<Event> events = eventRepository.findByStatus(status, lastFetchedId, size);
        List<EventResponse> content = StreamSupport.stream(events.spliterator(), false)
                .map(eventMapper::toResponse)
                .toList();
        UUID lastId = content.isEmpty() ? null : content.getLast().id();
        return new Page<>(content, lastId, size);
    }

    @Override
    public Page<EventResponse> getEventsBetweenDates(LocalDateTime start, LocalDateTime end, UUID lastFetchedId, int size) {
        Iterable<Event> events = eventRepository.findBetweenDates(start, end, lastFetchedId, size);
        List<EventResponse> content = StreamSupport.stream(events.spliterator(), false)
                .map(eventMapper::toResponse)
                .toList();
        UUID lastId = content.isEmpty() ? null : content.getLast().id();
        return new Page<>(content, lastId, size);
    }

    @Override
    public Page<EventResponse> searchEvents(String query, UUID lastFetchedId, int size) {
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

    private Event getOrThrow(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
    }

}
