package com.modsen.event.repository.jdbc;

import com.github.f4b6a3.uuid.UuidCreator;
import com.modsen.event.enums.EventStatus;
import com.modsen.event.exception.RepositoryException;
import com.modsen.event.model.Event;
import com.modsen.event.repository.EventRepository;
import com.modsen.event.mapper.EventRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Repository
public class JdbcEventRepository implements EventRepository {

    private final JdbcClient jdbcClient;
    private final EventRowMapper rowMapper;

    public JdbcEventRepository(JdbcClient jdbcClient, EventRowMapper rowMapper) {
        this.jdbcClient = jdbcClient;
        this.rowMapper = rowMapper;
    }

    @Override
    public Optional<Event> findById(UUID uuid) {
        final String sql = """
                SELECT
                    events.id AS event_id,
                    events.title,
                    events.description,
                    events.start_time,
                    events.location,
                    events.organizer_id,
                    events.created_by,
                    events.capacity,
                    events.type,
                    events.status,
                    events.created_time,
                    events.updated_time,
                    events.contact_info,
                    events.is_public,
                    events.eligibility_criteria,
                    events.budget,
                    events.image_url,
                    events.registration_deadline,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'id', tags.id,
                        'name', tags.name
                    )) FILTER (WHERE tags.id IS NOT NULL), '[]'::jsonb) AS tags,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'url', event_attachments.url
                    )) FILTER (WHERE event_attachments.url IS NOT NULL), '[]'::jsonb) AS attachments
                
                FROM events
                LEFT JOIN event_tags ON events.id = event_tags.event_id
                LEFT JOIN tags ON event_tags.tag_id = tags.id
                LEFT JOIN event_attachments ON events.id = event_attachments.event_id
                WHERE events.id = ?
                GROUP BY events.id, events.type, events.status
                """;

        return jdbcClient.sql(sql)
                .param(1, uuid)
                .query(rowMapper)
                .optional();
    }

    @Override
    public List<Event> findAll(UUID lastFetchedId, int size) {
        final String sql = """
                SELECT
                    events.id AS event_id,
                    events.title,
                    events.description,
                    events.start_time,
                    events.location,
                    events.organizer_id,
                    events.created_by,
                    events.capacity,
                    events.type,
                    events.status,
                    events.created_time,
                    events.updated_time,
                    events.contact_info,
                    events.is_public,
                    events.eligibility_criteria,
                    events.budget,
                    events.image_url,
                    events.registration_deadline,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'id', tags.id,
                        'name', tags.name
                    )) FILTER (WHERE tags.id IS NOT NULL), '[]'::jsonb) AS tags,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'url', event_attachments.url
                    )) FILTER (WHERE event_attachments.url IS NOT NULL), '[]'::jsonb) AS attachments
                
                FROM events
                LEFT JOIN event_tags ON events.id = event_tags.event_id
                LEFT JOIN tags ON event_tags.tag_id = tags.id
                LEFT JOIN event_attachments ON events.id = event_attachments.event_id
                WHERE events.id > COALESCE(?, '00000000-0000-0000-0000-000000000000'::uuid)
                GROUP BY events.id, events.type, events.status
                ORDER BY events.id
                LIMIT ?
                """;

        return jdbcClient.sql(sql)
                .param(1, lastFetchedId)
                .param(2, size)
                .query(rowMapper)
                .list();
    }

    @Override
    public List<Event> findByStatus(EventStatus status, UUID lastFetchedId, int size) {
        final String sql = """
                SELECT
                    events.id AS event_id,
                    events.title,
                    events.description,
                    events.start_time,
                    events.location,
                    events.organizer_id,
                    events.created_by,
                    events.capacity,
                    events.type,
                    events.status,
                    events.created_time,
                    events.updated_time,
                    events.contact_info,
                    events.is_public,
                    events.eligibility_criteria,
                    events.budget,
                    events.image_url,
                    events.registration_deadline,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'id', tags.id,
                        'name', tags.name
                    )) FILTER (WHERE tags.id IS NOT NULL), '[]'::jsonb) AS tags,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'url', event_attachments.url
                    )) FILTER (WHERE event_attachments.url IS NOT NULL), '[]'::jsonb) AS attachments
                
                FROM events
                LEFT JOIN event_tags ON events.id = event_tags.event_id
                LEFT JOIN tags ON event_tags.tag_id = tags.id
                LEFT JOIN event_attachments ON events.id = event_attachments.event_id
                WHERE events.id > COALESCE(?, '00000000-0000-0000-0000-000000000000'::uuid) AND events.status = ?
                GROUP BY events.id, events.type, events.status
                ORDER BY events.id
                LIMIT ?
                """;

        return jdbcClient.sql(sql)
                .param(1, lastFetchedId)
                .param(2, status.name())
                .param(3, size)
                .query(rowMapper)
                .list();
    }

    @Override
    public List<Event> findByOrganizer(UUID organizerId, UUID lastFetchedId, int size) {
        final String sql = """
                SELECT
                    events.id AS event_id,
                    events.title,
                    events.description,
                    events.start_time,
                    events.location,
                    events.organizer_id,
                    events.created_by,
                    events.capacity,
                    events.type,
                    events.status,
                    events.created_time,
                    events.updated_time,
                    events.contact_info,
                    events.is_public,
                    events.eligibility_criteria,
                    events.budget,
                    events.image_url,
                    events.registration_deadline,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'id', tags.id,
                        'name', tags.name
                    )) FILTER (WHERE tags.id IS NOT NULL), '[]'::jsonb) AS tags,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'url', event_attachments.url
                    )) FILTER (WHERE event_attachments.url IS NOT NULL), '[]'::jsonb) AS attachments
                
                FROM events
                LEFT JOIN event_tags ON events.id = event_tags.event_id
                LEFT JOIN tags ON event_tags.tag_id = tags.id
                LEFT JOIN event_attachments ON events.id = event_attachments.event_id
                WHERE events.id > COALESCE(?, '00000000-0000-0000-0000-000000000000'::uuid) AND events.organizer_id = ?
                GROUP BY events.id, events.type, events.status
                ORDER BY events.id
                LIMIT ?
                """;

        return jdbcClient.sql(sql)
                .param(1, lastFetchedId)
                .param(2, organizerId)
                .param(3, size)
                .query(rowMapper)
                .list();
    }

    @Override
    public List<Event> findByCreator(UUID creatorId, UUID lastFetchedId, int size) {
        final String sql = """
                SELECT
                    events.id AS event_id,
                    events.title,
                    events.description,
                    events.start_time,
                    events.location,
                    events.organizer_id,
                    events.created_by,
                    events.capacity,
                    events.type,
                    events.status,
                    events.created_time,
                    events.updated_time,
                    events.contact_info,
                    events.is_public,
                    events.eligibility_criteria,
                    events.budget,
                    events.image_url,
                    events.registration_deadline,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'id', tags.id,
                        'name', tags.name
                    )) FILTER (WHERE tags.id IS NOT NULL), '[]'::jsonb) AS tags,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'url', event_attachments.url
                    )) FILTER (WHERE event_attachments.url IS NOT NULL), '[]'::jsonb) AS attachments
                
                FROM events
                LEFT JOIN event_tags ON events.id = event_tags.event_id
                LEFT JOIN tags ON event_tags.tag_id = tags.id
                LEFT JOIN event_attachments ON events.id = event_attachments.event_id
                WHERE events.id > ? AND events.created_by = ?
                GROUP BY events.id, events.type, events.status
                ORDER BY events.id
                LIMIT ?
                """;

        return jdbcClient.sql(sql)
                .param(1, lastFetchedId)
                .param(2, creatorId)
                .param(3, size)
                .query(rowMapper)
                .list();
    }

    @Override
    public List<Event> findBetweenDates(LocalDateTime start, LocalDateTime end, UUID lastFetchedId, int size) {
        final String sql = """
                SELECT
                    events.id AS event_id,
                    events.title,
                    events.description,
                    events.start_time,
                    events.location,
                    events.organizer_id,
                    events.created_by,
                    events.capacity,
                    events.type,
                    events.status,
                    events.created_time,
                    events.updated_time,
                    events.contact_info,
                    events.is_public,
                    events.eligibility_criteria,
                    events.budget,
                    events.image_url,
                    events.registration_deadline,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'id', tags.id,
                        'name', tags.name
                    )) FILTER (WHERE tags.id IS NOT NULL), '[]'::jsonb) AS tags,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'url', event_attachments.url
                    )) FILTER (WHERE event_attachments.url IS NOT NULL), '[]'::jsonb) AS attachments
                
                FROM events
                LEFT JOIN event_tags ON events.id = event_tags.event_id
                LEFT JOIN tags ON event_tags.tag_id = tags.id
                LEFT JOIN event_attachments ON events.id = event_attachments.event_id
                WHERE events.id > COALESCE(?, '00000000-0000-0000-0000-000000000000'::uuid) AND events.start_time BETWEEN ? AND ?
                GROUP BY events.id, events.type, events.status
                ORDER BY events.id
                LIMIT ?
                """;

        return jdbcClient.sql(sql)
                .param(1, lastFetchedId)
                .param(2, start)
                .param(3, end)
                .param(4, size)
                .query(rowMapper)
                .list();
    }

    @Override
    public List<Event> findByTags(Collection<String> tags, UUID lastFetchedId, int size) {
        final String sql = """
                SELECT
                    events.id AS event_id,
                    events.title,
                    events.description,
                    events.start_time,
                    events.location,
                    events.organizer_id,
                    events.created_by,
                    events.capacity,
                    events.type,
                    events.status,
                    events.created_time,
                    events.updated_time,
                    events.contact_info,
                    events.is_public,
                    events.eligibility_criteria,
                    events.budget,
                    events.image_url,
                    events.registration_deadline,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'id', tags.id,
                        'name', tags.name
                    )) FILTER (WHERE tags.id IS NOT NULL), '[]'::jsonb) AS tags,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'url', event_attachments.url
                    )) FILTER (WHERE event_attachments.url IS NOT NULL), '[]'::jsonb) AS attachments
                
                FROM events
                LEFT JOIN event_tags ON events.id = event_tags.event_id
                LEFT JOIN tags ON event_tags.tag_id = tags.id
                LEFT JOIN event_attachments ON events.id = event_attachments.event_id
                WHERE events.id > ? AND tags.name IN (SELECT * FROM unnest(?::text[]))
                GROUP BY events.id, events.type, events.status
                HAVING COUNT(DISTINCT tags.name) = ?
                ORDER BY events.id
                LIMIT ?
                """;

        return jdbcClient.sql(sql)
                .param(1, lastFetchedId)
                .param(2, tags.toArray(new String[0]))
                .param(3, tags.size())
                .param(4, size)
                .query(rowMapper)
                .list();
    }

    @Override
    public List<Event> findByRegistrationDeadlineApproaching(LocalDateTime deadline, UUID lastFetchedId, int size) {
        final String sql = """
                SELECT
                    events.id AS event_id,
                    events.title,
                    events.description,
                    events.start_time,
                    events.location,
                    events.organizer_id,
                    events.created_by,
                    events.capacity,
                    events.type,
                    events.status,
                    events.created_time,
                    events.updated_time,
                    events.contact_info,
                    events.is_public,
                    events.eligibility_criteria,
                    events.budget,
                    events.image_url,
                    events.registration_deadline,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'id', tags.id,
                        'name', tags.name
                    )) FILTER (WHERE tags.id IS NOT NULL), '[]'::jsonb) AS tags,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'url', event_attachments.url
                    )) FILTER (WHERE event_attachments.url IS NOT NULL), '[]'::jsonb) AS attachments
                
                FROM events
                LEFT JOIN event_tags ON events.id = event_tags.event_id
                LEFT JOIN tags ON event_tags.tag_id = tags.id
                LEFT JOIN event_attachments ON events.id = event_attachments.event_id
                WHERE events.id > ? AND events.registration_deadline <= ?
                GROUP BY events.id, events.registration_deadline, events.type, events.status
                ORDER BY events.registration_deadline, events.id
                LIMIT ?
                """;

        return jdbcClient.sql(sql)
                .param(1, lastFetchedId)
                .param(2, deadline)
                .param(3, size)
                .query(rowMapper)
                .list();
    }

    @Override
    public List<Event> findPublicEvents(UUID lastFetchedId, int size) {
        final String sql = """
                SELECT
                    events.id AS event_id,
                    events.title,
                    events.description,
                    events.start_time,
                    events.location,
                    events.organizer_id,
                    events.created_by,
                    events.capacity,
                    events.type,
                    events.status,
                    events.created_time,
                    events.updated_time,
                    events.contact_info,
                    events.is_public,
                    events.eligibility_criteria,
                    events.budget,
                    events.image_url,
                    events.registration_deadline,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'id', tags.id,
                        'name', tags.name
                    )) FILTER (WHERE tags.id IS NOT NULL), '[]'::jsonb) AS tags,
                
                    COALESCE(jsonb_agg(DISTINCT jsonb_build_object(
                        'url', event_attachments.url
                    )) FILTER (WHERE event_attachments.url IS NOT NULL), '[]'::jsonb) AS attachments
                
                FROM events
                LEFT JOIN event_tags ON events.id = event_tags.event_id
                LEFT JOIN tags ON event_tags.tag_id = tags.id
                LEFT JOIN event_attachments ON events.id = event_attachments.event_id
                WHERE events.id > ? AND events.is_public = true
                GROUP BY events.id, events.type, events.status
                ORDER BY events.id
                LIMIT ?
                """;

        return jdbcClient.sql(sql)
                .param(1, lastFetchedId)
                .param(2, size)
                .query(rowMapper)
                .list();
    }

    @Override
    public boolean existsById(UUID uuid) {
        final String sql = "SELECT EXISTS(SELECT 1 FROM events WHERE id = ?)";

        return Boolean.TRUE.equals(jdbcClient.sql(sql)
                .param(1, uuid)
                .query(Boolean.class)
                .single());
    }

    @Override
    public void delete(Event entity) {
        deleteById(entity.id());
    }

    @Override
    public void deleteById(UUID uuid) {
        final String deleteAttachmentsSql = "DELETE FROM event_attachments WHERE event_id = ?";
        final String deleteTagsSql = "DELETE FROM event_tags WHERE event_id = ?";
        final String deleteEventSql = "DELETE FROM events WHERE id = ?";

        jdbcClient.sql(deleteAttachmentsSql)
                .param(1, uuid).update();

        jdbcClient.sql(deleteTagsSql)
                .param(1, uuid).update();

        jdbcClient.sql(deleteEventSql)
                .param(1, uuid).update();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends Event> S save(S entity) {
        final String sql = """
                INSERT INTO events (
                    id, title, description, start_time, location, organizer_id, created_by,
                    capacity, created_time, updated_time, contact_info, is_public,
                    eligibility_criteria, budget, image_url, registration_deadline, type, status
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        UUID entityId = UuidCreator.getTimeOrderedEpoch();
        LocalDateTime createdNow = LocalDateTime.now();

        jdbcClient.sql(sql)
                .param(1, entityId)
                .param(2, entity.title())
                .param(3, entity.description())
                .param(4, entity.startDateTime())
                .param(5, entity.location())
                .param(6, entity.organizerId())
                .param(7, entity.createdById())
                .param(8, entity.capacity())
                .param(9, createdNow)
                .param(10, createdNow)
                .param(11, entity.contactInfo())
                .param(12, entity.isPublic())
                .param(13, entity.eligibilityCriteria())
                .param(14, entity.budget())
                .param(15, entity.imageUrl())
                .param(16, entity.registrationDeadline())
                .param(17, entity.type().name())
                .param(18, entity.status().name())
                .update();

        processTags(entity.tags(), entityId);
        processAttachments(entity.attachments(), entityId);

        Event savedEntity = new Event(
                entityId,
                entity.title(),
                entity.description(),
                entity.startDateTime(),
                entity.location(),
                entity.organizerId(),
                entity.createdById(),
                entity.type(),
                entity.status(),
                entity.capacity(),
                createdNow,
                createdNow,
                entity.contactInfo(),
                entity.isPublic(),
                entity.eligibilityCriteria(),
                entity.budget(),
                entity.imageUrl(),
                entity.registrationDeadline(),
                entity.tags(),
                entity.attachments()
        );

        return (S) savedEntity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends Event> S update(UUID id, S entity) {
        final String updateEventSql = """
                UPDATE events
                SET
                    title = ?,
                    description = ?,
                    start_time = ?,
                    location = ?,
                    organizer_id = ?,
                    capacity = ?,
                    updated_time = ?,
                    contact_info = ?,
                    is_public = ?,
                    eligibility_criteria = ?,
                    budget = ?,
                    image_url = ?,
                    registration_deadline = ?,
                    type = ?,
                    status = ?
                WHERE id = ?
                """;

        LocalDateTime updatedNow = LocalDateTime.now();

        int updatedRows = jdbcClient.sql(updateEventSql)
                .param(1, entity.title())
                .param(2, entity.description())
                .param(3, entity.startDateTime())
                .param(4, entity.location())
                .param(5, entity.organizerId())
                .param(6, entity.capacity())
                .param(7, updatedNow)
                .param(8, entity.contactInfo())
                .param(9, entity.isPublic())
                .param(10, entity.eligibilityCriteria())
                .param(11, entity.budget())
                .param(12, entity.imageUrl())
                .param(13, entity.registrationDeadline())
                .param(14, entity.type().name())
                .param(15, entity.status().name())
                .param(16, id)
                .update();

        if (updatedRows == 0) {
            throw new RepositoryException("Event with id '" + id + "' not found");
        }

        jdbcClient.sql("DELETE FROM event_tags WHERE event_id = ?")
                .param(id).update();
        jdbcClient.sql("DELETE FROM event_attachments WHERE event_id = ?")
                .param(id).update();

        processTags(entity.tags(), id);
        processAttachments(entity.attachments(), id);

        Event updatedEvent = new Event(
                id,
                entity.title(),
                entity.description(),
                entity.startDateTime(),
                entity.location(),
                entity.organizerId(),
                entity.createdById(),
                entity.type(),
                entity.status(),
                entity.capacity(),
                entity.createdTime(),
                updatedNow,
                entity.contactInfo(),
                entity.isPublic(),
                entity.eligibilityCriteria(),
                entity.budget(),
                entity.imageUrl(),
                entity.registrationDeadline(),
                entity.tags(),
                entity.attachments()
        );

        return (S) updatedEvent;
    }

    private void processTags(List<String> tags, UUID eventId) {
        if (tags == null || tags.isEmpty()) {
            return;
        }

        for (String tagName : tags) {
            String selectTagSql = "SELECT id FROM tags WHERE name = ?";
            Optional<UUID> tagIdOpt = jdbcClient.sql(selectTagSql)
                    .param(tagName)
                    .query(UUID.class)
                    .optional();

            UUID tagId;
            if (tagIdOpt.isPresent()) {
                tagId = tagIdOpt.get();
            } else {
                tagId = UuidCreator.getTimeOrderedEpoch();
                jdbcClient.sql("INSERT INTO tags (id, name) VALUES (?, ?)")
                        .params(tagId, tagName)
                        .update();
            }
            jdbcClient.sql("INSERT INTO event_tags (event_id, tag_id) VALUES (?, ?)")
                    .params(eventId, tagId)
                    .update();
        }
    }

    private void processAttachments(List<String> attachments, UUID eventId) {
        if (attachments == null || attachments.isEmpty()) {
            return;
        }

        for (String url : attachments) {
            jdbcClient.sql("INSERT INTO event_attachments (event_id, url) VALUES (?, ?)")
                    .params(eventId, url)
                    .update();
        }
    }

}
