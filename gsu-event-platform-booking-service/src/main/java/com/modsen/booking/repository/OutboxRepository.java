package com.modsen.booking.repository;

import com.modsen.booking.model.OutboxEvent;

import java.util.List;

/**
 * @author Alexander Dudkin
 */
public interface OutboxRepository {
    void save(OutboxEvent event);

    void markAsProcessed(String id);

    List<OutboxEvent> findAllUnprocessed();
}
