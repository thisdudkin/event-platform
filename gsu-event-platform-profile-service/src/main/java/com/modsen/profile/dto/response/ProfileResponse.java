package com.modsen.profile.dto.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record ProfileResponse(
        UUID id,
        UUID userId,
        String email,
        String firstname,
        String lastname,
        LocalDate birthdate,
        Character gender,
        String bio
) implements Serializable { }
