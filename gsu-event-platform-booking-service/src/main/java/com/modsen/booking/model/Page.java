package com.modsen.booking.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record Page<T>(
        List<T> content,
        int currentSize,
        UUID lastFetchedId
) implements Serializable { }
