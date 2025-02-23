package com.modsen.profile.repository;

import com.modsen.profile.model.Profile;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface ProfileRepository extends CrudRepository<Profile, UUID> {
    boolean existsByEmail(String email);

    boolean existsByUserId(String userId);

    Optional<Profile> findByUserId(String userId);

    Optional<Profile> findByEmail(String email);
}
