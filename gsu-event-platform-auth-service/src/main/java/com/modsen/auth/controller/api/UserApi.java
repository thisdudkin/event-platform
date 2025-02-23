package com.modsen.auth.controller.api;

import com.modsen.auth.dto.request.RegistrationFields;
import com.modsen.auth.dto.request.RoleRequest;
import com.modsen.auth.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface UserApi {
    String URI = "/api/users";

    ResponseEntity<Void> updateUser(UUID id, RegistrationFields request);

    ResponseEntity<Void> assignRole(UUID id, RoleRequest request);

    ResponseEntity<Void> deleteUser(UUID id);

    ResponseEntity<Collection<UserResponse>> getAll(int page, int size);

    ResponseEntity<UserResponse> getById(UUID id);

    ResponseEntity<UserResponse> getByUsername(String username);

}
