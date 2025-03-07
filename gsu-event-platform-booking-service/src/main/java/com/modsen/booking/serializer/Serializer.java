package com.modsen.booking.serializer;

/**
 * @author Alexander Dudkin
 */
@FunctionalInterface
public interface Serializer<T> {
    byte[] serializeEvent(T event);
}
