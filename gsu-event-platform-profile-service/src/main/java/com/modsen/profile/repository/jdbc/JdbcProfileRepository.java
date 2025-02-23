package com.modsen.profile.repository.jdbc;

import com.github.f4b6a3.uuid.UuidCreator;
import com.modsen.profile.model.Profile;
import com.modsen.profile.repository.ProfileRepository;
import com.modsen.profile.utils.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Repository
public class JdbcProfileRepository implements ProfileRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcProfileRepository.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert profileInsert;
    private final SimpleJdbcInsert emailInsert;

    private static final String SELECT_PROFILE_JOIN_EMAIL =
            "SELECT p.id, p.user_id, pe.email, p.first_name, p.last_name, p.birth_date, p.gender, p.bio " +
            "FROM profiles p " +
            "LEFT JOIN profile_emails pe ON p.id = pe.profile_id ";

    private final RowMapper<Profile> profileRowMapper = (rs, rowNum) ->
            new Profile(
                    UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("user_id")),
                    rs.getString("email"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("birth_date") != null ? rs.getDate("birth_date").toLocalDate() : null,
                    rs.getString("gender") != null ? rs.getString("gender").charAt(0) : null,
                    rs.getString("bio")
            );

    public JdbcProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        this.profileInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(JdbcUtils.PROFILES_TABLE_NAME)
                .usingGeneratedKeyColumns(JdbcUtils.GENERATED_KEY_COLUMN);

        this.emailInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(JdbcUtils.PROFILE_EMAILS_TABLE_NAME)
                .usingGeneratedKeyColumns(JdbcUtils.GENERATED_KEY_COLUMN);
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM profile_emails WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByUserId(String userId) {
        String sql = "SELECT COUNT(*) FROM profiles WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    @Override
    public Optional<Profile> findByUserId(String userId) {
        try {
            String sql = SELECT_PROFILE_JOIN_EMAIL + "WHERE p.user_id = ?";
            Profile profile = jdbcTemplate.queryForObject(sql, profileRowMapper, userId);
            return Optional.ofNullable(profile);
        } catch (EmptyResultDataAccessException e) {
            log.debug("No profile found with userId: {}", userId);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Profile> findByEmail(String email) {
        try {
            String sql = SELECT_PROFILE_JOIN_EMAIL + "WHERE pe.email = ?";
            Profile profile = jdbcTemplate.queryForObject(sql, profileRowMapper, email);
            return Optional.ofNullable(profile);
        } catch (EmptyResultDataAccessException e) {
            log.debug("No profile found with email: {}", email);
            return Optional.empty();
        }
    }

    @Override
    public void delete(Profile entity) {
        if (entity.id() != null) {
            deleteById(entity.id());
        }
    }

    @Override
    public void deleteById(UUID uuid) {
        jdbcTemplate.update("DELETE FROM profile_emails WHERE profile_id = ?", uuid);
        jdbcTemplate.update("DELETE FROM profiles WHERE id = ?", uuid);
        log.debug("Deleted profile with id: {}", uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        String sql = "SELECT COUNT(*) FROM profiles WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, uuid);
        return count != null && count > 0;
    }

    @Override
    public Iterable<Profile> findAll(int page, int size) {
        String sql = SELECT_PROFILE_JOIN_EMAIL +
                     "ORDER BY p.last_name, p.first_name " +
                     "LIMIT ? OFFSET ?";
        int offset = (page - 1) * size;
        return jdbcTemplate.query(sql, profileRowMapper, size, offset);
    }

    @Override
    public Optional<Profile> findById(UUID uuid) {
        try {
            String sql = SELECT_PROFILE_JOIN_EMAIL + "WHERE p.id = ?";
            Profile profile = jdbcTemplate.queryForObject(sql, profileRowMapper, uuid);
            return Optional.ofNullable(profile);
        } catch (EmptyResultDataAccessException e) {
            log.debug("No profile found with id: {}", uuid);
            return Optional.empty();
        }
    }

    @Override
    public <S extends Profile> S save(S entity) {
        if (entity.id() != null && existsById(entity.id())) {
            return update(entity.id(), entity);
        }

        UUID profileId = entity.id() != null ? entity.id() : UuidCreator.getTimeOrderedEpoch();

        MapSqlParameterSource profileParams = new MapSqlParameterSource()
                .addValue("id", profileId)
                .addValue("user_id", entity.userId())
                .addValue("first_name", entity.firstname())
                .addValue("last_name", entity.lastname())
                .addValue("birth_date", entity.birthdate())
                .addValue("gender", entity.gender().toString())
                .addValue("bio", entity.bio());

        namedParameterJdbcTemplate.update(
                "INSERT INTO profiles (id, user_id, first_name, last_name, birth_date, gender, bio) " +
                "VALUES (:id, :user_id, :first_name, :last_name, :birth_date, :gender, :bio)",
                profileParams);

        if (entity.email() != null) {
            UUID emailId = UuidCreator.getTimeOrderedEpoch();
            MapSqlParameterSource emailParams = new MapSqlParameterSource()
                    .addValue("id", emailId)
                    .addValue("profile_id", profileId)
                    .addValue("email", entity.email());

            namedParameterJdbcTemplate.update(
                    "INSERT INTO profile_emails (id, profile_id, email) " +
                    "VALUES (:id, :profile_id, :email)",
                    emailParams);
        }

        @SuppressWarnings("unchecked")
        S savedEntity = (S) new Profile(
                profileId,
                entity.userId(),
                entity.email(),
                entity.firstname(),
                entity.lastname(),
                entity.birthdate(),
                entity.gender(),
                entity.bio()
        );

        return savedEntity;
    }

    @Override
    public <S extends Profile> S update(UUID id, S entity) {
        MapSqlParameterSource profileParams = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("user_id", entity.userId())
                .addValue("first_name", entity.firstname())
                .addValue("last_name", entity.lastname())
                .addValue("birth_date", entity.birthdate())
                .addValue("gender", entity.gender().toString())
                .addValue("bio", entity.bio());

        namedParameterJdbcTemplate.update(
                "UPDATE profiles SET " +
                "user_id = :user_id, " +
                "first_name = :first_name, " +
                "last_name = :last_name, " +
                "birth_date = :birth_date, " +
                "gender = :gender, " +
                "bio = :bio " +
                "WHERE id = :id",
                profileParams);

        Integer emailCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM profile_emails WHERE profile_id = ?",
                Integer.class,
                id);

        if (entity.email() != null) {
            if (emailCount != null && emailCount > 0) {
                jdbcTemplate.update(
                        "UPDATE profile_emails SET email = ? WHERE profile_id = ?",
                        entity.email(), id);
            } else {
                UUID emailId = UuidCreator.getTimeOrderedEpoch();
                MapSqlParameterSource emailParams = new MapSqlParameterSource()
                        .addValue("id", emailId)
                        .addValue("profile_id", id)
                        .addValue("email", entity.email());

                namedParameterJdbcTemplate.update(
                        "INSERT INTO profile_emails (id, profile_id, email) " +
                        "VALUES (:id, :profile_id, :email)",
                        emailParams);
            }
        }

        log.debug("Updated profile with id: {}", id);

        @SuppressWarnings("unchecked")
        S updatedEntity = (S) new Profile(
                id,
                entity.userId(),
                entity.email(),
                entity.firstname(),
                entity.lastname(),
                entity.birthdate(),
                entity.gender(),
                entity.bio()
        );

        return updatedEntity;
    }

}
