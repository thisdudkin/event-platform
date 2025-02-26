package com.modsen.event.repository;

import java.io.Serializable;
import java.util.List;

/**
 * @author Alexander Dudkin
 */
public record Page<T>(
        List<T> content,
        int currentPage,
        int totalPages,
        long totalItems
) implements Serializable { }
