package com.modsen.event.repository;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record Page<T>(
        List<T> content,
        UUID lastFetchedId,
        int size
) implements Serializable { }
