package com.modsen.auth.repository.jdbc;

import com.github.f4b6a3.uuid.UuidCreator;
import com.modsen.auth.security.user.Role;
import com.modsen.auth.model.User;
import com.modsen.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alexander Dudkin
 */
@Repository
public class JdbcUserRepository implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcUserRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsByUsername(String username) {
        final String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try {
            Boolean exists = jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, username);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                    return false;
                }
            });
            log.debug("existsByUsername: username={} exists={}", username, exists);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("existsByUsername error for username={}", username, e);
            throw e;
        }
    }

    @Override
    public boolean assignRole(User user, Role role) {
        final String checkSql = "SELECT COUNT(*) FROM user_roles WHERE user_id = ? AND role = ?";
        try {
            Boolean alreadyAssigned = jdbcTemplate.execute(checkSql, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setObject(1, user.getId());
                ps.setString(2, role.getAuthority());
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                    return false;
                }
            });
            if (alreadyAssigned != null && alreadyAssigned) {
                log.debug("assignRole: Role {} already assigned to user {}", role, user.getUsername());
                return false;
            }

            final String insertSql = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";
            Integer rowsAffected = jdbcTemplate.execute(insertSql, (PreparedStatementCallback<Integer>) ps -> {
                ps.setObject(1, user.getId());
                ps.setString(2, role.getAuthority());
                return ps.executeUpdate();
            });
            log.debug("assignRole: Role {} assigned to user {} (rowsAffected={})", role, user.getUsername(), rowsAffected);
            return rowsAffected != null && rowsAffected > 0;
        } catch (Exception e) {
            log.error("assignRole error for user {} and role {}", user.getUsername(), role, e);
            throw e;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final String sqlUser = "SELECT id, username, password FROM users WHERE username = ?";
        try {
            User user = jdbcTemplate.execute(sqlUser, (PreparedStatementCallback<User>) ps -> {
                ps.setString(1, username);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        UUID id = resultSet.getObject("id", UUID.class);
                        String uname = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        Set<Role> roles = getRolesByUserId(id);
                        return new User(id, uname, password, roles);
                    }
                    return null;
                }
            });
            if (user != null) {
                log.debug("findByUsername: Found user for username={}", username);
                return Optional.of(user);
            } else {
                log.info("findByUsername: No user found for username={}", username);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("findByUsername error for username={}", username, e);
            throw e;
        }
    }


    @Override
    public void delete(User user) {
        final String deleteRolesSql = "DELETE FROM user_roles WHERE user_id = ?";
        final String deleteUserSql = "DELETE FROM users WHERE id = ?";
        try {
            jdbcTemplate.execute(deleteRolesSql, (PreparedStatementCallback<Void>) ps -> {
                ps.setObject(1, user.getId());
                ps.executeUpdate();
                return null;
            });
            Integer rowsAffected = jdbcTemplate.execute(deleteUserSql, (PreparedStatementCallback<Integer>) ps -> {
                ps.setObject(1, user.getId());
                return ps.executeUpdate();
            });
            log.debug("delete: Deleted user {} (rowsAffected={})", user.getUsername(), rowsAffected);
        } catch (Exception e) {
            log.error("delete error for user {}", user.getUsername(), e);
            throw e;
        }
    }

    @Override
    public void deleteById(UUID uuid) {
        final String deleteRolesSql = "DELETE FROM user_roles WHERE user_id = ?";
        final String deleteUserSql = "DELETE FROM users WHERE id = ?";
        try {
            jdbcTemplate.execute(deleteRolesSql, (PreparedStatementCallback<Void>) ps -> {
                ps.setObject(1, uuid);
                ps.executeUpdate();
                return null;
            });
            Integer rowsAffected = jdbcTemplate.execute(deleteUserSql, (PreparedStatementCallback<Integer>) ps -> {
                ps.setObject(1, uuid);
                return ps.executeUpdate();
            });
            log.debug("deleteById: Deleted user id={} (rowsAffected={})", uuid, rowsAffected);
        } catch (Exception e) {
            log.error("deleteById error for id={}", uuid, e);
            throw e;
        }
    }

    @Override
    public boolean existsById(UUID uuid) {
        final String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        try {
            Boolean exists = jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setObject(1, uuid);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                    return false;
                }
            });
            log.debug("existsById: id={} exists={}", uuid, exists);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("existsById error for id={}", uuid, e);
            throw e;
        }
    }

    @Override
    public List<User> findAll(int page, int size) {
        final String sql = "SELECT id, username, password FROM users LIMIT ? OFFSET ?";
        int offset = page * size;
        List<User> users = new ArrayList<>();
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, size);
                ps.setInt(2, offset);
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        UUID id = resultSet.getObject("id", UUID.class);
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        Set<Role> roles = getRolesByUserId(id);
                        users.add(new User(id, username, password, roles));
                    }
                    return null;
                }
            });
            log.debug("findAll: Found {} users for page={} and size={}", users.size(), page, size);
            return users;
        } catch (Exception e) {
            log.error("findAll error for page={} and size={}", page, size, e);
            throw e;
        }
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        final String sql = "SELECT id, username, password FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.execute(sql, (PreparedStatementCallback<User>) ps -> {
                ps.setObject(1, uuid);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        UUID id = resultSet.getObject("id", UUID.class);
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        Set<Role> roles = getRolesByUserId(id);
                        return new User(id, username, password, roles);
                    }
                    return null;
                }
            });
            if (user != null) {
                log.debug("findById: Found user for id={}", uuid);
                return Optional.of(user);
            } else {
                log.info("findById: No user found for id={}", uuid);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("findById error for id={}", uuid, e);
            throw e;
        }
    }

    @Override
    public <S extends User> S save(S user) {
        UUID userId = user.getId() == null ? UuidCreator.getTimeOrderedEpoch() : user.getId();
        final String insertUserSql = "INSERT INTO users (id, username, password) VALUES (?, ?, ?)";
        final String insertRoleSql = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";
        try {
            Integer userRows = jdbcTemplate.execute(insertUserSql, (PreparedStatementCallback<Integer>) ps -> {

                ps.setObject(1, userId);
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getPassword());
                return ps.executeUpdate();
            });
            if (userRows == null || userRows == 0) {
                log.error("save: Failed to insert user {}", user.getUsername());
                throw new RuntimeException("User insertion failed");
            }
            for (Role role : user.getRoles()) {
                jdbcTemplate.execute(insertRoleSql, (PreparedStatementCallback<Integer>) ps -> {
                    ps.setObject(1, userId);
                    ps.setString(2, role.getAuthority());
                    return ps.executeUpdate();
                });
            }
            log.debug("save: Saved user {} with roles {}", user.getUsername(), user.getRoles());
            return user;
        } catch (Exception e) {
            log.error("save error for user {}", user.getUsername(), e);
            throw e;
        }
    }

    @Override
    public <S extends User> S update(UUID id, S user) {
        final String updateSql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        try {
            Integer rowsAffected = jdbcTemplate.execute(updateSql, (PreparedStatementCallback<Integer>) ps -> {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setObject(3, id);
                return ps.executeUpdate();
            });
            if (rowsAffected == null || rowsAffected == 0) {
                log.error("update: Failed to update user with id {}", id);
                throw new RuntimeException("User update failure");
            }
            log.debug("update: Successfully updated fields for user with id {}", id);
            return user;
        } catch (Exception e) {
            log.error("update error for user {}", user.getUsername(), e);
            throw e;
        }
    }

    private Set<Role> getRolesByUserId(UUID userId) {
        final String sql = "SELECT role FROM user_roles WHERE user_id = ?";
        List<String> roleNames = new ArrayList<>();
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setObject(1, userId);
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        roleNames.add(resultSet.getString("role"));
                    }
                    return null;
                }
            });
            Set<Role> roles = roleNames.stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toUnmodifiableSet());
            log.debug("getRolesByUserId: userId={} roles={}", userId, roles);
            return roles;
        } catch (Exception e) {
            log.error("getRolesByUserId error for userId={}", userId, e);
            throw e;
        }
    }

}
