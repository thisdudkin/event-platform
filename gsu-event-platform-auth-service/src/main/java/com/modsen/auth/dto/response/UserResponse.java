package com.modsen.auth.dto.response;

import com.modsen.auth.security.user.Role;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record UserResponse(
        UUID id,
        String username,
        String password,
        Set<Role> roles
) implements Serializable { }
