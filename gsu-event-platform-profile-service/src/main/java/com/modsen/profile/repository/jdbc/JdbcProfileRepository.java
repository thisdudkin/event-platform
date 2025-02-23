package com.modsen.profile.repository.jdbc;

import com.modsen.profile.model.Profile;
import com.modsen.profile.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
// FIXME
@Repository
public class JdbcProfileRepository implements ProfileRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcProfileRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public JdbcProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean existsByUserId(String userId) {
        return false;
    }

    @Override
    public Optional<Profile> findByUserId(String userId) {
        return Optional.empty();
    }

    @Override
    public Optional<Profile> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void delete(Profile entity) {

    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public Iterable<Profile> findAll(int page, int size) {
        return null;
    }

    @Override
    public Optional<Profile> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public <S extends Profile> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Profile> S update(UUID id, S entity) {
        return null;
    }

}
