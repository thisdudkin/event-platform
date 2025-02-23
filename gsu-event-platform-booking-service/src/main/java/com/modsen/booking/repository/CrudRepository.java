package com.modsen.booking.repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface CrudRepository<T, ID> {
    long count();

    void delete(T entity);

    void deleteById(ID id);

    boolean existsById(ID id);

    Iterable<T> findAll(UUID lastFetchedId, int size);

    Optional<T> findById(ID id);

    <S extends T> S save(S entity);
}
