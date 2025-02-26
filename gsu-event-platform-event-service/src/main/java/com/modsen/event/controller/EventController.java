package com.modsen.event.controller;

import com.modsen.event.dto.request.EventCreateRequest;
import com.modsen.event.dto.request.EventUpdateRequest;
import com.modsen.event.dto.response.EventResponse;
import com.modsen.event.enums.EventStatus;
import com.modsen.event.repository.Page;
import com.modsen.event.service.EventService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventCreateRequest request) {
        EventResponse createdEvent = eventService.createEvent(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEvent.id())
                .toUri();
        return ResponseEntity.created(location).body(createdEvent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable UUID id,
            @Valid @RequestBody EventUpdateRequest request
    ) {
        return ResponseEntity.ok(eventService.updateEvent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAllEvents(
            @RequestParam(required = false) UUID lastFetchedId,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(eventService.getAllEvents(lastFetchedId, size));
    }

    @GetMapping(params = "organizerId")
    public ResponseEntity<Page<EventResponse>> getEventsByOrganizer(
            @RequestParam UUID organizerId,
            @RequestParam(required = false) UUID lastFetchedId,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(eventService.getEventsByOrganizer(organizerId, lastFetchedId, size));
    }

    @GetMapping(params = "status")
    public ResponseEntity<Page<EventResponse>> getEventsByStatus(
            @RequestParam EventStatus status,
            @RequestParam(required = false) UUID lastFetchedId,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(eventService.getEventsByStatus(status, lastFetchedId, size));
    }

    @GetMapping(params = {"start", "end"})
    public ResponseEntity<Page<EventResponse>> getEventsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) UUID lastFetchedId,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(eventService.getEventsBetweenDates(start, end, lastFetchedId, size));
    }

}
