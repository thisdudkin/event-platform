package com.modsen.event.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.event.exception.RepositoryException;
import com.modsen.event.mapper.parser.JsonFieldParser;
import com.modsen.event.model.Event;
import com.modsen.event.model.EventStatus;
import com.modsen.event.model.EventType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Component
public class EventRowMapper implements RowMapper<Event> {

    private final ObjectMapper objectMapper;
    private final Map<String, JsonFieldParser> jsonParsers;

    public EventRowMapper(ObjectMapper objectMapper, Map<String, JsonFieldParser> jsonParsers) {
        this.jsonParsers = jsonParsers;
        this.objectMapper = objectMapper;
    }

    @Override
    public Event mapRow(ResultSet eventSet, int rowNum) throws SQLException {
        return new Event(
                eventSet.getObject("event_id", UUID.class),
                eventSet.getString("title"),
                eventSet.getString("description"),
                eventSet.getTimestamp("start_time").toLocalDateTime(),
                eventSet.getString("location"),
                eventSet.getObject("organizer_id", UUID.class),
                eventSet.getObject("created_by", UUID.class),
                parseJsonObject(eventSet.getString("event_type"), EventType.class),
                parseJsonObject(eventSet.getString("event_status"), EventStatus.class),
                eventSet.getInt("capacity"),
                eventSet.getTimestamp("created_time").toLocalDateTime(),
                eventSet.getTimestamp("updated_time").toLocalDateTime(),
                eventSet.getString("contact_info"),
                eventSet.getBoolean("is_public"),
                eventSet.getString("eligibility_criteria"),
                eventSet.getBigDecimal("budget"),
                eventSet.getString("image_url"),
                eventSet.getTimestamp("registration_deadline").toLocalDateTime(),
                parseJsonArray(eventSet.getString("tags"), "name"),
                parseJsonArray(eventSet.getString("attachments"), "url")
        );
    }

    private <T> T parseJsonObject(String json, Class<T> clazz) {
        if (json == null || json.isBlank()) return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException ex) {
            throw new RepositoryException(ex.getOriginalMessage());
        }
    }

    private List<String> parseJsonArray(String json, String field) {
        JsonFieldParser jsonParser = jsonParsers.get(field.concat("Parser"));
        if (jsonParser == null) {
            throw new RepositoryException("No parser found for: " + field);
        }
        return jsonParser.parse(json);
    }

}
