package com.modsen.auth.dto.request;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public record RegistrationFields(
        String username,
        String password
) implements Serializable { }
