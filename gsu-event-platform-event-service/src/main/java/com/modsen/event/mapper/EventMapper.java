package com.modsen.event.mapper;

import com.modsen.event.dto.request.EventCreateRequest;
import com.modsen.event.dto.request.EventUpdateRequest;
import com.modsen.event.dto.response.EventResponse;
import com.modsen.event.model.Event;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * @author Alexander Dudkin
 */
@Mapper(componentModel = SPRING)
public interface EventMapper {
    EventResponse toResponse(Event event);
    Event toEvent(EventCreateRequest request);
    Event toEvent(EventUpdateRequest request);
}
