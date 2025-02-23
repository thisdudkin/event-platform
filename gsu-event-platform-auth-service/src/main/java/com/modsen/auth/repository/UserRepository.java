package com.modsen.auth.repository;

import com.modsen.auth.security.user.Role;
import com.modsen.auth.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface UserRepository extends CrudRepository<User, UUID> {
    boolean existsByUsername(String username);

    boolean assignRole(User user, Role role);

    Optional<User> findByUsername(String username);
}