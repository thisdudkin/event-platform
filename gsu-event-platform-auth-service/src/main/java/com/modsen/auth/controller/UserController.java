package com.modsen.auth.controller;

import com.modsen.auth.controller.api.UserApi;
import com.modsen.auth.dto.request.RegistrationFields;
import com.modsen.auth.dto.request.RoleRequest;
import com.modsen.auth.dto.response.UserResponse;
import com.modsen.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequestMapping(UserApi.URI)
public class UserController implements UserApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id,
                                           @RequestBody RegistrationFields request) {
        userService.updateUser(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/assign/{id}")
    public ResponseEntity<Void> assignRole(@PathVariable UUID id,
                                           @RequestBody RoleRequest request) {
        userService.assignRole(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Collection<UserResponse>> getAll(@RequestParam int page,
                                                           @RequestParam int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.of(userService.getUserById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<UserResponse> getByUsername(@RequestParam String username) {
        return ResponseEntity.of(userService.getUserByUsername(username));
    }

}
