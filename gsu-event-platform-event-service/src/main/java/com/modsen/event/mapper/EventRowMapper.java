package com.modsen.event.mapper;

import com.modsen.event.enums.EventStatus;
import com.modsen.event.enums.EventType;
import com.modsen.event.exception.RepositoryException;
import com.modsen.event.mapper.parser.JsonFieldParser;
import com.modsen.event.model.Event;
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

    private final Map<String, JsonFieldParser> jsonParsers;

    public EventRowMapper(Map<String, JsonFieldParser> jsonParsers) {
        this.jsonParsers = jsonParsers;
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
                EventType.valueOf(eventSet.getString("type")),
                EventStatus.valueOf(eventSet.getString("status")),
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

    private List<String> parseJsonArray(String json, String field) {
        JsonFieldParser jsonParser = jsonParsers.get(field.concat("Parser"));
        if (jsonParser == null) {
            throw new RepositoryException("No parser found for: " + field);
        }
        return jsonParser.parse(json);
    }

}
