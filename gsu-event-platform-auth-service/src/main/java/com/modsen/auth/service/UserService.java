package com.modsen.auth.service;

import com.modsen.auth.dto.request.RegistrationFields;
import com.modsen.auth.dto.request.RoleRequest;
import com.modsen.auth.dto.response.UserResponse;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface UserService {
    void createUser(RegistrationFields request);
    void updateUser(UUID id, RegistrationFields request);
    void assignRole(UUID id, RoleRequest request);
    void deleteUser(UUID id);
    Optional<UserResponse> getUserById(UUID userId);
    Optional<UserResponse> getUserByUsername(String username);
    Collection<UserResponse> getAllUsers(int page, int size);
}
