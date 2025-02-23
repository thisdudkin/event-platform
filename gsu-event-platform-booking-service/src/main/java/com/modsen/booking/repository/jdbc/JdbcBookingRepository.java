package com.modsen.booking.repository.jdbc;

import com.modsen.booking.mapper.jdbc.BookingRowMapper;
import com.modsen.booking.model.Booking;
import com.modsen.booking.repository.BookingRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Repository
public class JdbcBookingRepository implements BookingRepository {

    private final JdbcTemplate jdbcTemplate;
    private final BookingRowMapper rowMapper;

    public JdbcBookingRepository(JdbcTemplate jdbcTemplate, BookingRowMapper rowMapper) {
        this.rowMapper = rowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Booking> findByUserId(UUID userId, UUID lastFetchedId, int size) {
        final String sql = """
                SELECT
                    bookings.id,
                    bookings.user_id,
                    bookings.event_id,
                    bookings.amount,
                    bookings.status,
                    bookings.created_time,
                    bookings.updated_time
                FROM bookings
                WHERE bookings.id > COALESCE(?, '00000000-0000-0000-0000-000000000000'::uuid) AND bookings.user_id = ?
                ORDER BY bookings.id
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, rowMapper, lastFetchedId, userId , size);
    }

    @Override
    public Iterable<Booking> findByEventId(UUID eventId, UUID lastFetchedId, int size) {
        final String sql = """
                SELECT
                    bookings.id,
                    bookings.user_id,
                    bookings.event_id,
                    bookings.amount,
                    bookings.status,
                    bookings.created_time,
                    bookings.updated_time
                FROM bookings
                WHERE bookings.id > COALESCE(?, '00000000-0000-0000-0000-000000000000'::uuid) AND bookings.event_id = ?
                ORDER BY bookings.id
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, rowMapper, lastFetchedId, eventId, size);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public long count() {
        final String sql = "SELECT COUNT(*) FROM bookings";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public void delete(Booking booking) {
        deleteById(booking.id());
    }

    @Override
    public void deleteById(UUID bookingId) {
        final String sql = "DELETE FROM bookings WHERE id = ?";
        jdbcTemplate.update(sql, bookingId);
    }

    @Override
    public boolean existsById(UUID bookingId) {
        final String sql = "SELECT EXISTS(SELECT 1 FROM bookings WHERE id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, bookingId));
    }

    @Override
    public Iterable<Booking> findAll(UUID lastFetchedId, int size) {
        final String sql = """
                SELECT
                    bookings.id,
                    bookings.user_id,
                    bookings.event_id,
                    bookings.amount,
                    bookings.status,
                    bookings.created_time,
                    bookings.updated_time
                FROM bookings
                WHERE bookings.id > COALESCE(?, '00000000-0000-0000-0000-000000000000'::uuid)
                ORDER BY bookings.id
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, rowMapper, lastFetchedId, size);
    }

    @Override
    public Optional<Booking> findById(UUID bookingId) {
        final String sql = """
                SELECT
                    bookings.id,
                    bookings.user_id,
                    bookings.event_id,
                    bookings.amount,
                    bookings.status,
                    bookings.created_time,
                    bookings.updated_time
                FROM bookings
                WHERE bookings.id = ?
                """;
        try {
            Booking booking = jdbcTemplate.queryForObject(sql, rowMapper, bookingId);
            return Optional.ofNullable(booking);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends Booking> S save(S booking) {
        if (existsById(booking.id())) {
            final String sql = """
                    UPDATE bookings
                    SET user_id = ?, event_id = ?, amount = ?, status = ?, updated_time = CURRENT_TIMESTAMP
                    WHERE id = ?
                    RETURNING id, user_id, event_id, amount, status, created_time, updated_time
                    """;
            return (S) jdbcTemplate.queryForObject(
                    sql,
                    rowMapper,
                    booking.userId(),
                    booking.eventId(),
                    booking.amount(),
                    booking.status().name(),
                    booking.id()
            );
        } else {
            final String sql = """
                    INSERT INTO bookings (id, user_id, event_id, amount, status)
                    VALUES (uuid_generate_v7(), ?, ?, ?, ?)
                    RETURNING id, user_id, event_id, amount, status, created_time, updated_time
                    """;
            return (S) jdbcTemplate.queryForObject(
                    sql,
                    rowMapper,
                    booking.userId(),
                    booking.eventId(),
                    booking.amount(),
                    booking.status().name()
            );
        }
    }

}
