package com.modsen.auth.dto.request;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public record RoleRequest(
        String role
) implements Serializable { }
