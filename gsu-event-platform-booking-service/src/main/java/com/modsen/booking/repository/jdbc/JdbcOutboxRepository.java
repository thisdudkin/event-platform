package com.modsen.booking.repository.jdbc;

import com.modsen.booking.model.OutboxEvent;
import com.modsen.booking.repository.OutboxRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexander Dudkin
 */
@Repository
public class JdbcOutboxRepository implements OutboxRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcOutboxRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(OutboxEvent event) {
        jdbcTemplate.update("INSERT INTO outbox (id, event_data, is_sent) VALUES (?, ?, ?)",
                event.id(),
                event.eventData(),
                event.isSent()
        );
    }

    @Override
    public void markAsProcessed(String id) {
        jdbcTemplate.update("UPDATE outbox SET is_sent = true WHERE id = ?", id);
    }

    @Override
    public List<OutboxEvent> findAllUnprocessed() {
        final String sql = """
                SELECT
                    outbox.id,
                    outbox.event_data,
                    outbox.created_at,
                    outbox.is_sent
                FROM outbox
                WHERE is_sent = false
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new OutboxEvent(
                        rs.getString("id"),
                        rs.getBytes("event_data"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getBoolean("is_sent")
                )
        );
    }
}
