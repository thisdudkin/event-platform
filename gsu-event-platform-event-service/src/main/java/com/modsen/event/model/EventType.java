package com.modsen.event.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record EventType(
        UUID id,
        String name
) implements Serializable { }
