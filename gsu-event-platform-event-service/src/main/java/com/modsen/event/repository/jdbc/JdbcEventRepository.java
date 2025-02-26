package com.modsen.event.repository.jdbc;

import com.modsen.event.model.Event;
import com.modsen.event.model.EventStatus;
import com.modsen.event.repository.EventRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Repository
public class JdbcEventRepository implements EventRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Event> findByStatus(EventStatus status, int page, int size) {
        return null;
    }

    @Override
    public Iterable<Event> findByOrganizer(UUID organizerId, int page, int size) {
        return null;
    }

    @Override
    public Iterable<Event> findByCreator(UUID creatorId, int page, int size) {
        return null;
    }

    @Override
    public Iterable<Event> findBetweenDates(LocalDateTime start, LocalDateTime end, int page, int size) {
        return null;
    }

    @Override
    public Iterable<Event> findByTags(Collection<String> tags, int page, int size) {
        return null;
    }

    @Override
    public Iterable<Event> findByRegistrationDeadlineApproaching(LocalDateTime deadline, int page, int size) {
        return null;
    }

    @Override
    public Iterable<Event> findPublicEvents(int page, int size) {
        return null;
    }

    @Override
    public void delete(Event entity) {

    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public Iterable<Event> findAll(int page, int size) {
        return null;
    }

    @Override
    public Optional<Event> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public <S extends Event> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Event> S update(UUID id, S entity) {
        return null;
    }

}
